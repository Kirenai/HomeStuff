package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.AmountNourishmentDto;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.NourishmentService;
import com.revilla.homestuff.util.ConstraintViolation;
import com.revilla.homestuff.util.Entity;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.MessageAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * NourishmentService
 *
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NourishmentServiceImp extends GeneralServiceImp<NourishmentDto, Long, Nourishment>
        implements NourishmentService {

    private final NourishmentRepository nourishmentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public JpaRepository<Nourishment, Long> getRepo() {
        return this.nourishmentRepository;
    }

    @Override
    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @Override
    public List<NourishmentDto> findAllNourishmentByStatus(boolean isAvailable) {
        log.info("Invoking NourishmentServiceImp.findAllNourishmentByStatus method");
        return this.nourishmentRepository.findByIsAvailable(isAvailable)
                .stream()
                .map(nourishment -> this.getModelMapper().map(nourishment, super.getFirstGenericClass()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public NourishmentDto create(Long userId, Long categoryId, NourishmentDto data,
                                 AuthUserDetails userDetails) {
        log.info("Invoking NourishmentServiceImp.create method");
        ConstraintViolation.validateDuplicate(data.getName(), this.nourishmentRepository,
                Nourishment.class);
        Nourishment nourishment = this.getModelMapper().map(data, super.getThirdGenericClass());
        User user = Entity.getById(userId, this.userRepository, User.class);
        nourishment.setUser(user);
        GeneralUtil.validateAuthorizationPermissionOrThrow(nourishment, userDetails,
                MessageAction.CREATE);
        Category category = Entity.getById(categoryId, this.categoryRepository,
                Category.class);
        nourishment.setIsAvailable(true);
        nourishment.setCategory(category);
        nourishment.getAmountNourishment().setNourishment(nourishment);
        Nourishment nourishmentSaved = this.nourishmentRepository.save(nourishment);
        return this.getModelMapper().map(nourishmentSaved, super.getFirstGenericClass())
                .setMessage(
                        GeneralUtil.simpleNameClass(Nourishment.class) + " created successfully");
    }

    @Transactional
    @Override
    public ApiResponseDto update(Long id, NourishmentDto data, AuthUserDetails userDetails) {
        log.info("Invoking NourishmentServiceImp.update method");
        return this.nourishmentRepository.findById(id)
                .map(n -> this.mapOutApiResponseDto(data, n))
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(Nourishment.class) + " not found with id: "
                                + id));
    }

    private ApiResponseDto mapOutApiResponseDto(NourishmentDto data, Nourishment n) {
        n.setName(data.getName());
        n.setImageUrl(data.getImageUrl());
        n.setDescription(data.getDescription());
        AmountNourishmentDto amountNourishmentDto = data.getAmountNourishment();
        AmountNourishment amountNourishment = n.getAmountNourishment();
        if (Objects.nonNull(amountNourishmentDto)
                && Objects.nonNull(amountNourishmentDto.getUnit())
                && Objects.nonNull(amountNourishment.getUnit())) {
            amountNourishment.setUnit(amountNourishmentDto.getUnit());
        }
        if (Objects.nonNull(amountNourishmentDto)
                && Objects.nonNull(amountNourishmentDto.getPercentage())
                && Objects.nonNull(amountNourishment.getPercentage())) {
            amountNourishment.setPercentage(amountNourishmentDto.getPercentage());
        }
        return GeneralUtil.responseMessageAction(Nourishment.class,
                "updated successfully");
    }

}
