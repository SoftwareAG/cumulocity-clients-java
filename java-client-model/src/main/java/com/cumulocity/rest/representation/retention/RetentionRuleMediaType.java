package com.cumulocity.rest.representation.retention;

import com.cumulocity.rest.representation.CumulocityMediaType;

public class RetentionRuleMediaType extends CumulocityMediaType {

    public static final RetentionRuleMediaType RETENTION_RULE = new RetentionRuleMediaType("retentionRule");

    public static final String RETENTION_RULE_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "retentionRule+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final RetentionRuleMediaType RETENTION_RULE_COLLECTION = new RetentionRuleMediaType("retentionRuleCollection");

    public static final String RETENTION_RULE_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "retentionRuleCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final RetentionRuleMediaType RETENTION_RULE_APIS = new RetentionRuleMediaType("retentionRuleApis");

    public static final String RETENTION_RULE_APIS_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "retentionRuleApis+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public RetentionRuleMediaType(String string) {
        super(string);
    }
}
