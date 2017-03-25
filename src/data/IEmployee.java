package data;

import java.math.BigDecimal;

public interface IEmployee {
    public String getId(); // Primary Key
    public String getName();
    public String getAddress();
    public String getPhone();
    public BigDecimal getWage();
    public String getPositionName();
    public IBranch getBranch(); // Foreign Key
}
