package com.cumulocity.me.rest.convert.inventory;

import static com.cumulocity.me.rest.convert.JSONArrayBuilder.aJSONArray;
import static com.cumulocity.me.rest.convert.JSONObjectBuilder.aJSONObject;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectCollectionRepresentationConverter.PROP_MANAGED_OBJECTS;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.convert.JSONObjectMatcher;
import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONArray;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectCollectionRepresentation;

public class ManagedObjectCollectionRepresentationConverterTest extends BaseManagedObjectConverterTestCase {

    ManagedObjectCollectionRepresentationConverter converter = new ManagedObjectCollectionRepresentationConverter();

    @Test
    public void shouldConvertToJson() throws Exception {
        ManagedObjectCollectionRepresentation representation = new ManagedObjectCollectionRepresentation();
        representation.setManagedObjects(getListOfManagedObjects());
        JSONObject expectedJSON = aJSONObject().withProperty(PROP_MANAGED_OBJECTS, getJsonListManagedObjects()).build();

        JSONObject outputJSON = toJson(representation);

        assertThat(JSONObjectMatcher.aJSONObjectLike(expectedJSON).matches(outputJSON)).isEqualTo(true);
    }

    @Test
    public void shouldConvertFromJson() throws Exception {
        JSONObject json = aJSONObject().withProperty(PROP_MANAGED_OBJECTS, getJsonListManagedObjects()).build();

        ManagedObjectCollectionRepresentation representation = fromJson(json);

        assertThat(representation).isNotNull();
        assertThat(representation.getManagedObjects()).isNotNull();
        assertThat(representation.getManagedObjects().size()).isEqualTo(getListOfManagedObjects().size());
    }

    private Object getJsonListManagedObjects() {
        JSONArray jsonArray = aJSONArray().withPropertyBuilder(getSampleJsonManagedObjectRepresentation())
                .withPropertyBuilder(getSampleJsonManagedObjectRepresentation()).build();
        return jsonArray;
    }

    private List getListOfManagedObjects() {
        List list = new ArrayList();
        list.add(getSampleManagedObjectRepresentation());
        list.add(getSampleManagedObjectRepresentation());
        return list;
    }

    @Override
    public BaseRepresentationConverter getManagedObjectConverter() {
        return converter;
    }

}
