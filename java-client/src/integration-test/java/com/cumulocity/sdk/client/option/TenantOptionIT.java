package com.cumulocity.sdk.client.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.cumulocity.rest.representation.tenant.OptionRepresentation.asOptionRepresetation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class TenantOptionIT extends JavaSdkITBase {

    private static final String CATEGORY = "test.category";
    private static final String KEY = "test.key";

    private TenantOptionApi tenantOptionApi;

    @Before
    public void setup() {
        tenantOptionApi = platform.getTenantOptionApi();
    }

    @After
    public void cleanup(){
        for(OptionRepresentation option: tenantOptionApi.getAllOptionsForCategory(CATEGORY)){
            tenantOptionApi.delete(new OptionPK(option.getCategory(), option.getKey()));
        }
    }

    @Test
    public void shouldCreateOption() {
        OptionRepresentation expectedOption = asOptionRepresetation(CATEGORY, KEY, "value");
        OptionRepresentation option = tenantOptionApi.save(expectedOption);
        OptionRepresentation savedOption = tenantOptionApi.getOption(new OptionPK(CATEGORY, KEY));
        assertThat(option, equalTo(expectedOption));
        assertThat(savedOption, equalTo(expectedOption));
    }

    @Test
    public void shouldUpdateOption() {
        OptionRepresentation expectedOption = asOptionRepresetation(CATEGORY, KEY, "value");
        OptionRepresentation option = tenantOptionApi.save(expectedOption);
        OptionRepresentation savedOption = tenantOptionApi.getOption(new OptionPK(CATEGORY, KEY));
        assertThat(option, equalTo(expectedOption));
        assertThat(savedOption, equalTo(expectedOption));

        expectedOption = asOptionRepresetation(CATEGORY, KEY, "value3");
        option = tenantOptionApi.save(expectedOption);
        savedOption = tenantOptionApi.getOption(new OptionPK(CATEGORY, KEY));
        assertThat(option, equalTo(expectedOption));
        assertThat(savedOption, equalTo(expectedOption));
    }

    @Test
    public void shouldGetAllOptions() {
        List<OptionRepresentation> expectedOptions = sampleOptions(CATEGORY, KEY, 10);
        saveOptions(expectedOptions);
        TenantOptionCollection options = tenantOptionApi.getOptions();
        PagedTenantOptionCollectionRepresentation optionCollection = options.get(1000);

        assertExpectedOptionsFound(optionCollection.allPages().iterator(), expectedOptions);
    }


    @Test
    public void shouldGetOptionsForSingleCategory() {
        int expectedCount = 5;
        List<OptionRepresentation> expectedOptions = sampleOptions(CATEGORY, KEY, expectedCount);
        saveOptions(expectedOptions);
        List<OptionRepresentation> options = tenantOptionApi.getAllOptionsForCategory(CATEGORY);

        assertThat(options.size(), equalTo(expectedCount));
        assertExpectedOptionsFound(options.iterator(), expectedOptions);
    }

    @Test
    public void shouldDeleteOption() {
        OptionPK optionPK = new OptionPK(CATEGORY, KEY);

        tenantOptionApi.save(asOptionRepresetation(CATEGORY, KEY, "value"));
        OptionRepresentation savedOption = tenantOptionApi.getOption(optionPK);
        assertThat(savedOption, notNullValue());

        tenantOptionApi.delete(new OptionPK(CATEGORY, KEY));
        try {
            tenantOptionApi.getOption(optionPK);
        } catch (SDKException e) {
            assertThat(e.getHttpStatus(), equalTo(404));
            return;
        }
        assertThat(true, is(false));
    }

    private List<OptionRepresentation> sampleOptions(String category, String keyPrefix, int count) {
        List<OptionRepresentation> options = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            options.add(asOptionRepresetation(category, keyPrefix + ".v" + i, "" + i));
        }
        return options;
    }

    private void saveOptions(List<OptionRepresentation> options) {
        for (OptionRepresentation option : options) {
            tenantOptionApi.save(option);
        }
    }

    private void assertExpectedOptionsFound(Iterator<OptionRepresentation> iterator, List<OptionRepresentation> expectedOptions) {
        int expectedCount = expectedOptions.size();
        while (iterator.hasNext()) {
            OptionRepresentation option = iterator.next();
            for (OptionRepresentation expectedOption : expectedOptions) {
                if (expectedOption.equals(option)) {
                    expectedCount--;
                    if (expectedCount == 0) {
                        break;
                    }
                }
            }
        }
        assertThat(expectedCount, equalTo(0));
    }

}
