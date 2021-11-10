/*
~ Copyright (c) 2012-2021 Cumulocity GmbH
~ Copyright (c) 2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA,
~ and/or its subsidiaries and/or its affiliates and/or their licensors.
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~     http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" BASIS,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
*/

package com.cumulocity.lpwan.codec.annotation;

import com.cumulocity.lpwan.codec.rest.CodecRestController;
import com.cumulocity.lpwan.codec.service.CodecService;
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
@Import({CodecRestController.class, CodecService.class})
public @interface CodecMicroserviceApplication {
}
