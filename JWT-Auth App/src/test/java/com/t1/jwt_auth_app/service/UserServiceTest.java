package com.t1.jwt_auth_app.service;

import com.t1.jwt_auth_app.exception.EntityNotFoundException;
import com.t1.jwt_auth_app.exception.RoleException;
import com.t1.jwt_auth_app.model.entity.Role;
import com.t1.jwt_auth_app.model.entity.RoleType;
import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.repository.RoleRepository;
import com.t1.jwt_auth_app.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final UserService userService = new UserServiceImpl(userRepository, roleRepository);


    @Test
    void testFindById_ExistingUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.findById(1L);

        assertThat(user.getId()).isEqualTo(foundUser.getId());
    }

    @Test
    void testFindById_NonExistingUser() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findById(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with id 2 not found");
    }

    @Test
    void testGetUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "user", "user@mail.ru", "userpassword", Instant.now(), Set.of(Role.from(RoleType.ROLE_USER))));
        userList.add(new User(2L, "admin", "admin@mail.ru", "adminpassword", Instant.now(), Set.of(Role.from(RoleType.ROLE_ADMIN))));

        when(userRepository.findAll()).thenReturn(userList);

        List<User> retrievedUsers = userService.getUsers();

        assertThat(retrievedUsers).hasSize(2);
        assertThat(retrievedUsers.get(0).getName()).isEqualTo("user");
    }

    @Test
    void testAddRole() {
        Set<Role> roles = new HashSet<>(Set.of(Role.from(RoleType.ROLE_USER)));
        User user = new User(1L, "user", "user@mail.ru",
                "userpassword", Instant.now(), roles);

        RoleType roleType = RoleType.ROLE_ADMIN;
        Role role = Role.from(roleType);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByAuthority(roleType)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.addRole(1L, roleType);

        assertThat(updatedUser.getRoles()).hasSize(2);
        assertThat(updatedUser.getRoles()).contains(Role.from(RoleType.ROLE_ADMIN));
        verify(userRepository, times(1))
                .save(updatedUser);
    }

    @Test
    void testRemoveRole() {
        Set<Role> roles = new HashSet<>(Set.of(Role.from(RoleType.ROLE_USER),
                Role.from(RoleType.ROLE_ADMIN)));
        User user = new User(1L, "user", "user@mail.ru",
                "userpassword", Instant.now(), roles);
        RoleType roleType = RoleType.ROLE_ADMIN;
        Role role = Role.from(roleType);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByAuthority(roleType)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.removeRole(1L, RoleType.ROLE_ADMIN);

        assertThat(updatedUser.getRoles()).hasSize(1);
        assertThat(updatedUser.getRoles()).contains(Role.from(RoleType.ROLE_USER));
        assertThat(updatedUser.getRoles()).doesNotContain(Role.from(RoleType.ROLE_ADMIN));
        verify(userRepository, times(1))
                .save(updatedUser);
    }

    @Test
    void testRemoveRole_withOneRole() {
        Set<Role> roles = new HashSet<>(Set.of(Role.from(RoleType.ROLE_USER)));
        User user = new User(1L, "user", "user@mail.ru",
                "userpassword", Instant.now(), roles);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.removeRole(1L, RoleType.ROLE_USER))
                .isInstanceOf(RoleException.class)
                .hasMessage("User must have at least one role");
        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void testRemoveRole_roleNotFound() {
        Set<Role> roles = new HashSet<>(Set.of(Role.from(RoleType.ROLE_USER),
                Role.from(RoleType.ROLE_ADMIN)));
        User user = new User(1L, "user", "user@mail.ru",
                "userpassword", Instant.now(), roles);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByAuthority(any(RoleType.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.removeRole(1L, RoleType.ROLE_ADMIN))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Role ROLE_ADMIN not found");
        verify(userRepository, never())
                .save(any(User.class));
    }
}
