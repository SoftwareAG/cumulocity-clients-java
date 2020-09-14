package com.cumulocity.sdk.client.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;

/**
 * API for retrieving system options from the platform.
 */
public interface SystemOptionApi {
    /**
     * Gets an option by id
     * Requires role ROLE_OPTION_MANAGEMENT_READ
     *
     * @param optionPK id of the option to search for
     * @return the system option with the given id
     * @throws SDKException if the option is not found or if the query failed
     */
    OptionRepresentation getOption(OptionPK optionPK) throws SDKException;

}
