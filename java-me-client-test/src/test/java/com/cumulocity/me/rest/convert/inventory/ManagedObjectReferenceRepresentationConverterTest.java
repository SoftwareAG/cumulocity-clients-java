package com.cumulocity.me.rest.convert.inventory;

import static com.cumulocity.me.rest.convert.JSONObjectBuilder.aJSONObject;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectReferenceRepresentationConverter.PROP_MANAGED_OBJECT;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.me.rest.convert.JSONObjectMatcher;
import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;

public class ManagedObjectReferenceRepresentationConverterTest extends BaseManagedObjectConverterTestCase {

    ManagedObjectReferenceRepresentationConverter converter = new ManagedObjectReferenceRepresentationConverter();
    
    @Override
    public BaseRepresentationConverter getManagedObjectConverter() {
        return converter;
    }
    
    @Test
    public void shouldConvertToJson() throws Exception {
        ManagedObjectReferenceRepresentation representation = getSampleManagedObjectReferenceRepresentation();
        JSONObject expectedJSON = aJSONObject().withPropertyBuilder(PROP_MANAGED_OBJECT, getSampleJsonManagedObjectRepresentation()).build();
        
        JSONObject outputJSON = toJson(representation);
        
        assertThat(JSONObjectMatcher.aJSONObjectLike(expectedJSON).matches(outputJSON)).isEqualTo(true);
    }
    
    @Test
    public void shouldConvertFromJson() throws Exception {
        JSONObject json = aJSONObject().withProperty(PROP_MANAGED_OBJECT, getSampleJsonManagedObjectRepresentation().build()).build();
        
        ManagedObjectReferenceRepresentation representation = fromJson(json);

        assertThat(representation.getManagedObject()).isNotNull();
    }

}
