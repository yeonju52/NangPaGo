package com.mars.app.domain.recipe.service;

import static com.mars.common.exception.NPGExceptionType.NOT_FOUND_COMMENT;
import static com.mars.common.exception.NPGExceptionType.NOT_FOUND_RECIPE;
import static com.mars.common.exception.NPGExceptionType.NOT_FOUND_USER;
import static com.mars.common.exception.NPGExceptionType.UNAUTHORIZED_NO_AUTHENTICATION_CONTEXT;

import com.mars.common.dto.page.PageResponseDto;
import com.mars.app.domain.recipe.dto.comment.RecipeCommentRequestDto;
import com.mars.app.domain.recipe.dto.comment.RecipeCommentResponseDto;
import com.mars.common.dto.page.PageRequestVO;
import com.mars.common.model.comment.recipe.RecipeComment;
import com.mars.app.domain.recipe.repository.RecipeCommentRepository;
import com.mars.common.model.recipe.Recipe;
import com.mars.app.domain.recipe.repository.RecipeRepository;
import com.mars.common.model.user.User;
import com.mars.app.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RecipeCommentService {

    private final RecipeCommentRepository recipeCommentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public PageResponseDto<RecipeCommentResponseDto> pagedCommentsByRecipe(Long recipeId, Long userId, PageRequestVO pageRequestVO) {
        Recipe recipe = findRecipeById(recipeId);
        return PageResponseDto.of(recipeCommentRepository.findByRecipeId(recipeId, pageRequestVO.toPageable())
                .map(comment -> RecipeCommentResponseDto.from(comment, recipe, userId))
        );
    }

    @Transactional
    public RecipeCommentResponseDto create(RecipeCommentRequestDto requestDto, Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NOT_FOUND_USER::of);

        Recipe recipe = findRecipeById(recipeId);
        RecipeComment newComment = RecipeComment.create(recipe, user, requestDto.content());
        RecipeComment savedComment = recipeCommentRepository.save(newComment);
        return RecipeCommentResponseDto.from(savedComment, recipe, userId);
    }

    @Transactional
    public RecipeCommentResponseDto update(Long commentId, Long userId, RecipeCommentRequestDto requestDto) {
        RecipeComment comment = findCommentById(commentId);
        validateOwnership(comment, userId);
        comment.updateText(requestDto.content());
        return RecipeCommentResponseDto.from(comment, comment.getRecipe(), userId);
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        RecipeComment comment = findCommentById(commentId);
        validateOwnership(comment, userId);
        recipeCommentRepository.delete(comment);
    }

    public int countCommentsByRecipe(Long recipeId) {
        findRecipeById(recipeId);
        return recipeCommentRepository.countByRecipeId(recipeId);
    }

    private void validateOwnership(RecipeComment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw UNAUTHORIZED_NO_AUTHENTICATION_CONTEXT.of("댓글을 수정/삭제할 권한이 없습니다.");
        }
    }

    private Recipe findRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
            .orElseThrow(() -> NOT_FOUND_RECIPE.of("레시피를 찾을 수 없습니다."));
    }

    private RecipeComment findCommentById(Long commentId) {
        return recipeCommentRepository.findById(commentId)
            .orElseThrow(() -> NOT_FOUND_COMMENT.of("댓글을 찾을 수 없습니다."));
    }
}
