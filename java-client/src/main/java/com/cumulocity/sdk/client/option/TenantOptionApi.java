package com.cumulocity.sdk.client.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.buffering.Future;

import java.util.List;

/**
 * API for creating, updating and retrieving options from the platform.
 */
public interface TenantOptionApi {

    /**
     * Gets an option by id
     * Requires role ROLE_OPTION_MANAGEMENT_READ
     *
     * @param optionPK id of the option to search for
     * @return the option with the given id
     * @throws SDKException if the option is not found or if the query failed
     */
    OptionRepresentation getOption(OptionPK optionPK) throws SDKException;

    /**
     * Gets all options from the platform
     * Requires role ROLE_OPTION_MANAGEMENT_READ
     *
     * @return collection of options with paging functionality
     * @throws SDKException if the query failed
     */
    TenantOptionCollection getOptions() throws SDKException;

    /**
     * Creates or updates an option in the platform.
     * Requires role ROLE_OPTION_MANAGEMENT_ADMIN
     *
     * @param option option to be created
     * @return the created option with the generated id
     * @throws SDKException if the option could not be created
     */
    OptionRepresentation save(OptionRepresentation option) throws SDKException;

    /**
     * Creates or updates an option in the platform. Immediate response is available through the Future object.
     * In case of lost connection, buffers data in persistence provider.
     * Requires role ROLE_OPTION_MANAGEMENT_ADMIN
     *
     * @param option option to be created
     * @return the created option with the generated id
     * @throws SDKException if the option could not be created
     */
    Future saveAsync(OptionRepresentation option) throws SDKException;

    /**
     * Deletes option from the platform.
     * Requires role ROLE_OPTION_MANAGEMENT_ADMIN
     *
     * @param optionPK to be deleted
     * @throws SDKException if the measurement could not be deleted
     */
    void delete(OptionPK optionPK) throws SDKException;

    /**
     * Gets all options from the platform for the specific category
     * Requires role ROLE_OPTION_MANAGEMENT_READ
     *
     * @return collection of options
     * @throws SDKException if the query failed
     */
    List<OptionRepresentation> getAllOptionsForCategory(String category) throws SDKException;
}
