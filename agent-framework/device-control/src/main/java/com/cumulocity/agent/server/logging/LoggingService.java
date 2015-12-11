package com.cumulocity.agent.server.logging;

import static com.cumulocity.agent.server.logging.LogFileCommandBuilder.searchInFile;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import c8y.AgentLogRequest;

import com.cumulocity.agent.server.repository.BinariesRepository;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.rest.representation.operation.Operations;
import com.cumulocity.sdk.client.SDKException;

@Component
public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    
    public static final String LOGFILE_CONTENT_TYPE = "text/plain";
    public static final String LOGFILE_FILE_EXTENSION = ".log";
    
    private final DeviceControlRepository deviceControl;
    
    private final BinariesRepository binaries;
    
    private final String logfile;
    
    private final String timestampFormat;
    
    private final String applicationId;
    
    @Autowired
    public LoggingService(DeviceControlRepository deviceControl, BinariesRepository binaries, 
            @Value("${C8Y.log.file.path}") String logfile, @Value("${C8Y.log.timestamp.format:}") String timestampFormat,
            @Value("${C8Y.application.id}") String applicationId) {
        this.logfile = logfile;
        this.deviceControl = deviceControl;
        this.binaries = binaries;
        this.timestampFormat = timestampFormat;
        this.applicationId = applicationId;
    }
    
    public void readLog(OperationRepresentation operation) throws SDKException {
        AgentLogRequest request = operation.get(AgentLogRequest.class);
        if (request == null) {
            logger.info("Could not handle operation with id: {} -> no AgentLogRequest fragment", operation.getId());
            return;
        }
        String command = buildCommand(request);
        try {
            InputStream logInput = readFile(command);
            uploadLog(operation, logInput);
        } catch (Exception e) {
            logger.error("Could not read log file", e);
            deviceControl.save(Operations.asFailedOperation(operation.getDeviceId(), "Error on reading file: " + e.getMessage()));
        }
    }

    private InputStream readFile(String command) throws IOException, InterruptedException {
        logger.info("Run log command: {}", command);
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        return process.getInputStream();
    }
    
    private String buildCommand(AgentLogRequest request) {
        LogFileCommandBuilder builder = searchInFile(logfile);
        if (request.getTenant() != null) {
            builder.withTenant(request.getTenant());
        }
        if (request.getDeviceUser() != null) {
            builder.withDeviceUser(request.getDeviceUser());
        }
        if (request.getDateFrom() != null && request.getDateTo() != null) {
            if (StringUtils.isEmpty(timestampFormat)) {
                builder.withTimeRange(request.getDateFrom(), request.getDateTo());
            } else {
                builder.withTimeRangeAndFormat(request.getDateFrom(), request.getDateTo(), timestampFormat);
            }
        }
        if (request.getSearchText() != null) {
            builder.withSearchText(request.getSearchText());
        }
        if (request.getMaximumLines() > 0) {
            builder.withMaximumLines(request.getMaximumLines());
        }
        return builder.build();
    }
    
    private void uploadLog(OperationRepresentation operation, InputStream is) {
        try {
            String filename = buildName(operation);
            ManagedObjectRepresentation container = uploadFileDummy(filename);
            uploadLogFile(container.getId(), is);
        } catch (SDKException e) {
            logger.error("Could not upload log", e);
        }
    }
    
    private String buildName(OperationRepresentation operation) {
        StringBuilder builder = new StringBuilder();
        builder.append(applicationId);
        builder.append("_ID");
        builder.append(operation.getId().getValue());
        builder.append(LOGFILE_FILE_EXTENSION);
        return builder.toString();
    }

    private void uploadLogFile(GId containerId, InputStream is) {
        binaries.replaceFile(containerId, LOGFILE_CONTENT_TYPE, is);
    }
    
    private ManagedObjectRepresentation uploadFileDummy(String filename) {
        ManagedObjectRepresentation container = new ManagedObjectRepresentation();
        container.setName(filename);
        return binaries.uploadFile(container, new byte[]{0x00});
    }
}
