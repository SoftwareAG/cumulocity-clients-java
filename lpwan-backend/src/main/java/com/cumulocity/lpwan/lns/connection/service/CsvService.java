package com.cumulocity.lpwan.lns.connection.service;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@TenantScope
public class CsvService {

    private static final String[] HEADERS = {"Tenant Name", "Device Name", "Device Managed Object Id"};
    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withHeader(HEADERS);

    @Autowired
    private ContextService<Credentials> contextService;

    public ByteArrayInputStream writeDataToCsv(final List<ManagedObjectRepresentation> deviceMoList) {
        log.info("Writing data to the csv printer");
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)) {
            for (final ManagedObjectRepresentation deviceMo : deviceMoList) {
                final List<String> data = Arrays.asList(
                        contextService.getContext().getTenant(),
                        deviceMo.getName(),
                        deviceMo.getId().getValue());

                printer.printRecord(data);
            }
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException("Csv writing error: " + e.getMessage());
        }
    }
}
