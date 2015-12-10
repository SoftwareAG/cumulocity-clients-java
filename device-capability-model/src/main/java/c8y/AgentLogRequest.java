package c8y;

import java.io.Serializable;
import java.util.Date;

import org.svenson.AbstractDynamicProperties;

public class AgentLogRequest extends AbstractDynamicProperties implements Serializable {

    private static final long serialVersionUID = -6443811928706492241L;
    
	private Date dateFrom;
	private Date dateTo;
	private int maximumLines = 0;
	private String file;
	private String searchText;
	private String tenant;
	private String deviceUser;
	
	public AgentLogRequest() {}
	
	public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public int getMaximumLines() {
        return maximumLines;
    }

    public void setMaximumLines(int maximumLines) {
        this.maximumLines = maximumLines;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getDeviceUser() {
        return deviceUser;
    }

    public void setDeviceUser(String deviceUser) {
        this.deviceUser = deviceUser;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateFrom == null) ? 0 : dateFrom.hashCode());
        result = prime * result + ((dateTo == null) ? 0 : dateTo.hashCode());
        result = prime * result + ((deviceUser == null) ? 0 : deviceUser.hashCode());
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        result = prime * result + maximumLines;
        result = prime * result + ((searchText == null) ? 0 : searchText.hashCode());
        result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AgentLogRequest other = (AgentLogRequest) obj;
        if (dateFrom == null) {
            if (other.dateFrom != null)
                return false;
        } else if (!dateFrom.equals(other.dateFrom))
            return false;
        if (dateTo == null) {
            if (other.dateTo != null)
                return false;
        } else if (!dateTo.equals(other.dateTo))
            return false;
        if (deviceUser == null) {
            if (other.deviceUser != null)
                return false;
        } else if (!deviceUser.equals(other.deviceUser))
            return false;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.equals(other.file))
            return false;
        if (maximumLines != other.maximumLines)
            return false;
        if (searchText == null) {
            if (other.searchText != null)
                return false;
        } else if (!searchText.equals(other.searchText))
            return false;
        if (tenant == null) {
            if (other.tenant != null)
                return false;
        } else if (!tenant.equals(other.tenant))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AgentLogRequest [dateFrom=" + dateFrom + ", dateTo=" + dateTo + ", maximumLines=" + maximumLines + ", file=" + file
                + ", searchText=" + searchText + ", tenant=" + tenant + ", deviceUser=" + deviceUser + "]";
    }
    
}
