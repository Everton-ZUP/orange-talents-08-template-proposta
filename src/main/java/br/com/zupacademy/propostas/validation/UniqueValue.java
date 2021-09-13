package br.com.zupacademy.propostas.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {UniqueValueValidator.class})
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValue {
    String message() default "DOCUMENTO DUPLICADO";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    Class<?> classe();
    String campo();
}
