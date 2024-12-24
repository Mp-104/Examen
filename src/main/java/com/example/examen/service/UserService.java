package com.example.examen.service;

import com.example.examen.authorities.UserRole;
import com.example.examen.dao.IUserDAO;
import com.example.examen.dto.PasswordDT0;
import com.example.examen.dto.UserDTO;
import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    @Cacheable(cacheNames = "users_by_username", key = "#username")
    public Optional<CustomUser> findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public Optional<CustomUser> findUserById(Long id) {
        return userDAO.findUserById(id);
    }

    @Override
    public String saveUser(UserDTO user) {

        try {
            if (userDAO.findByUsername(user.username()).isEmpty()) {

                CustomUser newUser = new CustomUser(user.username(), passwordEncoder.encode(user.password()), UserRole.USER);

                newUser.setFirstName(user.firstName());
                newUser.setLastName(user.lastName());
                newUser.setIsAccountNonLocked(true);
                newUser.setIsEnabled(true);
                newUser.setIsAccountNonExpired(true);
                newUser.setIsCredentialNonExpired(true);

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

        user.setIsEnabled(false);

    }

    @Override
    //@Cacheable(cacheNames = "all_users", key = "all")
    public List<CustomUser> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public CustomUser editUser(CustomUser customUser) {

        Optional<CustomUser> optionalCustomUser = userDAO.findUserById(customUser.getId());

        if (optionalCustomUser.isPresent()) {

            CustomUser user = userDAO.findUserById(customUser.getId()).get();

            user.setFirstName(customUser.getFirstName());
            user.setLastName(customUser.getLastName());
            user.setUsername(customUser.getUsername());
            user.setPassword(passwordEncoder.encode(customUser.getPassword()));

            user.setUserRole(customUser.getUserRole());

            user.setIsAccountNonExpired(customUser.getIsAccountNonExpired());
            user.setIsAccountNonLocked(customUser.getIsAccountNonLocked());
            user.setIsCredentialNonExpired(customUser.getIsCredentialNonExpired());
            user.setIsEnabled(customUser.getIsEnabled());

            userDAO.save(user);

            return user;
        }

        return optionalCustomUser.get();

    }

    @Override
    public String deleteUserById(Long id) {

        if (id == 1) {
            return "Kan inte ta bort denna användare";
        }

        if (userDAO.findUserById(id).isPresent()) {

            CustomUser userToDelete = userDAO.findUserById(id).get();

            CustomUser admin = userDAO.findUserById(1L).get();

            for (Personnel personnel : userToDelete.getPersonnelList()) {

                personnel.setCustomUser(admin);

            }

            List<Personnel> updatedPersonnelList = admin.getPersonnelList();

            updatedPersonnelList.addAll(userToDelete.getPersonnelList());

            userToDelete.setPersonnelList(null);

            admin.setPersonnelList(updatedPersonnelList);

            userDAO.deleteById(id);

            return "Användare med id: " + id + " borttagen!";
        }


        return "Användare med id: " + id + " inte borttagen.";
    }
}
