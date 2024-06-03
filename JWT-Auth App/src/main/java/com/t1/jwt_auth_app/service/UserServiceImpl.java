package com.t1.jwt_auth_app.service;

import com.t1.jwt_auth_app.configuration.properties.SuperUserProperties;
import com.t1.jwt_auth_app.exception.EntityNotFoundException;
import com.t1.jwt_auth_app.exception.RoleException;
import com.t1.jwt_auth_app.model.entity.Role;
import com.t1.jwt_auth_app.model.entity.RoleType;
import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.repository.RoleRepository;
import com.t1.jwt_auth_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private SuperUserProperties superUserProperties;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("User with id {0} not found", id)));
    }

    @Override
    @Transactional
    public User addRole(Long id, RoleType roleType) {
        User user = findById(id);
        Role role = roleRepository.findByAuthority(roleType).orElse(Role.from(roleType));
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public User removeRole(Long id, RoleType roleType) {
        User user = findById(id);
        if (user.getRoles().size() == 1) {
            throw new RoleException("User must have at least one role");
        }
        Role role = roleRepository.findByAuthority(roleType)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Role {0} not found", roleType.name())));
        user.getRoles().remove(role);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existedUser = findById(id);
        existedUser.setName(user.getName());
        existedUser.setEmail(user.getEmail());
        existedUser.setPassword(user.getPassword());
        return userRepository.save(existedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void createSuperUser() {
        User superUser = User.builder()
                .name(superUserProperties.getName())
                .email(superUserProperties.getEmail())
                .password(BCrypt.hashpw(superUserProperties.getPassword(), BCrypt.gensalt()))
                .roles(Set.of(Role.from(RoleType.ROLE_USER),
                        Role.from(RoleType.valueOf(superUserProperties.getRole()))))
                .build();
        if (!userRepository.existsByEmail(superUser.getEmail())) {
            userRepository.save(superUser);
        }
    }
}
