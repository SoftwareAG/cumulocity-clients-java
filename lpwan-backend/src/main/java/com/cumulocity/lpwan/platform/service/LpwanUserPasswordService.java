package com.cumulocity.lpwan.platform.service;

import com.cumulocity.lpwan.lns.connection.model.LnsConnectionDeserializer;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class LpwanUserPasswordService {
    @Setter
    @Value("${application.name}")
    private String appName;

    @NonNull
    private TenantOptionApi options;

    @NonNull
    private final MicroserviceSubscriptionsService subscriptionsService;

    public String generatePasswordAndSave(String userName) {
        return subscriptionsService.callForTenant(subscriptionsService.getTenant(), new Callable<String>() {
            @Override
            public String call() {
                String generatedPassword = StrongPasswordGenerator.generate(userName.concat(subscriptionsService.getTenant()), 32);
                options.save(OptionRepresentation.asOptionRepresentation(
                        appName,
                        getUserPasswordKey(), generatedPassword));
                return generatedPassword;
            }
        });
    }

    public Optional<String> get() {
        return subscriptionsService.callForTenant(subscriptionsService.getTenant(), new Callable<Optional<String>>() {
            @Override
            public Optional<String> call() throws Exception {
                try {
                    OptionRepresentation fetchedOption = options.getOption(new OptionPK(appName, getUserPasswordKey()));
                    return Optional.ofNullable(fetchedOption.getValue());
                } catch (Exception e) {
                    if (e instanceof SDKException && ((SDKException) e).getHttpStatus() == SC_NOT_FOUND) {
                        log.error("Error while retrieving the user credentials", e);
                        return Optional.empty();
                    }
                    throw e;
                }
            }
        });
    }

    private String getUserPasswordKey() {
        String userPasswordKey = "credentials.%s.password";
        return String.format(userPasswordKey, LnsConnectionDeserializer.getRegisteredAgentName().toLowerCase());
    }

    private static class StrongPasswordGenerator {

        public static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
        public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String DIGITS = "0123456789";
        public static final String PUNCTUATION = "!@#$%&*()_+-=[]|.?><";
        public static final List<String> REQUIRED_CHARS = Arrays.asList(LOWER, UPPER, DIGITS, PUNCTUATION);

        public static String generate(String seed, int length) {
            if (length < REQUIRED_CHARS.size() + 1) {
                throw new IllegalArgumentException("Cannot generate strong password containing upper, " +
                        "lower letters, digits and special character when password length " +
                        "is less than five characters. ");
            }

            String initPassword = RandomStringUtils.random(length, 0, 0, true, true, null,
                    new SecureRandom(seed.getBytes()));

            StringBuilder password = new StringBuilder(initPassword);
            SecureRandom random = new SecureRandom();

            NextPositionToPlaceChar nextPositionToPlaceChar = new NextPositionToPlaceChar(random, password.length());
            for (String chars : REQUIRED_CHARS) {
                char requiredChar = chars.charAt(random.nextInt(chars.length() - 1));
                password.setCharAt(nextPositionToPlaceChar.get(), requiredChar);
            }
            return new String(password);
        }

        private static class NextPositionToPlaceChar {

            List<Integer> alreadyReplacedPositions = new ArrayList<>();
            SecureRandom random;
            int passwordLength;

            NextPositionToPlaceChar(SecureRandom random, int passwordLength) {
                this.random = random;
                this.passwordLength = passwordLength;
            }

            int get() {
                int indexToPlaceNewChar = random.nextInt(passwordLength - 1);
                while (alreadyReplacedPositions.contains(indexToPlaceNewChar)) {
                    indexToPlaceNewChar = random.nextInt(passwordLength - 1);
                }
                alreadyReplacedPositions.add(indexToPlaceNewChar);
                return indexToPlaceNewChar;
            }
        }
    }

}


