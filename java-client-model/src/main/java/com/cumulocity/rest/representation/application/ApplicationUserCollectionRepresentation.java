package com.cumulocity.rest.representation.application;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import lombok.*;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@Builder(builderMethodName = "applicationUserCollectionRepresentation")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApplicationUserCollectionRepresentation extends BaseCollectionRepresentation<ApplicationUserRepresentation> {

    @Singular
    private List<ApplicationUserRepresentation> users = new ArrayList<>();

    @Override
    @JSONProperty(ignore = true)
    public Iterator<ApplicationUserRepresentation> iterator() {
        return users.iterator();
    }

    @JSONTypeHint(ApplicationUserRepresentation.class)
    public List<ApplicationUserRepresentation> getUsers() {
        return users;
    }
}
