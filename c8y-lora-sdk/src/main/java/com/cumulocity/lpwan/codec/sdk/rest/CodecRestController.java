package com.cumulocity.lpwan.codec.sdk.rest;

import com.cumulocity.lpwan.codec.sdk.exception.DecoderException;
import com.cumulocity.lpwan.codec.sdk.model.DecodeRequest;
import com.cumulocity.lpwan.codec.sdk.model.DecodeResponse;
import com.cumulocity.lpwan.codec.sdk.service.CodecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CodecRestController {

    @Autowired
    private CodecService codecService;

    /**
     * This REST API should expose '/decode' endpoint
     *
     * @param decode
     * @return
     */
    @PostMapping(value = "/decode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DecodeResponse decode(@RequestBody DecodeRequest decode) throws DecoderException {
        log.info("Received decode request for the device {}", decode.getDeviceId());
        return codecService.decode(decode);
    }
}
