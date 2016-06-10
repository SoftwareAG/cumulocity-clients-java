package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.model.RegisterDefinition;
import com.cumulocity.me.agent.util.Comparator;

public class RegisterDefinitionComparator implements Comparator{

    public boolean isSupported(Object object) {
        return object instanceof RegisterDefinition;
    }

    public int compare(Object first, Object second) {
        RegisterDefinition firstDefinition = (RegisterDefinition) first;
        RegisterDefinition secondDefinition = (RegisterDefinition) second;
        return compare(firstDefinition, secondDefinition);
    }

    private int compare(RegisterDefinition first, RegisterDefinition second) {
        if (first.getNumber() == second.getNumber()) {
            if (first.getStartBit() == second.getStartBit()){
                return 0;
            }
            if (first.getStartBit() < second.getStartBit()){
                return -1;
            } else {
                return 1;
            }
        }
        if (first.getNumber() < second.getNumber()){
            return -1;
        }
        return 1;
    }
}
