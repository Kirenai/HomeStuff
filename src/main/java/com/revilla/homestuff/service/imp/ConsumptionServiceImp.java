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
import com.revilla.homestuff.util.Entity;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.consumption.CheckingValueAmountAndProcessing;
import com.revilla.homestuff.util.enums.MessageAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
    private final ModelMapper modelMapper;

    @Override
    public JpaRepository<Consumption, Long> getRepo() {
        return this.consumptionRepository;
    }

    @Override
    public ModelMapper getModelMapper() {
        return this.modelMapper;
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
        User user = Entity.getById(userId, this.userRepository, User.class);
        Nourishment nourishment = Entity.getById(nourishmentId,
                this.nourishmentRepository, Nourishment.class);
        Consumption consumption = this.getModelMapper().map(data, super.getThirdGenericClass());
        consumption.setUser(user);
        GeneralUtil.validateAuthorizationPermissionOrThrow(consumption, userDetails,
                MessageAction.CREATE);
        AmountNourishment amountNourishment = nourishment.getAmountNourishment();
        CheckingValueAmountAndProcessing.process(data, amountNourishment, nourishment);
        consumption.setNourishment(nourishment);
        Consumption consumptionSaved = this.consumptionRepository.save(consumption);
        return this.getModelMapper().map(consumptionSaved, super.getFirstGenericClass())
                .setMessage(
                        GeneralUtil.simpleNameClass(Consumption.class) + " created successfully");
    }

}
