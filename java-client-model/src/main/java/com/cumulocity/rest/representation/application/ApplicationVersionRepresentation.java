package com.cumulocity.rest.representation.application;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.JSONProperty;

import java.util.Set;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApplicationVersionRepresentation extends AbstractExtensibleRepresentation {
    // might be null when updating only tags
    private String version;
    private String binaryId;
    private Set<String> tags;

    @JSONProperty(ignoreIfNull = true)
    public String getVersion() {
        return version;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getBinaryId() {
        return binaryId;
    }

    @JSONProperty(ignoreIfNull = true)
    public Set<String> getTags() {
        return tags;
    }
}
