package com.ssafy.pillme.member.application.service;

import com.ssafy.pillme.global.util.SecurityUtil;
import com.ssafy.pillme.member.application.exception.*;
import com.ssafy.pillme.member.domain.entity.LoginMember;
import com.ssafy.pillme.member.infrastructure.repository.LoginMemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChangePhoneService {
    @Value("${COOLSMS_API_KEY}")
    private String apiKey;

    @Value("${COOLSMS_API_SECRET}")
    private String apiSecret;

    @Value("${COOLSMS_SENDER_NUMBER}")
    private String senderNumber;

    private DefaultMessageService messageService;
    private final RedisTemplate<String, String> redisTemplate;
    private final LoginMemberRepository loginMemberRepository;

    private static final String SMS_CODE_PREFIX = "SMS:";
    private static final String VERIFIED_PREFIX = "VSMS:";
    private static final long CODE_EXPIRATION_TIME = 300000; // 5분
    private static final long VERIFIED_EXPIRATION_TIME = 1800000; // 30분

    @PostConstruct
    public void setupMessageService() {
        if (apiKey == null || apiSecret == null) {
            throw new InvalidChangeSmsApiKeyException();
        }
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    /**
     * 전화번호 변경 프로세스 시작
     */
    public void validateAndSendPhoneVerification(String newPhoneNumber) {
        Long currentMemberId = SecurityUtil.extractCurrentMemberId();
        validateNewPhone(newPhoneNumber, currentMemberId);
        sendVerificationSms(newPhoneNumber);
    }

    /**
     * 전화번호 중복 및 현재값 검증
     */
    private void validateNewPhone(String newPhone, Long memberId) {
        LoginMember member = loginMemberRepository.findById(memberId)
                .orElseThrow(NoMemberInfoException::new);

        if (member.getPhone().equals(newPhone)) {
            throw new SamePhoneNumberException();
        }

        if (loginMemberRepository.existsByPhoneAndDeletedFalse(newPhone)) {
            throw new AlreadyExistPhoneNumberException();
        }

        if (!isValidPhoneNumber(newPhone)) {
            throw new InvalidChangePhoneNumberException();
        }
    }

    /**
     * 인증 SMS 발송
     */
    public SingleMessageSentResponse sendVerificationSms(String phoneNumber) {
        String verificationCode = generateVerificationCode();
        saveVerificationCode(phoneNumber, verificationCode);

        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(phoneNumber);
        message.setText(String.format("[Pillme]\n인증번호: %s\n인증번호는 5분간 유효합니다.", verificationCode));

        try {
            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
            if (!response.getStatusCode().equals("2000")) {
                throw new FailedSmsSendException();
            }
            return response;
        } catch (Exception e) {
            throw new FailedSmsSendException();
        }
    }

    /**
     * SMS 인증번호 검증
     */
    public void verifySmsCode(String phoneNumber, String code) {
        String savedCode = findVerificationCode(phoneNumber);
        if (savedCode == null) {
            Long ttl = redisTemplate.getExpire(SMS_CODE_PREFIX + phoneNumber);
            if (ttl != null && ttl <= 0) {
                throw new ExpiredChangeSmsCodeException();
            }
            throw new FailedSmsSendException();
        }
        if (!savedCode.equals(code)) {
            throw new InvalidChangeSmsCodeException();
        }

        deleteVerificationCode(phoneNumber);
        saveVerifiedStatus(phoneNumber);
    }

    /**
     * 전화번호 변경
     */
    public void changePhone(String newPhoneNumber) {
        Long currentMemberId = SecurityUtil.extractCurrentMemberId();

        if (!isVerified(newPhoneNumber)) {
            throw new NotVerifiedPhoneNumberException();
        }

        // 회원 조회 및 전화번호 변경
        LoginMember member = loginMemberRepository.findByIdAndDeletedFalse(currentMemberId)
                .orElseThrow(NoMemberInfoException::new);

        member.updatePhoneNumber(newPhoneNumber);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^01[0-9]{9}$");
    }

    private boolean isVerified(String phoneNumber) {
        String key = VERIFIED_PREFIX + phoneNumber;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void saveVerificationCode(String phoneNumber, String code) {
        String key = SMS_CODE_PREFIX + phoneNumber;
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    private String findVerificationCode(String phoneNumber) {
        String key = SMS_CODE_PREFIX + phoneNumber;
        return redisTemplate.opsForValue().get(key);
    }

    private void deleteVerificationCode(String phoneNumber) {
        String key = SMS_CODE_PREFIX + phoneNumber;
        redisTemplate.delete(key);
    }

    private void saveVerifiedStatus(String phoneNumber) {
        String key = VERIFIED_PREFIX + phoneNumber;
        redisTemplate.opsForValue().set(key, "true", VERIFIED_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }
}
