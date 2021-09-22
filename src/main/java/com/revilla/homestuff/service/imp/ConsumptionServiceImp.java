package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Consumption;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.ConsumptionRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.ConsumptionService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.enums.MessageAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * ConsumptionService
 *
 * @author Kirenai
 */
@Slf4j
@Service
@Qualifier("consumption.service")
@RequiredArgsConstructor
public class ConsumptionServiceImp extends GeneralServiceImp<ConsumptionDto, Long, Consumption>
        implements ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final NourishmentRepository nourishmentRepository;
    private final UserRepository userRepository;

    @Override
    public JpaRepository<Consumption, Long> getRepo() {
        return this.consumptionRepository;
    }

    @Transactional
    @Override
    public ConsumptionDto create(
            Long nourishmentId,
            Long userId,
            ConsumptionDto data,
            AuthUserDetails userDetails) {
        log.info("Calling the create method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        User user = GeneralUtil.getEntityByIdOrThrow(userId, this.userRepository, User.class);
        Nourishment nourishment = GeneralUtil.getEntityByIdOrThrow(nourishmentId,
                this.nourishmentRepository, Nourishment.class);
        Consumption consumption = super.getModelMapper().map(data, super.getThirdGenericClass());
        consumption.setUser(user);
        GeneralUtil.validateAuthorizationPermissionOrThrow(consumption, userDetails,
                MessageAction.CREATE);
        AmountNourishment amountNourishment = nourishment.getAmountNourishment();
        if (Objects.nonNull(data.getUnit())) { // then, refactor
            if (Byte.toUnsignedInt(data.getUnit()) > Byte
                    .toUnsignedInt(amountNourishment.getUnit())) {
                throw new IllegalStateException("amount exceeded");
            }
            int unitUpdatedValue = (Byte.toUnsignedInt(amountNourishment.getUnit())
                    - Byte.toUnsignedInt(data.getUnit()));
            amountNourishment.setUnit((byte) unitUpdatedValue);
            if (amountNourishment.getUnit() == 0) {
                nourishment.setIsAvailable(false);
            }
        } else if (Objects.nonNull(data.getPercentage())) { // then, refactor
            if (data.getPercentage().doubleValue() > amountNourishment.getPercentage()
                    .doubleValue()) {
                throw new IllegalStateException("percentage exceeded");
            }
            double percentageUpdatedValue = (amountNourishment.getPercentage().doubleValue()
                    - data.getPercentage().doubleValue());
            amountNourishment.setPercentage(new BigDecimal(percentageUpdatedValue));
            if (amountNourishment.getPercentage().doubleValue() == 0.00) {
                nourishment.setIsAvailable(false);
            }
        }
        consumption.setNourishment(nourishment);
        Consumption consumptionSaved = this.consumptionRepository.save(consumption);
        return super.getModelMapper().map(consumptionSaved, super.getFirstGenericClass())
                .setMessage(
                        GeneralUtil.simpleNameClass(Consumption.class) + " created successfully");
    }

}
