package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

import static java.lang.String.valueOf;

public class MicroserviceApiRepresentation extends AbstractExtensibleRepresentation {
    public final static String APPLICATION_ID = "{{applicationId}}";
    public final static String APPLICATION_NAME = "{{applicationName}}";

    private String updateUrl;
    private String subscriptionsUrl;
    private String getUrl;

    @java.beans.ConstructorProperties({"updateUrl", "subscriptionsUrl", "getUrl"})
    public MicroserviceApiRepresentation(String updateUrl, String subscriptionsUrl, String getUrl) {
        this.updateUrl = updateUrl;
        this.subscriptionsUrl = subscriptionsUrl;
        this.getUrl = getUrl;
    }

    public MicroserviceApiRepresentation() {
    }

    public static MicroserviceApiRepresentationBuilder microserviceApiRepresentation() {
        return new MicroserviceApiRepresentationBuilder();
    }

    public String getAppUrl(String baseUrl) {
        return url(baseUrl, getGetUrl(), null, null);
    }

    public String getSubscriptionsUrl(String baseUrl) {
        return url(baseUrl, getSubscriptionsUrl(), null, null);
    }

    public String getUpdateUrl(String baseUrl, String name, String id) {
        return url(baseUrl, getUpdateUrl(), name, id);
    }

    private static String url(String baseUrl, String url, String applicationName, String applicationId) {
        return baseUrl + url.replace(APPLICATION_NAME, valueOf(applicationName)).replace(APPLICATION_ID, valueOf(applicationId));
    }

    public String getUpdateUrl() {
        return this.updateUrl;
    }

    public String getSubscriptionsUrl() {
        return this.subscriptionsUrl;
    }

    public String getGetUrl() {
        return this.getUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public void setSubscriptionsUrl(String subscriptionsUrl) {
        this.subscriptionsUrl = subscriptionsUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }

    public String toString() {
        return "MicroserviceApiRepresentation(updateUrl=" + this.getUpdateUrl() + ", subscriptionsUrl=" + this.getSubscriptionsUrl() + ", getUrl=" + this.getGetUrl() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MicroserviceApiRepresentation)) return false;
        final MicroserviceApiRepresentation other = (MicroserviceApiRepresentation) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        final Object this$updateUrl = this.getUpdateUrl();
        final Object other$updateUrl = other.getUpdateUrl();
        if (this$updateUrl == null ? other$updateUrl != null : !this$updateUrl.equals(other$updateUrl)) return false;
        final Object this$subscriptionsUrl = this.getSubscriptionsUrl();
        final Object other$subscriptionsUrl = other.getSubscriptionsUrl();
        if (this$subscriptionsUrl == null ? other$subscriptionsUrl != null : !this$subscriptionsUrl.equals(other$subscriptionsUrl))
            return false;
        final Object this$getUrl = this.getGetUrl();
        final Object other$getUrl = other.getGetUrl();
        if (this$getUrl == null ? other$getUrl != null : !this$getUrl.equals(other$getUrl)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + super.hashCode();
        final Object $updateUrl = this.getUpdateUrl();
        result = result * PRIME + ($updateUrl == null ? 43 : $updateUrl.hashCode());
        final Object $subscriptionsUrl = this.getSubscriptionsUrl();
        result = result * PRIME + ($subscriptionsUrl == null ? 43 : $subscriptionsUrl.hashCode());
        final Object $getUrl = this.getGetUrl();
        result = result * PRIME + ($getUrl == null ? 43 : $getUrl.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MicroserviceApiRepresentation;
    }

    public static class MicroserviceApiRepresentationBuilder {
        private String updateUrl;
        private String subscriptionsUrl;
        private String getUrl;

        MicroserviceApiRepresentationBuilder() {
        }

        public MicroserviceApiRepresentation.MicroserviceApiRepresentationBuilder updateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
            return this;
        }

        public MicroserviceApiRepresentation.MicroserviceApiRepresentationBuilder subscriptionsUrl(String subscriptionsUrl) {
            this.subscriptionsUrl = subscriptionsUrl;
            return this;
        }

        public MicroserviceApiRepresentation.MicroserviceApiRepresentationBuilder getUrl(String getUrl) {
            this.getUrl = getUrl;
            return this;
        }

        public MicroserviceApiRepresentation build() {
            return new MicroserviceApiRepresentation(updateUrl, subscriptionsUrl, getUrl);
        }

        public String toString() {
            return "MicroserviceApiRepresentation.MicroserviceApiRepresentationBuilder(updateUrl=" + this.updateUrl + ", subscriptionsUrl=" + this.subscriptionsUrl + ", getUrl=" + this.getUrl + ")";
        }
    }
}
