package c8y;

import lombok.Data;

@Data
public class LpwanDevice  {

    private boolean provisioned = false;
    private String type;
    private String errorMessage;
    private String serviceProvider;
    private String lpwanDeviceType;

}
