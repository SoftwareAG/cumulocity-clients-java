/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.tenant.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;


public interface TenantOptionRepository {

    String getAndDecryptOptionValue(OptionPK optionPK) throws DecryptFailedException, TenantOptionNotFoundException;

    void encryptAndSetOption(OptionRepresentation option);

    String getOptionValue(OptionPK optionPK) throws TenantOptionNotFoundException;

    void setOption(OptionRepresentation option);

    void removeTenantOption(OptionPK optionPK);
}
