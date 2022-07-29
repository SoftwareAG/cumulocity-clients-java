package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRestrictionsRepresentation extends AbstractExtensibleRepresentation {
    private List<String> forbiddenClients;
    private List<String> trustedUserAgents;
    private List<String> forbiddenUserAgents;
}
