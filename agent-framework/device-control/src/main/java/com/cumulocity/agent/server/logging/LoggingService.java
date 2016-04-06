package com.cumulocity.agent.server.logging;

import static com.cumulocity.agent.server.logging.LogFileCommandBuilder.searchInFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import c8y.LogfileRequest;

import com.cumulocity.agent.server.logging.LogFileCommandBuilder.InvalidSearchException;
import com.cumulocity.agent.server.repository.BinariesRepository;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.rest.representation.operation.Operations;
import com.cumulocity.sdk.client.SDKException;

@Component
public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    
    public static final String LOGFILE_CONTENT_TYPE = "text/plain";
    public static final String LOGFILE_FILE_EXTENSION = ".log";
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
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
        LogfileRequest request = operation.get(LogfileRequest.class);
        if (request == null) {
            logger.info("Could not handle operation with id: {} -> no AgentLogRequest fragment", operation.getId());
            return;
        }
        
        try {
            String command = buildCommand(request);
            String selfUrl = uploadLog(command, request);
            saveOperationWithLogLink(operation, request, selfUrl);
        } catch (InvalidSearchException e) {
            deviceControl.save(Operations.asFailedOperation(operation.getId(), e.getMessage()));
        } catch (Exception e) {
            deviceControl.save(Operations.asFailedOperation(operation.getId(), "Error on reading log file: " + e.getMessage()));
        }
    }

    private String buildCommand(LogfileRequest request) throws InvalidSearchException {
        LogFileCommandBuilder builder = searchInFile(logfile);
        if (request.getTenant() != null) {
            builder.withTenant(request.getTenant());
        }
        if (request.getDeviceUser() != null) {
            builder.withDeviceUser(request.getDeviceUser());
        }
        if (request.getDateFrom() != null && request.getDateTo() != null) {
            if (StringUtils.isEmpty(timestampFormat)) {
                builder.withTimeRange(request.getDateFrom().toDate(), request.getDateTo().toDate());
            } else {
                builder.withTimeRangeAndFormat(request.getDateFrom().toDate(), request.getDateTo().toDate(), timestampFormat);
            }
        }
        if (request.getSearchText() != null && request.getSearchText().isEmpty()) {
            builder.withSearchText(request.getSearchText());
        }
        if (request.getMaximumLines() > 0) {
            builder.withMaximumLines(request.getMaximumLines());
        }
        return builder.build();
    }
    
    private String uploadLog(String command, LogfileRequest request) throws SDKException, IOException {
        Process process =  null;
        try {
            logger.info("Run log command: {}", command);
            ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", command);
            builder.redirectErrorStream(true);
            process = builder.start();
            logger.info("Uploading log file");
            String filename = buildName(request);
            ManagedObjectRepresentation container = uploadFileDummy(filename);
            uploadLogFile(container.getId(), process.getInputStream());
            return container.getSelf();
        } catch (SDKException e) {
            logger.error("Could not upload log", e);
            throw e;
        } catch (IOException e) {
            logger.error("Could not read log", e);
            throw e;
        } finally {
            if (process != null) {
                logger.debug("Kill log read process");
                process.destroy();
            }
        }
    }
    
    private String buildName(LogfileRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(applicationId);
        if (request.getDeviceUser() != null) {
            builder.append("_");
            builder.append(request.getDeviceUser());
        }
        if (request.getDateFrom() != null && request.getDateTo() != null) {
            builder.append("_");
            builder.append(DATE_FORMAT.format(request.getDateFrom()));
        }
        builder.append(LOGFILE_FILE_EXTENSION);
        return builder.toString();
    }

    private void uploadLogFile(GId containerId, InputStream is) {
        binaries.replaceFile(containerId, LOGFILE_CONTENT_TYPE, is);
    }
    
    private ManagedObjectRepresentation uploadFileDummy(String filename) {
        ManagedObjectRepresentation container = new ManagedObjectRepresentation();
        container.setName(filename);
        container.setType(LOGFILE_CONTENT_TYPE);
        return binaries.uploadFile(container, new byte[] { 0x00 });
    }
    
    private void saveOperationWithLogLink(OperationRepresentation operation, LogfileRequest request, String selfUrl) {
        request.setFile(selfUrl.replace("managedObjects", "binaries"));
        OperationRepresentation updatedOperation = new OperationRepresentation();
        updatedOperation.setId(operation.getId());
        updatedOperation.set(request, LogfileRequest.class);
        updatedOperation.setStatus(OperationStatus.SUCCESSFUL.toString());
        deviceControl.save(updatedOperation);
    }
}
