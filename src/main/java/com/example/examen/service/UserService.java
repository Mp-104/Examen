package com.example.examen.service;

import com.example.examen.authorities.UserRole;
import com.example.examen.dao.IUserDAO;
import com.example.examen.dto.PasswordDT0;
import com.example.examen.dto.UserDTO;
import com.example.examen.model.CustomUser;
import com.example.examen.principal.MyPrincipal;
import com.example.examen.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.examen.principal.MyPrincipal.getLoggedInUser;

@Service
@Transactional
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final IUserDAO userDAO;

    public UserService(PasswordEncoder passwordEncoder, IUserDAO userDAO) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
    }

    @Override
    public Optional<CustomUser> findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public String saveUser(UserDTO user) {

        try {
            if (userDAO.findByUsername(user.username()).isEmpty()) {

                CustomUser newUser = new CustomUser(user.username(), passwordEncoder.encode(user.password()), UserRole.USER);

                newUser.setFirstName(user.firstName());
                newUser.setLastName(user.lastName());
                newUser.setAccountNonLocked(true);
                newUser.setEnabled(true);
                newUser.setAccountNonExpired(true);
                newUser.setCredentialNonExpired(true);

                userDAO.save(newUser);

                return "Användare: " + user.username() + " registrerad";

            } else {
                throw new Exception("Användare med namn: " + user.username() + " finns redan");
            }

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @Override
    public String changePassword(PasswordDT0 password) {

        CustomUser user = findUserByUsername(getLoggedInUser()).get();

        if (passwordEncoder.matches(password.currentPassword(), user.getPassword())) {

            user.setPassword(passwordEncoder.encode(password.newPassword()));
            //userDAO.save(user);

            return "Lösenord ändrat!";
        }

        return "Fel lösenord angivet";

    }

    @Override
    public void disableUser() {

        CustomUser user = findUserByUsername(getLoggedInUser()).get();

        user.setEnabled(false);

    }
}
