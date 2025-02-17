package com.ssafy.pillme.notification.application.service;

import com.ssafy.pillme.auth.domain.entity.Member;
import com.ssafy.pillme.notification.application.response.NotificationResponse;
import com.ssafy.pillme.notification.application.response.NotificationSettingResponse;
import com.ssafy.pillme.notification.domain.entity.NotificationSetting;
import com.ssafy.pillme.notification.presentation.request.NotificationConfirmRequest;
import com.ssafy.pillme.notification.presentation.request.NotificationDeleteRequest;
import com.ssafy.pillme.notification.presentation.request.NotificationSettingRequest;

import java.time.LocalTime;
import java.util.List;

public interface NotificationService {
    void createNotificationSetting(NotificationSettingRequest request, Member loginMember);

    NotificationSettingResponse getNotificationSetting(Member loginMember);

    void updateNotificationSetting(NotificationSettingRequest request, Member loginMember);

    void deleteNotificationSetting(Member loginMember);

    void sendDependencyRequestNotification(Member sender, Member receiver);

    void sendDependencyAcceptNotification(Member sender, Member receiver);

    void sendDependencyRejectNotification(Member sender, Member receiver);

    void sendMedicineRequestNotification(Member sender, Member receiver);

    void sendMedicineAcceptNotification(Member sender, Member receiver);

    void sendMedicineRejectNotification(Member sender, Member receiver);

    void sendDependencyDeleteRequestNotification(Member sender, Member receiver);

    void sendDependencyDeleteAcceptNotification(Member sender, Member receiver);

    void sendDependencyDeleteRejectNotification(Member sender, Member receiver);

    List<NotificationResponse> getNotificationList(Member loginMember);

    void readNotifications(NotificationConfirmRequest request, Member loginMember);

    void deleteNotifications(NotificationDeleteRequest request, Member loginMember);

    List<NotificationSetting> getNotificationSettingListForCurrentTime(LocalTime currentTime);

    void sendProtectorToDependentNotification(Member sender, Member receiver);

    void sendChatNotification(Long chatRoomId, Member sender, Member receiver, String message, Long sendTime);

    // 처방전 등록 요청 알림
    void sendTakingInformationNotification(Member sender, Member receiver, String diseaseName);

    // 처방전 등록 수락 알림
    void sendTakingInformationAcceptNotification(Member sender, Member receiver, String diseaseName);

    // 처방전 등록 거절 알림
    void sendTakingInformationRejectNotification(Member sender, Member receiver, String diseaseName);

    // 처방전 삭제 요청 알림
    void sendTakingInformationDeleteRequestNotification(Member sender, Member receiver, String diseaseName);

    // 처방전 삭제 수락 알림
    void sendTakingInformationDeleteAcceptNotification(Member sender, Member receiver, String diseaseName);

    // 처방전 삭제 거절 알림
    void sendTakingInformationDeleteRejectNotification(Member sender, Member receiver, String diseaseName);
}
