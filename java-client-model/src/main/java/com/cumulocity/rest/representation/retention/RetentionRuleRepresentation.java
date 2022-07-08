package com.cumulocity.rest.representation.retention;

import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.annotation.Null;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

public class RetentionRuleRepresentation extends AbstractExtensibleRepresentation {

    @Null(operation = {Command.CREATE})
    private GId id;

    private String dataType;

    private String fragmentType;

    private String type;

    private String source;

    @NotNull(operation = Command.CREATE)
    private Long maximumAge;

    private Boolean editable;

    @JSONProperty(ignoreIfNull =  true)
    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    @JSONConverter(type = IDTypeConverter.class)
    public GId getId() {
        return id;
    }

    public void setId(GId id) { this.id = id; }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(String fragmentType) {
        this.fragmentType = fragmentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(Long maximumAge) {
        this.maximumAge = maximumAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RetentionRuleRepresentation that = (RetentionRuleRepresentation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null) return false;
        if (fragmentType != null ? !fragmentType.equals(that.fragmentType) : that.fragmentType != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (maximumAge != null ? !maximumAge.equals(that.maximumAge) : that.maximumAge != null) return false;
        return editable != null ? editable.equals(that.editable) : that.editable == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (fragmentType != null ? fragmentType.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (maximumAge != null ? maximumAge.hashCode() : 0);
        result = 31 * result + (editable != null ? editable.hashCode() : 0);
        return result;
    }
}
