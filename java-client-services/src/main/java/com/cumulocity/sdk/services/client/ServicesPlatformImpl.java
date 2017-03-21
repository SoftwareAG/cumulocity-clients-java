package com.cumulocity.sdk.services.client;

import com.cumulocity.email.client.EmailApi;
import com.cumulocity.email.client.EmailApiImpl;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sms.client.SmsMessagingApi;
import com.cumulocity.sms.client.SmsMessagingApiImpl;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.BasicCredentialsProvider;

import static org.apache.http.client.fluent.Executor.newInstance;

public class ServicesPlatformImpl implements ServicesPlatform {

    public interface CredentialsProvider {
        String getTenant();
        String getUsername();
        String getPassword();
    }

    private final String host;
    private final Executor authorizedTemplate;

    public ServicesPlatformImpl(String host, final CredentialsProvider auth) {
        if (host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }
        this.host = host;
        this.authorizedTemplate = newInstance().use(new BasicCredentialsProvider() {
            public Credentials getCredentials(AuthScope authscope) {
                return new UsernamePasswordCredentials(auth.getTenant() + "/" + auth.getUsername(), auth.getPassword());
            }
        });
    }

    public ServicesPlatformImpl(String host, final CumulocityCredentials credentials) {
        this(host, new CredentialsProvider() {
            public String getTenant() {
                return credentials.getTenantId();
            }

            public String getUsername() {
                return credentials.getUsername();
            }

            public String getPassword() {
                return credentials.getPassword();
            }
        });
    }

    @Override
    public SmsMessagingApi getSmsMessagingApi() {
        return new SmsMessagingApiImpl(host, authorizedTemplate);
    }

    @Override
    public EmailApi getEmailApi() {
        return new EmailApiImpl(host, authorizedTemplate);
    }
}
