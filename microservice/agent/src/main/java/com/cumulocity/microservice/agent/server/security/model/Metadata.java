package com.cumulocity.microservice.agent.server.security.model;

import lombok.*;

import java.util.List;

/**
 * Dont use MicroserviceMetadataRepresentation because of issue:
 * Unrecognized field "selfDecoded" (Class com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation), not marked as ignorable
 */
@Data
@Builder(builderMethodName = "metadata")
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    @Singular

    private List<String> roles;

}