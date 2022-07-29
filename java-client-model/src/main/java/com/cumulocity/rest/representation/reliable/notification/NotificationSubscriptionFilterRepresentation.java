package com.cumulocity.rest.representation.reliable.notification;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.svenson.JSONProperty;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSubscriptionFilterRepresentation extends AbstractExtensibleRepresentation {

    private List<String> apis;
    private String typeFilter;

    @JSONProperty(ignoreIfNull = true)
    public List<String> getApis() {
        return apis;
    }

    public void setApis(List<String> apis) {
        this.apis = apis;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getTypeFilter() {
        return typeFilter;
    }

    public void setTypeFilter(String typeFilter) {
        this.typeFilter = typeFilter;
    }

}
