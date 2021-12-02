/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.annotation;

import com.cumulocity.lpwan.codec.CodecMicroservice;
import com.cumulocity.microservice.autoconfigure.MicroserviceApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used in the Main class to make the Codec microservice spring boot enabled.
 * And also it helps in inheriting the Rest Controller and Service class.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MicroserviceApplication
@Import(CodecMicroservice.class)
public @interface CodecMicroserviceApplication {
}
