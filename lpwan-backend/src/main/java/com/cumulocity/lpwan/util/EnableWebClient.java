package com.cumulocity.lpwan.util;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({LpwanConfiguration.class})
public @interface EnableWebClient {
}
