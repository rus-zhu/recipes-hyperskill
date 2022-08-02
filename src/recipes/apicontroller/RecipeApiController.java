package recipes.apicontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
public class RecipeApiController {

    @Autowired
    RecipeService recipeService;

    @PostMapping("/new")
    public ResponseEntity addRecipe(@RequestBody @Valid Recipe recipe,
                                    @AuthenticationPrincipal User user) {
        Long id = recipeService.addNewRecipe(recipe, user);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateRecipe(@PathVariable Long id, @RequestBody @Valid Recipe recipe,
                                       @AuthenticationPrincipal User user) {
        return recipeService.updateRecipeById(id, recipe, user);
    }

    @GetMapping("/{id}")
    public ResponseEntity getRecipe(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipe(id);
        if (recipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(recipe.get());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRecipe(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        return recipeService.deletedRecipeById(id, user);
    }

    @GetMapping("/search")
    public ResponseEntity searchRecipes(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "category", required = false) String category) {
        if (category != null) {
            return ResponseEntity.ok(recipeService.searchByCategory(category));
        } else if (name != null) {
            return ResponseEntity.ok(recipeService.searchByName(name));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
