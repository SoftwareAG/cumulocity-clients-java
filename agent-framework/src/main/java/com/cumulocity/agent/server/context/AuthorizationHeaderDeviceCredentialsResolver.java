package com.cumulocity.agent.server.context;

import static com.cumulocity.sdk.client.PagedCollectionResource.PAGE_SIZE_KEY;
import static com.cumulocity.sdk.client.RestConnector.X_CUMULOCITY_APPLICATION_KEY;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.FluentIterable.from;
import static java.lang.Integer.parseInt;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class AuthorizationHeaderDeviceCredentialsResolver implements DeviceCredentailsResolver<ContainerRequestContext> {

    private static final Integer DEFAULT_PAGE_SIZE = 5;

    private static final Function<String, Integer> toInt = new Function<String, Integer>() {
        @Override
        public Integer apply(String s) {
            return parseInt(s);
        }
    };

    @Override
    public DeviceCredentials get(ContainerRequestContext request) {
        String authorization = request.getHeaderString(AUTHORIZATION);
        String applicationKey = request.getHeaderString(X_CUMULOCITY_APPLICATION_KEY);
        final List<String> pageSizeParam = Optional.fromNullable(request.getUriInfo().getQueryParameters().get(PAGE_SIZE_KEY)).or(
                ImmutableList.<String> of());
        int pageSize = from(pageSizeParam).first().transform(toInt).or(DEFAULT_PAGE_SIZE);
        return DeviceCredentials.from(authorization, applicationKey, pageSize);
    }

    @Override
    public boolean supports(Object credentialSource) {
        return credentialSource instanceof ContainerRequestContext
                && !isNullOrEmpty(((ContainerRequestContext) credentialSource).getHeaderString(AUTHORIZATION));
    }
}
