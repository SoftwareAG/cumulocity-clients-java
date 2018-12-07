package com.cumulocity.sdk.services.client;

import com.cumulocity.email.client.EmailApi;
import com.cumulocity.email.client.EmailApiImpl;
import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sms.client.SmsMessagingApi;
import com.cumulocity.sms.client.SmsMessagingApiImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.net.URI;

import static org.apache.http.client.fluent.Executor.newInstance;

@Slf4j
public class ServicesPlatformImpl implements ServicesPlatform {

    public interface CredentialsProvider {
        String getTenant();

        String getUsername();

        String getPassword();
    }

    private final String host;
    private final Executor executor;

    public ServicesPlatformImpl(String host, final CumulocityBasicCredentials credentials) {
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

    public ServicesPlatformImpl(String host, final CredentialsProvider auth) {
        if (host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }
        this.host = host;
        this.executor = createExecutor(host, auth);
    }

    @Override
    public SmsMessagingApi getSmsMessagingApi() {
        return new SmsMessagingApiImpl(host, executor);
    }

    @Override
    public EmailApi getEmailApi() {
        return new EmailApiImpl(host, executor);
    }

    private static Executor createExecutor(final String url, final CredentialsProvider auth) {
        return newInstance()
                .use(new BasicCredentialsProvider() {
                    public Credentials getCredentials(AuthScope authscope) {
                        return new UsernamePasswordCredentials(auth.getTenant() + "/" + auth.getUsername(), auth.getPassword());
                    }
                })
                .authPreemptive(getHost(url));
    }

    private static HttpHost getHost(String host) {
        final URI uri = URI.create(host);
        return new HttpHost(uri.getHost(), uri.getPort());
    }
}
