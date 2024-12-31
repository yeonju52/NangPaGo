package com.mars.NangPaGo.domain.favorite.recipe.repository;

import com.mars.NangPaGo.domain.favorite.recipe.entity.RecipeFavorite;
import com.mars.NangPaGo.domain.recipe.entity.Recipe;
import com.mars.NangPaGo.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeFavoriteRepository extends JpaRepository<RecipeFavorite, Long> {

    Optional<RecipeFavorite> findByUserAndRecipe(User user, Recipe recipe);

    @Query("SELECT rf FROM RecipeFavorite rf WHERE rf.user.email = :email AND rf.recipe.id = :recipeId")
    Optional<RecipeFavorite> findByEmailAndRecipeId(@Param("email") String email, @Param("recipeId") Long recipeId);
}