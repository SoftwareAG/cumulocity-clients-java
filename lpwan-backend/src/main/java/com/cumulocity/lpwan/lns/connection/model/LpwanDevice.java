package com.cumulocity.lpwan.lns.connection.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LpwanDevice {
    String name;
    String managedObjectId;
}
