package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
    boolean existsByEmail(String email);
}