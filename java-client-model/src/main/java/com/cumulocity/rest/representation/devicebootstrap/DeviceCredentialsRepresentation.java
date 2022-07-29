package com.cumulocity.rest.representation.devicebootstrap;

import static com.cumulocity.rest.representation.annotation.Command.CREATE;
import static com.cumulocity.rest.representation.annotation.Command.UPDATE;

import com.cumulocity.rest.representation.CustomPropertiesMapRepresentation;
import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.annotation.Null;
import lombok.Getter;
import org.svenson.JSONProperty;

public class DeviceCredentialsRepresentation extends CustomPropertiesMapRepresentation {
	
    @NotNull(operation = {CREATE})
	private String id;

    @Null(operation = {CREATE})
	private String tenantId;
	
    @Null(operation = {CREATE})
	private String username;
	
    @Null(operation = {CREATE})
	private String password;

	@Null(operation = {UPDATE})
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
