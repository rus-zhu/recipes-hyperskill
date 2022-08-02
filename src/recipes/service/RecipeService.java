package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepo;

    public Long addNewRecipe(Recipe recipe, User user) {
        recipe.setUser(user);
        recipeRepo.save(recipe);
        return recipe.getId();
    }

    public Optional<Recipe> getRecipe(Long id) {;
        return recipeRepo.findById(id);
    }

    public ResponseEntity deletedRecipeById(Long id, User user) {
        Optional<Recipe> recipe = recipeRepo.findById(id);
        if (recipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!recipe.get().getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        recipeRepo.delete(recipe.get());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity updateRecipeById(Long id, Recipe newRecipe, User user) {
        Optional<Recipe> oldRecipe = recipeRepo.findById(id);
        if (oldRecipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if (!oldRecipe.get().getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            newRecipe.setId(oldRecipe.get().getId());
            newRecipe.setUser(user);
            recipeRepo.save(newRecipe);
            return ResponseEntity.noContent().build();
        }
    }

    public List<Recipe> searchByCategory(String category) {
        return recipeRepo.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> searchByName(String name) {
        return recipeRepo.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }
}
