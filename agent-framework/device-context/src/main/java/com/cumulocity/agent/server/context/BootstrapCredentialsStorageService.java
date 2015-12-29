package com.cumulocity.agent.server.context;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.devicecontrol.DeviceCredentialsApi;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import static org.springframework.core.io.support.PropertiesLoaderUtils.loadProperties;

public class BootstrapCredentialsStorageService {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapCredentialsStorageService.class);

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String TENANT_KEY = "tenant";

    private final DeviceCredentialsApi deviceCredentialsApi;
    private final String deviceCredentialsFilePath;
    private final File credentialsSource;

    @Autowired
    public BootstrapCredentialsStorageService(DeviceCredentialsApi deviceCredentialsApi, String deviceCredentialsFilePath) {
        this.deviceCredentialsApi = deviceCredentialsApi;
        this.deviceCredentialsFilePath = deviceCredentialsFilePath;
        credentialsSource = new File(deviceCredentialsFilePath);
    }

    public DeviceCredentials getCredentials(String imei) throws SDKException, IOException {
        checkFileExistance();
        FileSystemResource resource = new FileSystemResource(credentialsSource);
        Properties credentialsProps = loadProperties(resource);
        DeviceCredentials credentials = fromProperties(credentialsProps, imei);
        if (credentials != null) {
            return credentials;
        }
        logger.debug("Polling credentials from C8Y for serial: {}", imei);
        return pollCredentials(credentialsProps, imei);
    }

    private void checkFileExistance() throws IllegalStateException {
        if (!credentialsSource.exists()) {
            throw new IllegalStateException(String.format("Credentials file %s doesn't exist!",
                    deviceCredentialsFilePath));
        }
    }

    private DeviceCredentials fromProperties(Properties credentialsProps, String imei) {
        String tenant = credentialsProps.getProperty(key(imei, TENANT_KEY));
        String username = credentialsProps.getProperty(key(imei, USERNAME_KEY));
        String password = credentialsProps.getProperty(key(imei, PASSWORD_KEY));
        if (tenant != null && username != null && password != null) {
            logger.debug("Found credentials for {}/{} from {}", tenant, username, deviceCredentialsFilePath);
            return new DeviceCredentials(tenant, username, password, null, new GId(imei));
        }
        logger.error("There is no credentials for serial {} in {}", imei, deviceCredentialsFilePath);
        return null;
    }

    private synchronized DeviceCredentials pollCredentials(Properties credentialsProps, String imei) throws IOException {
        DeviceCredentialsRepresentation credentials = deviceCredentialsApi.pollCredentials(imei);
        logger.debug("Found credentials for {}/{}:{} from C8Y", credentials.getTenantId(),
                credentials.getUsername(), credentials.getPassword());
        writeCredentialsToFile(imei, credentials, credentialsProps);
        return toDeviceCredentials(credentials, imei);
    }

    private void writeCredentialsToFile(String imei, DeviceCredentialsRepresentation credentials, Properties credentialsProps) throws IOException {
        credentialsProps.setProperty(key(imei, TENANT_KEY), credentials.getTenantId());
        credentialsProps.setProperty(key(imei, USERNAME_KEY), credentials.getUsername());
        credentialsProps.setProperty(key(imei, PASSWORD_KEY), credentials.getPassword());
        credentialsProps.store(new FileSystemResource(credentialsSource).getOutputStream(), "Device Credentials");
    }

    private DeviceCredentials toDeviceCredentials(DeviceCredentialsRepresentation representation, String deviceId) {
        return new DeviceCredentials(
                representation.getTenantId(),
                representation.getUsername(),
                representation.getPassword(),
                null,
                new GId(deviceId));
    }

    private String key(String imei, String fieldName) {
        return String.format("%s.%s", imei, fieldName);
    }
}
