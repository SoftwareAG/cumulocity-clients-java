package com.cumulocity.sdk.client.cep;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;

import com.google.common.io.Resources;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class CepApiIT  extends JavaSdkITBase {


    final CepApi cepApi = platform.getCepApi();

    @AfterEach
    public void cleanup() {
        final PagedCepModuleCollectionRepresentation cep = cepApi.getModules().get();
        final Iterable<CepModuleRepresentation> it = cep.allPages();
        for ( CepModuleRepresentation cepModule : Lists.newLinkedList(it)) {
            cepApi.delete(cepModule);
        }
    }

//  Ignored till resolving issue: MTM-42839
    @Disabled
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

//  Ignored till resolving issue: MTM-42839
    @Disabled
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
