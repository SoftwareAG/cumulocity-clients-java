package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessTokenToUserDataMappingsRepresentation extends AbstractExtensibleRepresentation {
    private String firstNameClaimName;
    private String lastNameClaimName;
    private String emailClaimName;
    private String phoneNumberClaimName;
}
