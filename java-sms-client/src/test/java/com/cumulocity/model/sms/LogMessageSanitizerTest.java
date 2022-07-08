package com.cumulocity.model.sms;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LogMessageSanitizerTest {
    private LogMessageSanitizer anonymizer;

    @ParameterizedTest
    @CsvSource({
            "5,+2332,+2332",
            "2,+2332,+2*32",
            "2,+23432432,+2*****32",
            "2,+234ad32432,+2*******32"
    })
    void shouldAnonymizeMiddlePart(int leftoversLength, String sample, String expectedResult) {
        anonymizer = new LogMessageSanitizer(leftoversLength);
        Assertions.assertThat(anonymizer.sanitize(sample)).isEqualTo(expectedResult);
    }

}
