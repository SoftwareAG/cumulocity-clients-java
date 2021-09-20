package com.cumulocity.lpwan.codec.rest;

import com.cumulocity.lpwan.codec.model.Decode;
import com.cumulocity.lpwan.codec.model.Encode;
import com.cumulocity.lpwan.devicetype.model.DecodedDataMapping;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public abstract class BaseCodecRestController {

    final Logger logger = LoggerFactory.getLogger(BaseCodecRestController.class);

    @PostMapping(value = "/encode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public abstract String encode(@RequestBody Encode encode);

    @PostMapping(value = "/decode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public abstract DecodedDataMapping decode(@RequestBody Decode decode);
    
    @GetMapping(value = "/models", produces = MediaType.APPLICATION_JSON_VALUE)
    public abstract List<String> getModels();

    @GetMapping(value = "/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public abstract JSONObject getMetaData();
}
