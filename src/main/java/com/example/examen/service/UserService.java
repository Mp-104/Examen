package com.example.examen.service;

import com.example.examen.authorities.UserRole;
import com.example.examen.dto.UserDTO;
import com.example.examen.model.CustomUser;
import com.example.examen.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<CustomUser> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public String saveUser(UserDTO user) {

        try {
            if (userRepository.findByUsername(user.username()).isEmpty()) {

                CustomUser newUser = new CustomUser(user.username(), passwordEncoder.encode(user.password()), UserRole.USER);

                newUser.setFirstName(user.firstName());
                newUser.setLastName(user.lastName());
                newUser.setAccountNonLocked(true);
                newUser.setEnabled(true);
                newUser.setAccountNonExpired(true);
                newUser.setCredentialNonExpired(true);

                userRepository.save(newUser);

                return "Användare: " + user.username() + " registrerad";

            } else {
                throw new Exception("Användare med namn: " + user.username() + " finns redan");
            }

        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
