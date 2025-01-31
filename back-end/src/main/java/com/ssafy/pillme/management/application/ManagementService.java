package com.ssafy.pillme.management.application;

import com.ssafy.pillme.auth.domain.entity.Member;
import com.ssafy.pillme.global.code.ErrorCode;
import com.ssafy.pillme.management.application.exception.NoInformationException;
import com.ssafy.pillme.management.application.exception.NoMedicationException;
import com.ssafy.pillme.management.application.response.TakingDetailResponse;
import com.ssafy.pillme.management.application.response.TakingInformationResponse;
import com.ssafy.pillme.management.domain.Information;
import com.ssafy.pillme.management.domain.Management;
import com.ssafy.pillme.management.infrastructure.InformationRepository;
import com.ssafy.pillme.management.infrastructure.ManagementRepository;
import com.ssafy.pillme.management.presentation.request.MedicationRegisterRequest;
import com.ssafy.pillme.management.presentation.request.TakingInformationRequest;
import com.ssafy.pillme.search.domain.Medication;
import com.ssafy.pillme.search.infrastructure.MedicationRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManagementService {
    private final ManagementRepository managementRepository;
    private final InformationRepository informationRepository;
    private final MedicationRepository medicationRepository;

    public void saveTakingInformation(MedicationRegisterRequest request) {
        Member writer = Member.builder().build();
        Member reader = Member.builder().build();
        Information savedInformation = informationRepository.save(request.toInformation(writer, reader));
        saveManagement(request.takingInfoDto(), savedInformation);
    }


    private void saveManagement(List<TakingInformationRequest> takingList, Information information) {
        for (TakingInformationRequest taking : takingList) {
            Medication medication = medicationRepository.findById(taking.medicationId())
                    .orElseThrow(() -> new NoMedicationException(ErrorCode.MEDICATION_NOT_FOUND));

            Management management = taking.toManagement(medication, information);
            managementRepository.save(management);
        }
    }

    public TakingDetailResponse findByInformationId(Long id) {
        Information information = informationRepository.findById(id)
                .orElseThrow(() -> new NoInformationException(ErrorCode.INFORMATION_NOT_FOUND));

        return TakingDetailResponse.of(information, TakingInformationResponse.from(information.getManagements()));
    }


    public void findManagementByDate(final LocalDate localDate) {

    }
}
