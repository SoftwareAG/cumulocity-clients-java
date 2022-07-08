package com.cumulocity.rest.representation.identity;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.support.BDDJsonRepresentationTestBase;
import com.cumulocity.rest.representation.support.TestJsonProvider;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ExternalIDRepresentationTest extends BDDJsonRepresentationTestBase<ExternalIDRepresentation> {

    @ParameterizedTest()
    @EnumSource(TestJsonProvider.class)
    void shouldDeserializeToRepresentation(TestJsonProvider provider) {
        // given
        givenJsonString("{\"externalId\":\"EXT-ID2\"," +
                "\"type\":\"TYPE-2\"," +
                "\"managedObject\":{\"id\":\"321\"}}");
        // when
        whenDeserializedUsing(provider);
        // then
        thenReturnedExternalId()
                .hasExternalId("EXT-ID2")
                .hasType("TYPE-2")
                .hasManagedObjectWithId(GId.asGId(321L));
    }

    private ExternalIdRepresentationAssert thenReturnedExternalId() {
        return new ExternalIdRepresentationAssert(getRepresentation());
    }

    @Value
    private static class ExternalIdRepresentationAssert {
        ExternalIDRepresentation rep;

        public ExternalIdRepresentationAssert hasExternalId(String externalId) {
            assertThat(rep.getExternalId()).isEqualTo(externalId);
            return this;
        }
        public ExternalIdRepresentationAssert hasType(String type) {
            assertThat(rep.getType()).isEqualTo(type);
            return this;
        }
        public ExternalIdRepresentationAssert hasManagedObjectWithId(GId id) {
            assertThat(rep.getManagedObject()).isNotNull();
            assertThat(rep.getManagedObject().getId()).isEqualTo(id);
            return this;
        }
    }
}