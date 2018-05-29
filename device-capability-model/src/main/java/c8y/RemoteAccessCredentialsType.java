package c8y;

import org.springframework.util.StringUtils;

public enum RemoteAccessCredentialsType {
    NONE(new CredentialsValidator() {
        @Override
        public void validateCredentials(RemoteAccessCredentials credentials) {

        }
    }),
    USER_PASS(new CredentialsValidator() {
        @Override
        public void validateCredentials(RemoteAccessCredentials credentials) {
            validateValueNotEmpty(credentials.getUsername(), "User", USER_PASS);
        }
    }),
    KEY_PAIR(new CredentialsValidator() {
        @Override
        public void validateCredentials(RemoteAccessCredentials credentials) {
            validateValueNotEmpty(credentials.getUsername(), "User", USER_PASS);
            validateValueNotEmpty(credentials.getPrivateKey(), "Private key", KEY_PAIR);
            validateValueNotEmpty(credentials.getPublicKey(), "Public key", KEY_PAIR);
        }
    }),
    KEY_PAIR_HOST(new CredentialsValidator() {
        @Override
        public void validateCredentials(RemoteAccessCredentials credentials) {
            validateValueNotEmpty(credentials.getUsername(), "User", USER_PASS);
            validateValueNotEmpty(credentials.getPrivateKey(), "Private key", KEY_PAIR_HOST);
            validateValueNotEmpty(credentials.getPublicKey(), "Public key", KEY_PAIR_HOST);
            validateValueNotEmpty(credentials.getHostCertificate(), "Host certificate", KEY_PAIR_HOST);
        }
    });



    private static void validateValueNotEmpty(String valueToCheck, final String fieldName, RemoteAccessCredentialsType credentialsType) {
        if (StringUtils.isEmpty(valueToCheck)) {
            throw new RemoteAccessConfigurationException("%s must be provided in %s credentials", fieldName, credentialsType);
        }
    }

    private final CredentialsValidator credentialsValidator;

    RemoteAccessCredentialsType(CredentialsValidator credentialsValidator) {
        this.credentialsValidator = credentialsValidator;
    }

    public void validateCredentials(RemoteAccessCredentials credentials) {
        this.credentialsValidator.validateCredentials(credentials);
    }

    private interface CredentialsValidator {
        void validateCredentials(RemoteAccessCredentials credentials);
    }
}
