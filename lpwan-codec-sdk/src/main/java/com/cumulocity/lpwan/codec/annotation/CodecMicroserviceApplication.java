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
 * The <b>CodecMicroserviceApplication</b> annotation should be used in the class having the <b>Main</b> method of the Codec microservice to make it Spring Boot enabled.
 * And also it helps in inheriting the Spring beans.
 *
 * @author Bhaskar Reddy Byreddy
 * @author  Atul Kumar Panda
 * @version 1.0
 * @since   2021-12-01
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MicroserviceApplication
@Import(CodecMicroservice.class)
public @interface CodecMicroserviceApplication {
}
