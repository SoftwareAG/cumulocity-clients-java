package com.cumulocity.me.rest.representation.application;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;

public class ApplicationRepresentation extends BaseCumulocityResourceRepresentation {

    private String id;

//	@NotNull(operation = Command.CREATE)
	private String name;
	
//	@NotNull(operation = Command.CREATE)
	private String key;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
}
