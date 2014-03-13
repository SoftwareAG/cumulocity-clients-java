package com.cumulocity.sdk.client.cep;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.google.common.io.Resources;

public class CepApiIT  extends JavaSdkITBase{

    
    final CepApi cepApi = platform.getCepApi();
    
    @Test
    public void should() throws IOException {
        
        final CepModuleRepresentation cepModule = cepApi.create(Resources.newReaderSupplier(Resources.getResource("cep/test-module.epl"), Charset.forName("UTF-8")).getInput());
        
        
    }

}
