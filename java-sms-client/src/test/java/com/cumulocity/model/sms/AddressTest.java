package com.cumulocity.model.sms;

import org.junit.jupiter.api.Test;

import static com.cumulocity.model.sms.Address.phoneNumber;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressTest {
    @Test
    public void shouldNormalize() {
        assertThat(nonNormalizedPhoneNumber().normalize().getNumber()).isEqualTo("+9876543210");
    }

    @Test
    public void shouldEncodeToUseInUrl() {
        assertThat(nonNormalizedPhoneNumber().asUrlParameter()).isEqualTo("tel:qweqwe%2B9%218%407%236%245%254%5E3%262*1%2F0%28");
    }

    private Address nonNormalizedPhoneNumber() {
        return phoneNumber("qweqwe+9!8@7#6$5%4^3&2*1/0(");
    }
}
