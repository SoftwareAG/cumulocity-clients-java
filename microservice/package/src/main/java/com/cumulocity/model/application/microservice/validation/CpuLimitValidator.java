package com.cumulocity.model.application.microservice.validation;

import com.cumulocity.model.Cpu;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpuLimitValidator implements ConstraintValidator<MinCpu, String> {

    private Cpu minimal;

    @Override
    public void initialize(MinCpu validCpu) {
        minimal = Cpu.tryParse(validCpu.value()).get();

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final Optional<Cpu> parsed = Cpu.tryParse(value);
        return Strings.isNullOrEmpty(value) || !parsed.isPresent() || parsed.get().compareTo(minimal) >= 0;
    }
}
