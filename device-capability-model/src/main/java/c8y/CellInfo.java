package c8y;

import java.util.List;

import org.svenson.JSONTypeHint;

public class CellInfo {

    private String radioType;
    private List<CellTower> cellTowers;

    public CellInfo() {

    }

    public CellInfo(String radioType, List<CellTower> cellTowers) {
        this.radioType = radioType;
        this.cellTowers = cellTowers;
    }

    public String getRadioType() {
        return radioType;
    }

    public void setRadioType(String radioType) {
        this.radioType = radioType;
    }

    public List<CellTower> getCellTowers() {
        return cellTowers;
    }

    @JSONTypeHint(CellTower.class)
    public void setCellTowers(List<CellTower> cellTowers) {
        this.cellTowers = cellTowers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellTowers == null) ? 0 : cellTowers.hashCode());
        result = prime * result + ((radioType == null) ? 0 : radioType.hashCode());
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
        CellInfo other = (CellInfo) obj;
        if (cellTowers == null) {
            if (other.cellTowers != null)
                return false;
        } else if (!cellTowers.equals(other.cellTowers))
            return false;
        if (radioType == null) {
            if (other.radioType != null)
                return false;
        } else if (!radioType.equals(other.radioType))
            return false;
        return true;
    }

}
