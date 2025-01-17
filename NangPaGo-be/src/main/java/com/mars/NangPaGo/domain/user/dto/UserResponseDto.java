package com.mars.NangPaGo.domain.user.dto;

import com.mars.NangPaGo.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserResponseDto(
    String email,
    String nickname,
    String role
) {

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
            .email(user.getEmail())
            .nickname(user.getNickname())
            .role(user.getRole())
            .build();
    }
}
