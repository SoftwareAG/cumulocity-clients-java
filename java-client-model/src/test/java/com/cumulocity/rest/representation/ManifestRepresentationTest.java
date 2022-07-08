package com.cumulocity.rest.representation;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cumulocity.rest.representation.application.ManifestRepresentation;

public class ManifestRepresentationTest {
    
    @Test
    public void shouldRemoveDuplicateEntriesFromImports() {
        // Given
        ManifestRepresentation manifest = aManifestWithDuplicateImports();
        
        // When
        List<String> uniqueImports = manifest.getUniqueImports();
        
        // Then
        assertThat(uniqueImports).doesNotHaveDuplicates();
    }
    
    @Test
    public void shouldReturnNullIfNoImports() {
     // Given
        ManifestRepresentation manifest = new ManifestRepresentation();
        
        // When
        List<String> uniqueImports = manifest.getUniqueImports();
        
        // Then
        assertThat(uniqueImports).isNull();
    }

    private ManifestRepresentation duplicateImports(ManifestRepresentation manifest) {
        ManifestRepresentation newRepresentation = new ManifestRepresentation();
        List<String> imports = manifest.getImports();
        List<String> newImports = new ArrayList<String>();
        for (String imp : imports) {
            newImports.add(imp);
            newImports.add(imp);
        }
        newRepresentation.setImports(newImports);
        return newRepresentation;
    }

    private ManifestRepresentation aManifestWithDuplicateImports() {
        ManifestRepresentation manifest = new ManifestRepresentation();
        List<String> listImports = new ArrayList<String>();
        listImports.add("myplugin1");
        listImports.add("myplugin2");
        listImports.add("myplugin3");
        manifest.setImports(listImports);
        return duplicateImports(manifest);
    }
}
