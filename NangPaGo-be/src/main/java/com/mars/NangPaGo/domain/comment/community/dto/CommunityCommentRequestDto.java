package com.mars.NangPaGo.domain.comment.community.dto;

import jakarta.validation.constraints.NotEmpty;

public record CommunityCommentRequestDto(
    @NotEmpty(message = "댓글 내용은 비어 있을 수 없습니다.")
    String content
) {
}