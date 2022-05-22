package com.revilla.homestuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.jwt.JwtAuthenticationEntryPoint;
import com.revilla.homestuff.security.jwt.JwtTokenFilter;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.NourishmentService;
import com.revilla.homestuff.service.imp.AuthUserDetailsServiceImp;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.AmountNourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.NourishmentServiceDataTestUtils;
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

@WebMvcTest(value = NourishmentResource.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenProvider.class)
})
class NourishmentResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtTokenFilter tokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NourishmentService nourishmentService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthUserDetailsServiceImp userDetailsServiceImp;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final StringBuilder URL = new StringBuilder("/api/nourishments");
    private String token;

    private NourishmentDto nourishmentDtoOneMock;

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

        this.nourishmentDtoOneMock = NourishmentServiceDataTestUtils.getNourishmentDtoMock(
                1L,
                "orange",
                "orange.png",
                "orange",
                AmountNourishmentServiceDataTestUtils
                        .getAmountNourishmentDtoMock((byte) 20));

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
    @DisplayName("Should return nourishment list")
    void getNourishmentList() throws Exception {
        List<NourishmentDto> nourishmentDtoList = NourishmentServiceDataTestUtils.getNourishmentDtoList();
        Mockito.when(this.nourishmentService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(nourishmentDtoList);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.toString())
                .header(HttpHeaders.AUTHORIZATION, this.token);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nourishmentId").value(nourishmentDtoList.get(0).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(nourishmentDtoList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(nourishmentDtoList.get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].imagePath").value(nourishmentDtoList.get(0).getImagePath()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isAvailable").value(nourishmentDtoList.get(0).getIsAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nourishmentId").value(nourishmentDtoList.get(1).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(nourishmentDtoList.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(nourishmentDtoList.get(1).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].imagePath").value(nourishmentDtoList.get(1).getImagePath()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isAvailable").value(nourishmentDtoList.get(1).getIsAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].nourishmentId").value(nourishmentDtoList.get(2).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(nourishmentDtoList.get(2).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value(nourishmentDtoList.get(2).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].imagePath").value(nourishmentDtoList.get(2).getImagePath()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].isAvailable").value(nourishmentDtoList.get(2).getIsAvailable()));
    }

    @Test
    @DisplayName("Should return nourishment by id")
    void getNourishmentById() throws Exception {
        Long nourishmentId = 1L;
        Mockito.when(this.nourishmentService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.nourishmentDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/").append(nourishmentId).toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nourishmentId").value(this.nourishmentDtoOneMock.getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.nourishmentDtoOneMock.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(this.nourishmentDtoOneMock.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imagePath").value(this.nourishmentDtoOneMock.getImagePath()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAvailable").value(this.nourishmentDtoOneMock.getIsAvailable()));
    }

    @Test
    @DisplayName("Should return error when nourishment not found")
    void getNourishmentByIdNotFound() throws Exception {
        Long nourishmentId = 1L;
        Mockito.when(this.nourishmentService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new EntityNoSuchElementException("Nourishment not found with id: " + nourishmentId));

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/").append(nourishmentId).toString())
                .header(HttpHeaders.AUTHORIZATION, this.token);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should return list of available nourishments")
    void getAvailableNourishmentList() throws Exception {
        List<NourishmentDto> nourishmentDtoList = NourishmentServiceDataTestUtils.getNourishmentDtoList();
        Mockito.when(this.nourishmentService.findAllNourishmentByStatus(Mockito.anyBoolean()))
                .thenReturn(nourishmentDtoList);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/stock/").append(true).toString())
                .header(HttpHeaders.AUTHORIZATION, this.token);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nourishmentId").value(nourishmentDtoList.get(0).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(nourishmentDtoList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(nourishmentDtoList.get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].imagePath").value(nourishmentDtoList.get(0).getImagePath()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isAvailable").value(nourishmentDtoList.get(0).getIsAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nourishmentId").value(nourishmentDtoList.get(1).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(nourishmentDtoList.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(nourishmentDtoList.get(1).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].imagePath").value(nourishmentDtoList.get(1).getImagePath()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isAvailable").value(nourishmentDtoList.get(1).getIsAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].nourishmentId").value(nourishmentDtoList.get(2).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(nourishmentDtoList.get(2).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value(nourishmentDtoList.get(2).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].imagePath").value(nourishmentDtoList.get(2).getImagePath()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].isAvailable").value(nourishmentDtoList.get(2).getIsAvailable()));
    }

    @Test
    @DisplayName("Should create nourishment")
    void createNourishment() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Mockito.when(this.nourishmentService.create(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(NourishmentDto.class), Mockito.any()))
                .thenReturn(this.nourishmentDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .post(this.URL.append("/user/").append(userId).append("/category/").append(categoryId).toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.nourishmentDtoOneMock));

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nourishmentId").value(this.nourishmentDtoOneMock.getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.nourishmentDtoOneMock.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(this.nourishmentDtoOneMock.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imagePath").value(this.nourishmentDtoOneMock.getImagePath()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAvailable").value(this.nourishmentDtoOneMock.getIsAvailable()));
    }

    @Test
    @DisplayName("Should update nourishment")
    void updateNourishment() throws Exception {
        Long nourishmentId = 1L;
        ApiResponseDto apiResponseDto = ApiResponseDataTestUtils
                .getApiResponseMock("updated successfully", Nourishment.class);
        Mockito.when(this.nourishmentService.update(Mockito.anyLong(), Mockito.any(NourishmentDto.class), Mockito.any()))
                .thenReturn(apiResponseDto);

        RequestBuilder request = MockMvcRequestBuilders
                .put(this.URL.append("/").append(nourishmentId).toString())
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.nourishmentDtoOneMock));

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(apiResponseDto.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(apiResponseDto.getMessage()));
    }

    @Test
    @DisplayName("Should delete nourishment")
    void deleteNourishment() throws Exception {
        Long nourishmentId = 1L;
        ApiResponseDto apiResponseDto = ApiResponseDataTestUtils
                .getApiResponseMock("deleted successfully", Nourishment.class);
        Mockito.when(this.nourishmentService.delete(Mockito.anyLong(), Mockito.any()))
                .thenReturn(apiResponseDto);

        RequestBuilder request = MockMvcRequestBuilders
                .delete(this.URL.append("/").append(nourishmentId).toString())
                .header(HttpHeaders.AUTHORIZATION, this.token);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(apiResponseDto.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(apiResponseDto.getMessage()));
    }

}