package com.ssafy.pillme.admin.application.response;

import com.ssafy.pillme.auth.domain.vo.Gender;
import com.ssafy.pillme.auth.domain.vo.Provider;

public record SinginMemberResponse(Long id,
                                   String name,
                                   String email,
                                   String phone,
                                   String nickname,
                                   String birthday,
                                   Gender gender,
                                   boolean oauth,
                                   Provider provider,
                                   boolean deleted) {
}
