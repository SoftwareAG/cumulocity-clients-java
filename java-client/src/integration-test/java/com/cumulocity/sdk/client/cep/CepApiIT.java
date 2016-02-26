package com.cumulocity.sdk.client.cep;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import org.junit.After;
import org.junit.Test;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.InputSupplier;

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
        final String cepModuleFile = cepModuleFile("cep/test-module.epl");
        //When
        final CepModuleRepresentation cepModule = cepApi.create(cepModuleFile);
        //Then
        assertThat(cepModule).isNotNull();
        assertThat(cepModule.getId()).isNotNull().isNotEmpty();
    }
    
    
    
    @Test
    public void shouldDeleteCepModule() throws IOException {
        //Given
        final CepModuleRepresentation cepModule = cepApi.create( cepModuleFile("cep/test-module.epl"));
        //When
        cepApi.delete(cepModule);
        //Then
        assertThat(cepModule).isNotNull();
        assertThat(cepModule.getId()).isNotNull().isNotEmpty();
    }

    private String cepModuleFile(String module) throws IOException {
        URL resource = Resources.getResource(module);
        return Resources.toString(resource, Charsets.UTF_8);
    }

}
