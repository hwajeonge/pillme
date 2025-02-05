package com.ssafy.pillme.dependency.presentation.controller;

import com.ssafy.pillme.dependency.application.response.DependentListResponse;
import com.ssafy.pillme.dependency.application.service.DependencyService;
import com.ssafy.pillme.dependency.presentation.request.*;
import com.ssafy.pillme.global.response.JSONResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dependency")
@RequiredArgsConstructor
public class DependencyController {
    private final DependencyService dependencyService;

    // 보호자가 피보호자 등록 요청
    @PostMapping
    public ResponseEntity<JSONResponse<Void>> requestDependency(@RequestBody DependentPhoneRequest request) {
        dependencyService.requestDependency(request);
        return ResponseEntity.ok(JSONResponse.onSuccess());
    }

    // 피보호자가 보호자 등록 요청 수락
    @PostMapping("/accept")
    public ResponseEntity<JSONResponse<Void>> acceptDependency(@RequestBody DependencyAcceptRequest request) {
        dependencyService.acceptDependency(request);
        return ResponseEntity.ok(JSONResponse.onSuccess());
    }

    // 피보호자가 보호자 등록 요청 거절
    @PostMapping("/reject")
    public ResponseEntity<JSONResponse<Void>> rejectDependency(@RequestBody DependencyRejectRequest request) {
        dependencyService.rejectDependency(request);
        return ResponseEntity.ok(JSONResponse.onSuccess());
    }

    //TODO: memberService 추가 후, 테스트 필요
    // 로컬 회원(피보호자) 등록
    @PostMapping("/local-member")
    public ResponseEntity<JSONResponse<Void>> createLocalMember(@RequestBody LocalMemberRequest request) {
        dependencyService.createLocalMemberWithDependency(request);
        return ResponseEntity.ok(JSONResponse.onSuccess());
    }

    // 피보호자 관리 목록
    @GetMapping("/dependents")
    public ResponseEntity<JSONResponse<List<DependentListResponse>>> getDependents() {
        return ResponseEntity.ok(JSONResponse.onSuccess(dependencyService.getDependents()));
    }

    /* 가족 관계 삭제 요청 - 보호자와 피보호자 모두 삭제 요청가능
     * dependencyId(가족 관계 ID)를 받아서 삭제
     * 현재 로그인한 사용자가 삭제를 요청하는 입장.
     * 삭제 요청을 보내는 회원이 sender, 삭제 요청을 받는 회원이 receiver
     * */
    @PostMapping("/delete/{dependencyId}")
    public ResponseEntity<JSONResponse<Void>> deleteRequestDependency(@PathVariable Long dependencyId) {
        dependencyService.deleteRequestDependency(dependencyId);
        return ResponseEntity.ok(JSONResponse.onSuccess());
    }

    /*
    * 가족 관계 삭제 요청 수락 - 삭제 요청을 받은 회원이 삭제 요청을 수락하는 경우
    * 메시지에 존재하는 senderId를 통해 삭제 요청을 보낸 회원을 찾아서 삭제 요청을 수락
    * */
    @DeleteMapping("/delete/accept")
    public ResponseEntity<JSONResponse<Void>> acceptDeleteDependency(@RequestBody AcceptDependencyDeletionRequest request) {
        dependencyService.acceptDeleteDependency(request);
        return ResponseEntity.ok(JSONResponse.onSuccess());
    }
}
