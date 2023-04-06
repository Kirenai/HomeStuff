package com.revilla.homestuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.RoleService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

@WebMvcTest(value = RoleResource.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenProvider.class)
})
@WithMockUser
class RoleResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserRepository userRepository;

    private final StringBuilder URL = new StringBuilder("/api/roles");

    private RoleDto roleDtoOneMock;

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

        this.roleDtoOneMock = RoleServiceDataTestUtils.getRoleDtoMock(3L, RoleName.ROLE_ADMIN);

        Mockito.when(this.userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(userMockOne));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should return all roles")
    void getAllRolesWhenGetRoles() throws Exception {
        List<RoleDto> roles = RoleServiceDataTestUtils.getMockRoleDtoList();
        Mockito.when(this.roleService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(roles);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.toString())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roleId").value(roles.get(0).getRoleId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(roles.get(0).getName().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].roleId").value(roles.get(1).getRoleId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(roles.get(1).getName().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].roleId").value(roles.get(2).getRoleId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(roles.get(2).getName().name()));

    }

    @Test
    @DisplayName("Should return role by id")
    void getRoleByIdWhenFindOne() throws Exception {
        Long roleId = 1L;
        Mockito.when(this.roleService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.roleDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/").append(roleId).toString())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleId").value(this.roleDtoOneMock.getRoleId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.roleDtoOneMock.getName().name()));
    }

    @Test
    @DisplayName("Should create role")
    void createRoleWhenCreateRole() throws Exception {
        Mockito.when(this.roleService.create(Mockito.any(RoleDto.class)))
                .thenReturn(this.roleDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .post(this.URL.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsBytes(this.roleDtoOneMock))
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleId").value(this.roleDtoOneMock.getRoleId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.roleDtoOneMock.getName().name()));
    }

    @Test
    @DisplayName("Should update role")
    void updateRoleWhenUpdateRole() throws Exception {
        Long roleId = 1L;
        ApiResponseDto apiResponseDto = ApiResponseDataTestUtils
                .getApiResponseMock("updated successfully", Role.class);
        Mockito.when(this.roleService.update(Mockito.anyLong(), Mockito.any(RoleDto.class)))
                .thenReturn(apiResponseDto);

        RequestBuilder request = MockMvcRequestBuilders
                .put(this.URL.append("/").append(roleId).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsBytes(this.roleDtoOneMock))
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(apiResponseDto.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(apiResponseDto.getMessage()));
    }

}