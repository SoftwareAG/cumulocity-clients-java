package com.cumulocity.lpwan.codec.service;

import com.cumulocity.lpwan.codec.model.Decode;
import com.cumulocity.lpwan.codec.model.Encode;
import com.cumulocity.lpwan.devicetype.model.DecodedDataMapping;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BaseDeviceCodecService {

    DecodedDataMapping decode(Decode decode);

    String encode(Encode encode);

    List<String> getModels();

    JSONObject getMetData();
}
