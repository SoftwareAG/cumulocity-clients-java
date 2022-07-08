package com.cumulocity.rest.representation.connector;

import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.idtype.GId;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorFilterRepresentation {
    
    private List<String> apis;
    private List<String> fragmentsToCopy;
    private List<String> fragmentsToFilter;
    private String typeFilter;
    private GId sourceId;
    private GId deviceGroupId;

    @JSONProperty(ignoreIfNull = true)
    public List<String> getApis() {
        return apis;
    }

    public void setApis(List<String> apis) {
        this.apis = apis;
    }
    
    @JSONProperty(ignoreIfNull = true)
    public List<String> getFragmentsToCopy() {
        return fragmentsToCopy;
    }

    public void setFragmentsToCopy(List<String> fragmentsToCopy) {
        this.fragmentsToCopy = fragmentsToCopy;
    }

    @JSONProperty(ignoreIfNull = true)
    public List<String> getFragmentsToFilter() {
        return fragmentsToFilter;
    }

    public void setFragmentsToFilter(List<String> fragmentsToFilter) {
        this.fragmentsToFilter = fragmentsToFilter;
    }
    
    @JSONProperty(ignoreIfNull = true)
    public String getTypeFilter() {
        return typeFilter;
    }

    public void setTypeFilter(String typeFilter) {
        this.typeFilter = typeFilter;
    }

    @JSONConverter(type = IDTypeConverter.class)
    @JSONProperty(ignoreIfNull = true)
    public GId getSourceId() {
        return sourceId;
    }

    public void setSourceId(GId sourceId) {
        this.sourceId = sourceId;
    }

    @JSONConverter(type = IDTypeConverter.class)
    @JSONProperty(ignoreIfNull = true)
    public GId getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(GId deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }
}
