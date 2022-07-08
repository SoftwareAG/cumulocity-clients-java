package com.cumulocity.rest.representation.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.svenson.JSONProperty;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

public class ManifestRepresentation extends AbstractExtensibleRepresentation {

	private Long id;
	private Long applicationId;
	private List<String> imports;
	private String contextPath;

	@JSONProperty(ignoreIfNull = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JSONProperty(ignoreIfNull = true)
	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	@JSONProperty(ignoreIfNull = true)
	public List<String> getImports() {
		return imports;
	}
	
	@JSONProperty(ignore = true)
	public List<String> getUniqueImports() {
	    if (imports == null) {
	        return null;
	    }
	    return new ArrayList(new HashSet(imports));
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}

	@JSONProperty(ignoreIfNull = true)
	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	
}
