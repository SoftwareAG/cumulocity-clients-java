package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.model.CoilDefinition;
import com.cumulocity.me.agent.fieldbus.model.RegisterDefinition;
import com.cumulocity.me.agent.util.Comparator;

public class CoilDefinitionComparator implements Comparator{

    public boolean isSupported(Object object) {
        return object instanceof CoilDefinition;
    }

    public int compare(Object first, Object second) {
        CoilDefinition firstDefinition = (CoilDefinition) first;
        CoilDefinition secondDefinition = (CoilDefinition) second;
        return compare(firstDefinition, secondDefinition);
    }

    private int compare(CoilDefinition first, CoilDefinition second) {
        if (first.getNumber() == second.getNumber()) {
            return 0;
        }
        if (first.getNumber() < second.getNumber()){
            return -1;
        }
        return 1;
    }
}
