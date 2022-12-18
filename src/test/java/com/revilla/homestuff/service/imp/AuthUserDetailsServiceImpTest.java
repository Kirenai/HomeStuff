package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUserDetailsServiceImpTest {

    @InjectMocks
    private AuthUserDetailsServiceImp userDetailsService;
    @Mock
    private UserRepository userRepository;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        Long userId = 1L;
        String username = "user";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        Byte age = 20;

        Long roleId = 1L;
        RoleName roleName = RoleName.ROLE_USER;

        this.user = UserServiceDataTestUtils.getUserMock(userId, username, password, firstName, lastName, age);
        this.role = RoleServiceDataTestUtils.getRoleMock(roleId, roleName);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        String username = "user";
        String messageExpected = "User not found with username: " + username;

        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        BadCredentialsException ex = assertThrows(BadCredentialsException.class,
                () -> this.userDetailsService.loadUserByUsername(username)
        );

        assertEquals(messageExpected, ex.getMessage());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Should return user details when user found")
    void shouldReturnUserDetailsWhenUserFound() {
        String username = "user";
        int userRepoTimes = 1;

        this.user.setRoles(Set.of(this.role));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(this.user));

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        assertEquals(this.user.getUsername(), userDetails.getUsername());
        assertEquals(this.user.getPassword(), userDetails.getPassword());

        verify(userRepository, times(userRepoTimes)).findByUsername(username);
    }
}