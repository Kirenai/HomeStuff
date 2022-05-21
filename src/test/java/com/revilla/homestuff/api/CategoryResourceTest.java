package com.revilla.homestuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.jwt.JwtAuthenticationEntryPoint;
import com.revilla.homestuff.security.jwt.JwtTokenFilter;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.CategoryService;
import com.revilla.homestuff.service.imp.AuthUserDetailsServiceImp;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.CategoryServiceDataTestUtils;
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

import java.util.List;
import java.util.Optional;

@WebMvcTest(value = CategoryResource.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenProvider.class)
})
class CategoryResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtTokenFilter tokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthUserDetailsServiceImp userDetailsServiceImp;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final StringBuilder URL = new StringBuilder("/api/categories");
    private String token;

    private CategoryDto categoryDtoOneMock;

    @BeforeEach
    void setUp() {
        Long userIdOne = 1L;

        String username = "kirenai";
        String password = "kirenai";
        String firstName = "kirenai";
        String lastName = "kirenai";
        Byte age = 22;

        User userMockOne = UserServiceDataTestUtils.getMockUser(userIdOne, username,
                password, firstName, lastName, age);
        userMockOne.setRoles(List.of(RoleServiceDataTestUtils.getMockRole(1L, RoleName.ROLE_ADMIN)));

        this.categoryDtoOneMock = CategoryServiceDataTestUtils.getCategoryDtoMock(1L, "Category 1");

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
    @DisplayName("Should return all categories")
    void when_getAllCategories_then_returnAllCategories() throws Exception {
        List<CategoryDto> categoryDtoListMock = CategoryServiceDataTestUtils.getCategoryDtoListMock();
        Mockito.when(this.categoryService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(categoryDtoListMock);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.content().json(this.objectMapper.writeValueAsString(categoryDtoListMock)));
    }

    @Test
    @DisplayName("Should return category by id")
    void when_getCategoryById_then_returnCategory() throws Exception {
        Long categoryId = 1L;
        Mockito.when(this.categoryService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.categoryDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/").append(categoryId).toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(this.objectMapper.writeValueAsString(this.categoryDtoOneMock)));
    }
    
    @Test
    @DisplayName("Should create category")
    void when_createCategory_then_returnCategory() throws Exception {
        Mockito.when(this.categoryService.create(Mockito.any(CategoryDto.class)))
                .thenReturn(this.categoryDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .post(this.URL.toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.categoryDtoOneMock))
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(this.objectMapper.writeValueAsString(this.categoryDtoOneMock)));
    }

    @Test
    @DisplayName("Should update category")
    void when_updateCategory_then_returnApiResponseDto() throws Exception {
        Long categoryId = 1L;
        ApiResponseDto response = ApiResponseDataTestUtils
                .getMockRoleResponse("updated successfully", Category.class);
        Mockito.when(this.categoryService.update(Mockito.anyLong(), Mockito.any(CategoryDto.class)))
                .thenReturn(response);

        RequestBuilder request = MockMvcRequestBuilders
                .put(this.URL.append("/").append(categoryId).toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.categoryDtoOneMock))
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(this.objectMapper.writeValueAsString(response)));
    }

}