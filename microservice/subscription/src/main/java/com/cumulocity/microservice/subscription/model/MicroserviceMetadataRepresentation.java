package com.cumulocity.microservice.subscription.model;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import org.svenson.JSONProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MicroserviceMetadataRepresentation extends AbstractExtensibleRepresentation {

    private List<String> requiredRoles;

    private List<String> roles;

    private String url;

    @java.beans.ConstructorProperties({"requiredRoles", "roles", "url"})
    public MicroserviceMetadataRepresentation(List<String> requiredRoles, List<String> roles, String url) {
        this.requiredRoles = requiredRoles;
        this.roles = roles;
        this.url = url;
    }

    public MicroserviceMetadataRepresentation() {
    }

    public static MicroserviceMetadataRepresentationBuilder microserviceMetadataRepresentation() {
        return new MicroserviceMetadataRepresentationBuilder();
    }

    @JSONProperty(ignoreIfNull = true)
    public List<String> getRequiredRoles() {
        return requiredRoles;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getUrl() {
        return url;
    }

    @JSONProperty(ignoreIfNull = true)
    public List<String> getRoles() {
        return roles;
    }

    public void setRequiredRoles(List<String> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return "MicroserviceMetadataRepresentation(requiredRoles=" + this.getRequiredRoles() + ", roles=" + this.getRoles() + ", url=" + this.getUrl() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MicroserviceMetadataRepresentation)) return false;
        final MicroserviceMetadataRepresentation other = (MicroserviceMetadataRepresentation) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        final Object this$requiredRoles = this.getRequiredRoles();
        final Object other$requiredRoles = other.getRequiredRoles();
        if (this$requiredRoles == null ? other$requiredRoles != null : !this$requiredRoles.equals(other$requiredRoles))
            return false;
        final Object this$roles = this.getRoles();
        final Object other$roles = other.getRoles();
        if (this$roles == null ? other$roles != null : !this$roles.equals(other$roles)) return false;
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + super.hashCode();
        final Object $requiredRoles = this.getRequiredRoles();
        result = result * PRIME + ($requiredRoles == null ? 43 : $requiredRoles.hashCode());
        final Object $roles = this.getRoles();
        result = result * PRIME + ($roles == null ? 43 : $roles.hashCode());
        final Object $url = this.getUrl();
        result = result * PRIME + ($url == null ? 43 : $url.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MicroserviceMetadataRepresentation;
    }

    public static class MicroserviceMetadataRepresentationBuilder {
        private ArrayList<String> requiredRoles;
        private ArrayList<String> roles;
        private String url;

        MicroserviceMetadataRepresentationBuilder() {
        }

        public MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder requiredRole(String requiredRole) {
            if (this.requiredRoles == null) this.requiredRoles = new ArrayList<String>();
            this.requiredRoles.add(requiredRole);
            return this;
        }

        public MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder requiredRoles(Collection<? extends String> requiredRoles) {
            if (this.requiredRoles == null) this.requiredRoles = new ArrayList<String>();
            this.requiredRoles.addAll(requiredRoles);
            return this;
        }

        public MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder clearRequiredRoles() {
            if (this.requiredRoles != null)
                this.requiredRoles.clear();

            return this;
        }

        public MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder role(String role) {
            if (this.roles == null) this.roles = new ArrayList<String>();
            this.roles.add(role);
            return this;
        }

        public MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder roles(Collection<? extends String> roles) {
            if (this.roles == null) this.roles = new ArrayList<String>();
            this.roles.addAll(roles);
            return this;
        }

        public MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder clearRoles() {
            if (this.roles != null)
                this.roles.clear();

            return this;
        }

        public MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder url(String url) {
            this.url = url;
            return this;
        }

        public MicroserviceMetadataRepresentation build() {
            List<String> requiredRoles;
            switch (this.requiredRoles == null ? 0 : this.requiredRoles.size()) {
                case 0:
                    requiredRoles = java.util.Collections.emptyList();
                    break;
                case 1:
                    requiredRoles = java.util.Collections.singletonList(this.requiredRoles.get(0));
                    break;
                default:
                    requiredRoles = java.util.Collections.unmodifiableList(new ArrayList<String>(this.requiredRoles));
            }
            List<String> roles;
            switch (this.roles == null ? 0 : this.roles.size()) {
                case 0:
                    roles = java.util.Collections.emptyList();
                    break;
                case 1:
                    roles = java.util.Collections.singletonList(this.roles.get(0));
                    break;
                default:
                    roles = java.util.Collections.unmodifiableList(new ArrayList<String>(this.roles));
            }

            return new MicroserviceMetadataRepresentation(requiredRoles, roles, url);
        }

        public String toString() {
            return "MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder(requiredRoles=" + this.requiredRoles + ", roles=" + this.roles + ", url=" + this.url + ")";
        }
    }
}
