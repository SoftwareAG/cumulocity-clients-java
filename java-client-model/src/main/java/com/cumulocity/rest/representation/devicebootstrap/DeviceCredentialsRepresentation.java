package com.cumulocity.rest.representation.devicebootstrap;

import com.cumulocity.rest.representation.CustomPropertiesMapRepresentation;
import lombok.Getter;
import org.svenson.JSONProperty;

public class DeviceCredentialsRepresentation extends CustomPropertiesMapRepresentation {
	
	private String id;

	private String tenantId;
	
	private String username;
	
	private String password;

	@Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
	private String securityToken;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

}
