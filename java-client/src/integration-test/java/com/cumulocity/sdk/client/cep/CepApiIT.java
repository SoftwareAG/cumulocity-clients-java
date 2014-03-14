package com.cumulocity.sdk.client.cep;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.newInputStreamSupplier;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Test;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
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
        final InputSupplier<InputStream> cepModuleFile = cepModuleFile("cep/test-module.epl");
        //When
        final CepModuleRepresentation cepModule = cepApi.create(cepModuleFile.getInput());
        //Then
        assertThat(cepModule).isNotNull();
        assertThat(cepModule.getId()).isNotNull().isNotEmpty();
    }
    
    
    
    @Test
    public void shouldDeleteCepModule() throws IOException {
        //Given
        final CepModuleRepresentation cepModule = cepApi.create( cepModuleFile("cep/test-module.epl").getInput());
        //When
        cepApi.delete(cepModule);
        //Then
        assertThat(cepModule).isNotNull();
        assertThat(cepModule.getId()).isNotNull().isNotEmpty();
    }

    private InputSupplier<InputStream> cepModuleFile(String module) {
        return newInputStreamSupplier(getResource(module));
    }

}
