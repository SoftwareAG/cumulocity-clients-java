package c8y;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.AbstractDynamicProperties;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Agent extends AbstractDynamicProperties {
    private String name;
    private String version;
    private String url;
    private String maintainer;
}
