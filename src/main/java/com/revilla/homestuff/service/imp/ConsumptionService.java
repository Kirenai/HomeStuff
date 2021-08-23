package com.revilla.homestuff.service.imp;

import java.util.List;
import java.util.stream.Collectors;
import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.Consumption;
import com.revilla.homestuff.repository.ConsumptionRepository;
import com.revilla.homestuff.service.GeneralService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
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
public class ConsumptionService implements GeneralService<ConsumptionDto, Long> {

    private final ConsumptionRepository consumptionRepository;
    private final ModelMapper modelmapper;

	@Override
	public List<ConsumptionDto> findAll(Pageable pageable) {
        log.info("Calling the findAll methond in ConsumptionService");
        return this.consumptionRepository.findAll(pageable)
            .getContent()
            .stream()
            .map(c -> this.modelmapper.map(c, ConsumptionDto.class))
            .collect(Collectors.toList());
	}

	@Override
	public ConsumptionDto findOne(Long id) {
        log.info("Calling the findOne methond in ConsumptionService");
        Consumption consumption = this.consumptionRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Consumption don't found"));
        return this.modelmapper.map(consumption, ConsumptionDto.class);
	}

	@Override
	public ConsumptionDto create(ConsumptionDto data) {
        log.info("Calling the create methond in ConsumptionService");
        Consumption consumptionData = this.modelmapper.map(data, Consumption.class);
        Consumption consumptionSaved = this.consumptionRepository.save(consumptionData);
        return this.modelmapper.map(consumptionSaved, ConsumptionDto.class);
	}

	@Override
	public ConsumptionDto update(Long id, ConsumptionDto data) {
        log.info("Calling the update methond in ConsumptionService");
        return this.consumptionRepository.findById(id)
            .map(c -> {
                c.setUnit(data.getUnit());
                c.setPercentage(data.getPercentage());
                return this.modelmapper.map(this.consumptionRepository.save(c), ConsumptionDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("Consumption don't found"));
    }

	@Override
	public ConsumptionDto delete(Long id) {
        log.info("Calling the delete methond in ConsumptionService");
        return this.consumptionRepository.findById(id)
            .map(c -> {
                this.consumptionRepository.delete(c);
                return this.modelmapper.map(c, ConsumptionDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("Consumption don't found"));
	}

}
