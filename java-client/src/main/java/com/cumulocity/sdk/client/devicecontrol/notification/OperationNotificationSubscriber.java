package com.cumulocity.sdk.client.devicecontrol.notification;

import static com.cumulocity.sdk.client.notification.DefaultBayeuxClientProvider.createProvider;

import org.cometd.bayeux.Message;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.notification.Subscriber;
import com.cumulocity.sdk.client.notification.SubscriberImpl;
import com.cumulocity.sdk.client.notification.Subscription;
import com.cumulocity.sdk.client.notification.SubscriptionListener;
import com.cumulocity.sdk.client.notification.SubscriptionNameResolver;

public class OperationNotificationSubscriber implements Subscriber<GId, OperationRepresentation> {
    public static final String DEVICE_CONTROL_NOTIFICATIONS_URL = "devicecontrol/notifications";

    private final Subscriber<GId, Message> subscriber;

    public OperationNotificationSubscriber(PlatformParameters parameters) {
        subscriber = new SubscriberImpl<GId>(new AgentDeviceIdAsSubscriptonName(), createProvider(DEVICE_CONTROL_NOTIFICATIONS_URL,
                parameters, OperationRepresentation.class));
    }

    public Subscription<GId> subscribe(final GId object, final SubscriptionListener<GId, OperationRepresentation> handler) {
        return subscriber.subscribe(object, new MappingSubscriptionListener(handler));
    }

    public void start() throws SDKException {
        subscriber.start();
    }

    public void stop() {
        subscriber.stop();
    }

    private static final class MappingSubscriptionListener implements SubscriptionListener<GId, Message> {
        private final SubscriptionListener<GId, OperationRepresentation> handler;

        private MappingSubscriptionListener(SubscriptionListener<GId, OperationRepresentation> handler) {
            this.handler = handler;
        }

        @Override
        public void onNotification(Subscription<GId> subscription, Message notification) {
            handler.onNotification(subscription, asOperation(notification));
        }

        private OperationRepresentation asOperation(Message notification) {
            final Object data = notification.getData();
            return data instanceof OperationRepresentation ? (OperationRepresentation) data : null;
        }

        @Override
        public void onError(Subscription<GId> subscription, Throwable ex) {
            handler.onError(subscription, ex);
        }
    }

    private static final class AgentDeviceIdAsSubscriptonName implements SubscriptionNameResolver<GId> {
        @Override
        public String apply(GId id) {
            return  "/" + id.getValue();
        }
    }
}
