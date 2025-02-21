package com.ssafy.pillme.management.presentation.request;

import com.ssafy.pillme.management.domain.item.TakingSettingItem;

public record AddTakingInformationRequest(
        String medicationName,
        Integer period,
        boolean morning,
        boolean lunch,
        boolean dinner,
        boolean sleep
) {
    public TakingSettingItem toItem() {
        return new TakingSettingItem(
                medicationName,
                period,
                morning,
                lunch,
                dinner,
                sleep
        );
    }
}
