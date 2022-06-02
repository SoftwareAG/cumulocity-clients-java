package com.cumulocity.lpwan.lns.connection.service;

import com.cumulocity.lpwan.exception.LpwanServiceException;
import com.cumulocity.lpwan.lns.connection.model.LpwanDevice;
import com.cumulocity.microservice.context.inject.TenantScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

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

    private static final String[] HEADERS = {"Device Name", "Device Managed Object Id"};
    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withHeader(HEADERS);

    public ByteArrayInputStream writeDataToCsv(final List<LpwanDevice> deviceList) throws LpwanServiceException {
        log.info("Writing data to the csv printer");
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)) {
            for (final LpwanDevice device : deviceList) {
                final List<String> data = Arrays.asList(
                        device.getName(),
                        device.getManagedObjectId());

                printer.printRecord(data);
            }
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (final IOException e) {
            String errorMessage = "Csv writing error: " + e.getMessage();
            log.error(errorMessage, e);
            throw new LpwanServiceException(errorMessage, e);
        }
    }
}
