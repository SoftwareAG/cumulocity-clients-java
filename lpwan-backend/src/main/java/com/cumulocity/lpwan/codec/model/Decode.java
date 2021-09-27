package com.cumulocity.lpwan.codec.model;

import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.BaseEncoding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Decode {
	private String payload;
	private UplinkConfiguration uplinkConfiguration;
}
