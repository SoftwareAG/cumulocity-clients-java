package com.cumulocity.sdk.client.cep;

import static com.google.common.io.Resources.getResource;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import org.junit.After;
import org.junit.Test;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.google.common.collect.Lists;

public class CepApiIT  extends JavaSdkITBase{

    
    final CepApi cepApi = platform.getCepApi();
    
    @After
    public void cleanup() {
        final PagedCepModuleCollectionRepresentation cep = cepApi.getModules().get();
        final Iterable<CepModuleRepresentation> it = cep.allPages();
        for ( CepModuleRepresentation cepModule : Lists.newLinkedList(it)) {
            cepApi.delete(cepModule);
        }
    }
    
    @Test
    public void shouldCreateCepModule() throws IOException {
        //Given
        final ByteSource cepModuleFile = cepModuleFile("cep/test-module.epl");
        //When
        final CepModuleRepresentation cepModule = cepApi.create(cepModuleFile.openStream());
        //Then
        assertThat(cepModule).isNotNull();
        assertThat(cepModule.getId()).isNotNull().isNotEmpty();
    }
    
    
    
    @Test
    public void shouldDeleteCepModule() throws IOException {
        //Given
        final CepModuleRepresentation cepModule = cepApi.create( cepModuleFile("cep/test-module.epl").openStream());
        //When
        cepApi.delete(cepModule);
        //Then
        assertThat(cepModule).isNotNull();
        assertThat(cepModule.getId()).isNotNull().isNotEmpty();
    }

    private ByteSource cepModuleFile(String module) {
        return Resources.asByteSource(getResource(module));
    }

}
