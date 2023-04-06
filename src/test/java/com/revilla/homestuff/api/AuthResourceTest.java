package com.revilla.homestuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revilla.homestuff.dto.request.LoginRequestDto;
import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.jwt.JwtAuthenticationEntryPoint;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.AuthService;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.service.imp.AuthUserDetailsServiceImp;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import com.revilla.homestuff.utils.dto.request.LoginRequestDtoDataTest;
import com.revilla.homestuff.utils.dto.request.RegisterRequestDtoDataTest;
import com.revilla.homestuff.utils.dto.response.ApiResponseDataTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

@WebMvcTest(value = AuthResource.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenProvider.class)
})
@WithMockUser
class AuthResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private AuthUserDetailsServiceImp userDetailsServiceImp;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final StringBuilder URL = new StringBuilder("/api/auth");

    private User userMockOne;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        Long userIdOne = 1L;

        String username = "kirenai";
        String password = "kirenai";
        String firstName = "kirenai";
        String lastName = "kirenai";
        Byte age = 22;

        this.userMockOne = UserServiceDataTestUtils.getUserMock(userIdOne, username,
                password, firstName, lastName, age);
        this.userMockOne.setRoles(List.of(RoleServiceDataTestUtils.getRoleMock(1L, RoleName.ROLE_ADMIN)));

        Mockito.when(this.userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(userMockOne));
    }

    @Test
    @DisplayName("Should log in")
    void when_login_then_returnAuthUserDetails() throws Exception {
        LoginRequestDto loginRequestDto = LoginRequestDtoDataTest.getLoginRequestDtoMock();

        ResponseEntity<AuthUserDetails> authUserDetailsResponseMock = ResponseEntity
                .ok()
                .body(new AuthUserDetails(this.userMockOne));

        Mockito.when(this.authService.login(Mockito.any(LoginRequestDto.class)))
                .thenReturn(authUserDetailsResponseMock);

        RequestBuilder request = MockMvcRequestBuilders
                .post(this.URL.append("/login").toString())
                .content(this.objectMapper.writeValueAsString(loginRequestDto))
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        this.objectMapper.writeValueAsString(authUserDetailsResponseMock.getBody())
                ));
    }

    @Test
    @DisplayName("Should register")
    void when_register_then_returnApiResponseDto() throws Exception {
        RegisterRequestDto mockRegisterRequestDto = RegisterRequestDtoDataTest
                .getRegisterRequestDtoMock();
        ApiResponseDto apiResponseMock = ApiResponseDataTestUtils.
                getApiResponseMock("registered successfully", User.class);

        Mockito.when(this.authService.register(Mockito.any(RegisterRequestDto.class)))
                .thenReturn(apiResponseMock);

        RequestBuilder request = MockMvcRequestBuilders
                .post(this.URL.append("/register").toString())
                .content(this.objectMapper.writeValueAsString(mockRegisterRequestDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockRegisterRequestDto.setPassword(null);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(this.objectMapper.writeValueAsString(apiResponseMock)));
    }


}