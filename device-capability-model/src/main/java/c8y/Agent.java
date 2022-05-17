package c8y;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.AbstractDynamicProperties;

/**
 * Allows device integrators to provide information about the agent running on the device.
 */
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
