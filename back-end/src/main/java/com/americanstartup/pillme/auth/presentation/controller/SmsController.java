package com.americanstartup.pillme.auth.presentation.controller;

import com.americanstartup.pillme.auth.application.service.SmsService;
import com.americanstartup.pillme.auth.presentation.request.SendSmsVerificationRequest;
import com.americanstartup.pillme.auth.presentation.request.VerifySmsCodeRequest;
import com.americanstartup.pillme.global.response.JSONResponse;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/sms/verification")
    public ResponseEntity<JSONResponse<SingleMessageSentResponse>> sendSmsVerification(@RequestBody SendSmsVerificationRequest request) {
        SingleMessageSentResponse response = smsService.sendVerificationSms(request.phoneNumber());
        return ResponseEntity.ok(JSONResponse.onSuccess(response));
    }

    @PostMapping("/sms/verify")
    public ResponseEntity<JSONResponse<Void>> verifySmsCode(@RequestBody VerifySmsCodeRequest request) {
        smsService.verifySmsCode(request.phoneNumber(), request.code());
        return ResponseEntity.ok(JSONResponse.onSuccess());
    }

    @PostMapping("/sms/test")
    public ResponseEntity<JSONResponse<SingleMessageSentResponse>> test() {
        SingleMessageSentResponse response = smsService.sendVerificationSms("01024137151");
        return ResponseEntity.ok(JSONResponse.onSuccess(response));
    }
}

