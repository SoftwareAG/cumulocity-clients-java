package com.cumulocity.lpwan.codec.sdk;

import com.cumulocity.lpwan.codec.sdk.rest.CodecRestController;
import com.cumulocity.lpwan.codec.sdk.service.CodecService;
import com.cumulocity.microservice.autoconfigure.MicroserviceApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MicroserviceApplication
@Import({CodecRestController.class, CodecService.class})
public @interface CodecMicroserviceApplication {
}
