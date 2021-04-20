package com.cumulocity.sdk.client.inventory;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.apache.commons.codec.Resources;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class BinariesApiIT extends JavaSdkITBase {

    final BinariesApi binariesApi = platform.getBinariesApi();

    //smoke test
    @Test
    public void shouldUploadFile() {
        // given
        ManagedObjectRepresentation container = new ManagedObjectRepresentation();
        container.setName("java-sdk_sample_binary");
        container.setType("sag_Binary");
        container.set("iot.cumulocity.com", "domain");

        InputStream file = Resources.getInputStream("cep/test-module.epl");
        // when
        ManagedObjectRepresentation uploaded = binariesApi.uploadFile(container, file);
        // then
        assertThat(uploaded).isNotNull();
        assertThat(uploaded.get("contentType")).isEqualTo(MediaType.APPLICATION_OCTET_STREAM);
    }

}
