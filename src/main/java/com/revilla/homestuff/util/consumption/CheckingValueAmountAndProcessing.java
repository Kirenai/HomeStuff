package com.revilla.homestuff.util.consumption;

import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.AmountNourishment;
import com.revilla.homestuff.entity.Nourishment;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class CheckingValueAmountAndProcessing {

    public static void process(ConsumptionDto data,
                               AmountNourishment amountNourishment,
                               Nourishment nourishment) {
        log.info("Invoking CheckingValueAmountAndProcessing.process method");
        if (Objects.nonNull(data.getUnit())) {
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
        } else if (Objects.nonNull(data.getPercentage())) {
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
    }

}
