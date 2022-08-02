package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.model.User;
import recipes.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepo;

    public Boolean registeredNewUser(User user) {
        if(userRepo.existsByEmail(user.getEmail())) {
            return false;
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
            userRepo.save(user);
            return true;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }

        return user;
    }
}
