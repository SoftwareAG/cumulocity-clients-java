package com.cumulocity.model.option;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OptionPK implements Serializable {

	public static final String CONFIGURATION_CATEGORY = "configuration";

	private String key;

	private String category;

	public OptionPK() {}

	public OptionPK(OptionPK source) {
		this(source.getCategory(), source.getKey());
	}

	public OptionPK(String category, String key) {
		this.key = key;
		this.category = category;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OptionPK other = (OptionPK) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name("/");
	}

	public String propertyName() {
		return name(".");
	}

	public String name(String separator) {
		return category + separator + key;
	}
}
