package com.mars.app.domain.user_recipe.repository;

import com.mars.common.enums.userRecipe.UserRecipeStatus;
import com.mars.common.model.userRecipe.UserRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRecipeRepository extends JpaRepository<UserRecipe, Long> {

    @Query("SELECT ur FROM UserRecipe ur WHERE (ur.isPublic = true OR ur.user.id = :userId) AND ur.recipeStatus = 'ACTIVE' ORDER BY ur.createdAt DESC")
    Page<UserRecipe> findByIsPublicTrueOrUserIdAndRecipeStatus(@Param("userId") Long userId, Pageable pageable);

    Optional<UserRecipe> findByIdAndRecipeStatus(Long id, UserRecipeStatus status);
}
