package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private User userMockOne;
    private UserDto userDtoMockOne;

    private String token;

    @BeforeEach
    void setUp() {
        Long userIdOne = 1L;

        String username = "kirenai";
        String password = "kirenai";
        String firstName = "kirenai";
        String lastName = "kirenai";
        Byte age = 22;

        String SECRET_KEY = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
        this.token = Jwts.builder()
                .setSubject(username)
                .setExpiration(java.sql.Date.valueOf(LocalDate.now()
                        .plusDays(1)))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

        this.userMockOne = UserServiceDataTestUtils.getMockUser(userIdOne, username,
                password, firstName, lastName, age);
        this.userMockOne.setRoles(List.of(RoleServiceDataTestUtils.getMockRole(1L, RoleName.ROLE_ADMIN)));
        this.userDtoMockOne = UserServiceDataTestUtils.getMockUserDto(userIdOne,
                username, password, firstName, lastName, age);

        Mockito.when(this.userRepository.findByUsername(Mockito.anyString()))
                        .thenReturn(Optional.of(userMockOne));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldGetUsers() throws Exception {
        Mockito.when(this.userRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(userMockOne)));
        Mockito.when(this.userService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(List.of(userDtoMockOne));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users")
                .header("Authorization", "Bearer " + this.token)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(this.userDtoMockOne.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(this.userDtoMockOne.getUsername()));

    }

    @Test
    void shouldGetUser() throws Exception {
        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userMockOne));
        Mockito.when(this.userService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenReturn(userDtoMockOne);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/1")
                .header("Authorization", "Bearer " + this.token)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(this.userDtoMockOne.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(this.userDtoMockOne.getUsername()));
    }

}