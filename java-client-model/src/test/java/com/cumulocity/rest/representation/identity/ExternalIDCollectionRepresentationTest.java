package com.cumulocity.rest.representation.identity;

import com.cumulocity.rest.representation.support.BDDJsonRepresentationTestBase;
import com.cumulocity.rest.representation.support.TestJsonProvider;
import lombok.Value;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ExternalIDCollectionRepresentationTest extends BDDJsonRepresentationTestBase<ExternalIDCollectionRepresentation> {

    @ParameterizedTest
    @EnumSource(TestJsonProvider.class)
    void shouldDeserializeToRepresentation(TestJsonProvider provider) {
        givenJsonString("{" +
                "   \"externalIds\":[" +
                "      {" +
                "         \"externalId\":\"132458\"," +
                "         \"type\":\"c8y_Serial\"," +
                "         \"managedObject\":{" +
                "            \"id\":\"55\"" +
                "         }" +
                "      }," +
                "      {" +
                "         \"externalId\":\"565484\"," +
                "         \"type\":\"c8y_xtDevice\"," +
                "         \"managedObject\":{" +
                "            \"id\":\"88\"" +
                "         }" +
                "      }" +
                "   ]" +
                "}");
        whenDeserializedUsing(provider);

        thenReturnedExternalIdCollection()
                .hasXtIdListOfSize(2)
                .andTheList()
                .extracting(ExternalIDRepresentation::getExternalId)
                .contains("132458", "565484");
    }

    private ExternalIdCollectionAssert thenReturnedExternalIdCollection() {
        return new ExternalIdCollectionAssert(getRepresentation());
    }

    @Value
    private static class ExternalIdCollectionAssert {
        ExternalIDCollectionRepresentation rep;

        public ExternalIdCollectionAssert hasXtIdListOfSize(int size) {
            assertThat(rep.getExternalIds()).hasSize(size);
            return this;
        }
        public ListAssert<ExternalIDRepresentation> andTheList() {
            return Assertions.assertThat(rep.getExternalIds());
        }
    }
}