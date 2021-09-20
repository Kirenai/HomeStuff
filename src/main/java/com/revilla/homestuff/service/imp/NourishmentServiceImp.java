package com.revilla.homestuff.service.imp;

import java.util.Objects;
import com.revilla.homestuff.dto.AmountNourishementDto;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.NourishmentService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.RoleName;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * NourishmentService
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("nourishment.service")
public class NourishmentServiceImp extends GeneralServiceImp<NourishmentDto, Long, Nourishment>
        implements NourishmentService {

    private final NourishmentRepository nourishmentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
	public JpaRepository<Nourishment, Long> getRepo() {
		return this.nourishmentRepository;
    }

    @Transactional
    @Override
    public NourishmentDto create(
            Long userId,
            Long categoryId,
            NourishmentDto data) {
        log.info("Calling the create method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        GeneralUtil.validateDuplicateConstraintViolation(data.getName(),
                this.nourishmentRepository, Nourishment.class);
        Nourishment nourishment = super.getModelMapper().map(data,
                super.getThirdGenericClass());
        nourishment.setIsAvailable(true);
        User user = GeneralUtil.getEntityByIdOrThrow(userId, this.userRepository,
                User.class);
        Category category = GeneralUtil.getEntityByIdOrThrow(categoryId,
                this.categoryRepository, Category.class);
        nourishment.setUser(user);
        nourishment.setCategory(category);
        nourishment.getAmountNourishment().setNourishment(nourishment);
        Nourishment nourishmentSaved = this.nourishmentRepository.save(nourishment);
        return super.getModelMapper().map(nourishmentSaved, super.getFirstGenericClass())
                .setMessage(GeneralUtil.simpleNameClass(Nourishment.class)
                        + " created successfully");
    }

    @Transactional
	@Override
	public ApiResponseDto update(Long id, NourishmentDto data,
            AuthUserDetails userDetails) {
        log.info("Calling the update method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        return this.nourishmentRepository.findById(id)
            .map(n -> {
                if (n.getUser().getUserId().equals(userDetails.getUserId())
                            || userDetails.getAuthorities().contains(
                                    new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                    n.setName(data.getName());
                    n.setImagePath(data.getImagePath());
                    n.setDescription(data.getDescription());
                    AmountNourishementDto amountNourishmentDto = data.getAmountNourishment();
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
                    return GeneralUtil.responseMessageAction(n, Nourishment.class,
                            "updated successfully");
                }
                throw new UnauthorizedPermissionException(
                        "You don't have the permission to update this nourishment");
            })
            .orElseThrow(() -> new EntityNoSuchElementException(
                    GeneralUtil.simpleNameClass(Nourishment.class)
                            + " don't found with id: " + id)
            );
	}

}
