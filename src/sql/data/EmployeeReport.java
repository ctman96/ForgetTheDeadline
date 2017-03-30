package sql.data;

import java.math.BigDecimal;

public class EmployeeReport {
    private String eid;
    private int count;
    private BigDecimal sum;

    public EmployeeReport(String eid,  int count, BigDecimal sum) {
        this.eid = eid;
        this.count = count;
        this.sum = sum;
    }

    public String getId() {
        return eid;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getSum(){return sum;}
}
