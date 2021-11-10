package com.cumulocity.lpwan.codec.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecodeRequest {
	@NonNull
	private String payload;
	@NotNull
	private String deviceId;
}