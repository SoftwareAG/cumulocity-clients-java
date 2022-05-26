package com.cumulocity.lpwan.lns.connection.service;

import com.cumulocity.lpwan.lns.connection.model.LpwanDeviceDetails;
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

    public ByteArrayInputStream writeDataToCsv(final List<LpwanDeviceDetails> deviceMoList) {
        log.info("Writing data to the csv printer");
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)) {
            for (final LpwanDeviceDetails deviceMo : deviceMoList) {
                final List<String> data = Arrays.asList(
                        deviceMo.getDeviceName(),
                        deviceMo.getDeviceMoId());

                printer.printRecord(data);
            }
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException("Csv writing error: " + e.getMessage());
        }
    }
}
