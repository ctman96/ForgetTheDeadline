package sql.data;

import java.math.BigDecimal;

public class AggregateBranchReport {
    private String sku;
    private BigDecimal num;

    public AggregateBranchReport(String sku, BigDecimal num) {
        this.sku = sku;
        this.num = num;
    }

    public String getSku(){ return sku;}

    public BigDecimal getNum() {
        return num;
    }
}
