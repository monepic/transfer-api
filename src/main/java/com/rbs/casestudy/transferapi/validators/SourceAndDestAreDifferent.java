package com.rbs.casestudy.transferapi.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.rbs.casestudy.transferapi.requests.TransferRequest;
import com.rbs.casestudy.transferapi.validators.SourceAndDestAreDifferent.SourceAndDestAreDifferentValidator;

/**
 * Validate that the source and destination accounts are different
 * @author ed
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SourceAndDestAreDifferentValidator.class })
public @interface SourceAndDestAreDifferent {

    String message() default "Source account and destination account must be different";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public static class SourceAndDestAreDifferentValidator 
    implements ConstraintValidator<SourceAndDestAreDifferent, TransferRequest> {

        @Override
        public boolean isValid(TransferRequest value, ConstraintValidatorContext context) {
            System.out.println("vvvvv");
            return value.getSourceAccountNumber() == null 
                    || value.getDestinationAccountNumber() == null
                    || ! value.getSourceAccountNumber().equals(value.getDestinationAccountNumber());
        }
    }
}