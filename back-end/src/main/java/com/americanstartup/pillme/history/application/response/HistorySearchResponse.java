package com.americanstartup.pillme.history.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.americanstartup.pillme.history.domain.History;
import com.americanstartup.pillme.management.domain.Information;
import com.americanstartup.pillme.management.domain.Management;
import java.time.LocalDate;

public record HistorySearchResponse(
        Long informationId,
        String diseaseName,
        String hospital,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate takingDate
) {
    public static HistorySearchResponse of(final History history) {
        Management management = history.getManagement();
        Information information = management.getInformation();
        return new HistorySearchResponse(
                information.getId(),
                information.getDiseaseName(),
                information.getHospital(),
                history.getTakingDate()
        );
    }
}
