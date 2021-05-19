package com.cumulocity.sdk.client.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemOptionIT extends JavaSdkITBase {

    private SystemOptionApi systemOptionApi;

    private final String SYSTEM_OPTION_CATEGORY = "system";

    private final String SYSTEM_OPTION_KEY = "version";

    private final String VERSION_REGEX = "(?!\\.)(\\d+(\\.\\d+)+)([-.][A-Z]+)?(?![\\d.])$";

    @BeforeEach
    public void setUp() {
        systemOptionApi = platform.getSystemOptionApi();
    }

    @Test
    public void test() {
        final OptionRepresentation option = systemOptionApi.getOption(new OptionPK(SYSTEM_OPTION_CATEGORY, SYSTEM_OPTION_KEY));
        assertThat(option.getCategory()).isEqualTo(SYSTEM_OPTION_CATEGORY);
        assertThat(option.getKey()).isEqualTo(SYSTEM_OPTION_KEY);
        assertThat(option.getValue().matches(VERSION_REGEX));
    }

}
