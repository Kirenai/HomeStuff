package com.revilla.homestuff.service.imp;

import javax.transaction.Transactional;
import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.Consumption;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.ConsumptionRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.ConsumptionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ConsumptionService
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

    @Transactional
    @Override
    public ConsumptionDto create(
            Long nourishmentId,
            Long userId,
            ConsumptionDto data) {
        log.info("Calling the create methond in " + getClass());
        Consumption consumption = this.modelMapper.map(data, this.getThirdGenericClass());
        Nourishment nourishment = this.nourishmentRepository.findById(nourishmentId).orElseThrow();
        User user = this.userRepository.findById(userId).orElseThrow();
        consumption.setNourishment(nourishment);
        Consumption consumptionSaved = this.consumptionRepository.save(consumption);
        user.getConsumptions().add(consumption);
        this.userRepository.save(user);
        return this.modelMapper.map(consumptionSaved, this.getFirstGenericClass());
    }

    @Override
    public ConsumptionDto update(Long id, ConsumptionDto data) {
        log.info("Calling the update methond in " + getClass());
        return this.consumptionRepository.findById(id)
            .map(c -> {
                c.setUnit(data.getUnit());
                c.setPercentage(data.getPercentage());
                return this.modelMapper.map(this.consumptionRepository.save(c), this.getFirstGenericClass());
            })
            .orElseThrow(() -> new IllegalStateException("Consumption don't found"));
    }


}
