package com.ssafy.pillme.management.application.response;


import com.ssafy.pillme.management.domain.Information;
import com.ssafy.pillme.management.domain.item.TakingInformationItem;
import java.time.LocalDate;
import java.util.List;

public record TakingDetailResponse(
        String hospital,
        String diseaseName,
        Long reader,
        boolean isSupplements,
        LocalDate startDate,
        LocalDate endDate,
        List<TakingInformationItem> medicationTakingInfo
) {
    public static TakingDetailResponse of(
            final Information information,
            final List<TakingInformationItem> medicationTakingInfo) {
        return new TakingDetailResponse(
                information.getHospital(),
                information.getDiseaseName(),
                information.getReader().getId(),
                information.isSupplement(),
                information.getStartDate(),
                information.getEndDate(),
                medicationTakingInfo
        );
    }
}
