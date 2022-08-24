package com.cumulocity.rest.representation.reliable.notification;

import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.SourceableRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

import java.util.List;

public class NotificationSubscriptionRepresentation extends AbstractExtensibleRepresentation implements Cloneable, SourceableRepresentation {

    private String context;

    private String subscription;

    private List<String> fragmentsToCopy;

    private GId id;

    private boolean nonPersistent;

    private ManagedObjectRepresentation source;

    private NotificationSubscriptionFilterRepresentation subscriptionFilter;

    @JSONConverter(type = IDTypeConverter.class)
    public void setId(GId id) {
        this.id = id;
    }

    @JSONProperty(ignoreIfNull = true)
    public GId getId() {
        return id;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getContext() {
        return context;
    }

    @JSONProperty(ignoreIfNull = true)
    public boolean isNonPersistent() {
        return nonPersistent;
    }

    public void setNonPersistent(boolean nonPersistent) { this.nonPersistent = nonPersistent; }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getSubscription() {
        return subscription;
    }

    public void setSubscriptionFilter(NotificationSubscriptionFilterRepresentation subscriptionFilter) {
        this.subscriptionFilter = subscriptionFilter;
    }

    @JSONProperty(ignoreIfNull = true)
    public NotificationSubscriptionFilterRepresentation getSubscriptionFilter() {
        return subscriptionFilter;
    }

    public void setFragmentsToCopy(List<String> fragmentsToCopy) {
        this.fragmentsToCopy = fragmentsToCopy;
    }

    @JSONProperty(ignoreIfNull = true)
    public List<String> getFragmentsToCopy() {
        return fragmentsToCopy;
    }

    public void setSource(ManagedObjectRepresentation source) {
        this.source = source;
    }

    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectRepresentation getSource() {
        return source;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
