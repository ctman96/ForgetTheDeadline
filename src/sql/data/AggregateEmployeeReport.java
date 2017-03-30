package sql.data;

import java.math.BigDecimal;

public class AggregateEmployeeReport {
    private BigDecimal num;

    public AggregateEmployeeReport(BigDecimal num) {
        this.num = num;
    }

    public BigDecimal getNum(){return num;}
}
