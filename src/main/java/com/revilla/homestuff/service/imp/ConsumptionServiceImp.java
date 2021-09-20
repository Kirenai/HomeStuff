package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Consumption;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.ConsumptionRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.ConsumptionService;
import com.revilla.homestuff.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

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

    // TODO: only the authenticated user can create their own consumptions,
    // then implement
    @Transactional
    @Override
    public ConsumptionDto create(
            Long nourishmentId,
            Long userId,
            ConsumptionDto data) {
        log.info("Calling the create method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        Consumption consumption = super.getModelMapper().map(data, super.getThirdGenericClass());
        Nourishment nourishment = GeneralUtil.getEntityByIdOrThrow(nourishmentId,
                this.nourishmentRepository, Nourishment.class);
        User user = GeneralUtil.getEntityByIdOrThrow(userId, this.userRepository,
                User.class);
        AmountNourishment amountNourishment = nourishment.getAmountNourishment();
        if (data.getUnit() != null) { //then, refactor
            if (Byte.toUnsignedInt(data.getUnit()) > Byte.toUnsignedInt(amountNourishment.getUnit())) {
                throw new IllegalStateException("amount exceeded");
            }
            int unitUpdatedValue = (Byte.toUnsignedInt(amountNourishment.getUnit()) - Byte.toUnsignedInt(data.getUnit()));
            amountNourishment.setUnit((byte) unitUpdatedValue);
            if (amountNourishment.getUnit() == 0) {
                nourishment.setIsAvailable(false);
            }
        } else if (data.getPercentage() != null) { //then, refactor
            if (data.getPercentage().doubleValue() > amountNourishment.getPercentage().doubleValue()) {
                throw new IllegalStateException("percentage exceeded");
            }
            double percentageUpdatedValue = (amountNourishment.getPercentage().doubleValue() - data.getPercentage().doubleValue());
            amountNourishment.setPercentage(new BigDecimal(percentageUpdatedValue));
            if (amountNourishment.getPercentage().doubleValue() == 0.00) {
                nourishment.setIsAvailable(false);
            }
        }
        consumption.setNourishment(nourishment);
        consumption.setUser(user);
        Consumption consumptionSaved = this.consumptionRepository.save(consumption);
        return super.getModelMapper().map(consumptionSaved, super.getFirstGenericClass());
    }

    @Override
    public ConsumptionDto update(Long id, ConsumptionDto data) {
        log.info("Calling the update method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        return this.consumptionRepository.findById(id)
                .map(c -> {
                    c.setUnit(data.getUnit());
                    c.setPercentage(data.getPercentage());
                    return super.getModelMapper().map(
                            this.consumptionRepository.save(c),
                            super.getFirstGenericClass()
                    );
                })
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(Consumption.class)
                                + " don't found with id: " + id)
                );
    }


}
