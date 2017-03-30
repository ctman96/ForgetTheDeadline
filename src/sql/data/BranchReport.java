package sql.data;

import java.math.BigDecimal;

public class BranchReport {
    private String sku;
    String bid;
    private int count;

    public BranchReport(String sku, String bid, int count) {
        this.sku = sku;
        this.bid = bid;
        this.count = count;
    }

    public String getId() {
        return bid;
    }

    public String getSku(){ return sku;}

    public int getCount() {
        return count;
    }
}
