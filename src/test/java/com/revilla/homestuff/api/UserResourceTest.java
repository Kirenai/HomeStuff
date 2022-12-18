package com.revilla.homestuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.jwt.JwtAuthenticationEntryPoint;
import com.revilla.homestuff.security.jwt.JwtTokenFilter;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.service.imp.AuthUserDetailsServiceImp;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import com.revilla.homestuff.utils.dto.response.ApiResponseDataTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

@WebMvcTest(value = UserResource.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenProvider.class)
})
class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtTokenFilter tokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthUserDetailsServiceImp userDetailsServiceImp;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private UserDto userDtoMockOne;

    private final StringBuilder URL = new StringBuilder("/api/users");
    private String token;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        Long userIdOne = 1L;

        String username = "kirenai";
        String password = "kirenai";
        String firstName = "kirenai";
        String lastName = "kirenai";
        Byte age = 22;

        User userMockOne = UserServiceDataTestUtils.getUserMock(userIdOne, username,
                password, firstName, lastName, age);
        userMockOne.setRoles(List.of(RoleServiceDataTestUtils.getRoleMock(1L, RoleName.ROLE_ADMIN)));
        this.userDtoMockOne = UserServiceDataTestUtils.getUserDtoMock(userIdOne,
                username, password, firstName, lastName, age);

        this.token = this.jwtTokenProvider.getTokenPrefix() +
                this.jwtTokenProvider.generateJwtToken(
                        new UsernamePasswordAuthenticationToken(
                                userMockOne.getUsername(),
                                userMockOne.getPassword()
                        )
                );

        Mockito.when(this.userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(userMockOne));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should get users")
    void shouldGetUsers() throws Exception {
        Mockito.when(this.userService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.userDtoMockOne));

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(this.userDtoMockOne.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(this.userDtoMockOne.getUsername()));

    }

    @Test
    @DisplayName("Should get user")
    void shouldGetUser() throws Exception {
        Mockito.when(this.userService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenReturn(userDtoMockOne);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/1").toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(this.userDtoMockOne.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(this.userDtoMockOne.getUsername()));
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() throws Exception {
        Mockito.when(this.userService.create(Mockito.any(UserDto.class)))
                .thenReturn(this.userDtoMockOne);

        RequestBuilder request = MockMvcRequestBuilders
                .post(this.URL.toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userDtoMockOne));

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(this.userDtoMockOne.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(this.userDtoMockOne.getUsername()));
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() throws Exception {
        ApiResponseDto updateResponse = ApiResponseDataTestUtils
                .getApiResponseMock("updated successfully", User.class);
        Mockito.when(this.userService.update(Mockito.anyLong(), Mockito.any(UserDto.class), Mockito.any()))
                .thenReturn(updateResponse);

        RequestBuilder request = MockMvcRequestBuilders
                .put(this.URL.append("/1").toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userDtoMockOne));

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(updateResponse.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(updateResponse.getMessage()));
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() throws Exception {
        ApiResponseDto deleteResponse = ApiResponseDataTestUtils
                .getApiResponseMock("deleted successfully", User.class);
        Mockito.when(this.userService.delete(Mockito.anyLong(), Mockito.any()))
                .thenReturn(deleteResponse);

        RequestBuilder request = MockMvcRequestBuilders
                .delete(this.URL.append("/1").toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(deleteResponse.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(deleteResponse.getMessage()));
    }

}