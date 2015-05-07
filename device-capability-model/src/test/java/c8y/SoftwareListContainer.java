package c8y;

import org.svenson.JSONTypeHint;

public class SoftwareListContainer {
    private SoftwareList c8y_SoftwareList;

    public SoftwareList getC8y_SoftwareList() {
        return c8y_SoftwareList;
    }

    @JSONTypeHint(SoftwareItem.class)
    public void setC8y_SoftwareList(SoftwareList c8y_SoftwareList) {
        this.c8y_SoftwareList = c8y_SoftwareList;
    }

}
