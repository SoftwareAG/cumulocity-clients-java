package com.cumulocity.lpwan.tenant.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;


public interface TenantOptionRepository {

    String getAndDecryptOptionValue(OptionPK optionPK) throws DecryptFailedException, TenantOptionNotFoundException;

    void encryptAndSetOption(OptionRepresentation option);

    String getOptionValue(OptionPK optionPK) throws TenantOptionNotFoundException;

    void setOption(OptionRepresentation option);
}
