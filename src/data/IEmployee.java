package data;

import java.math.BigDecimal;

public interface IEmployee {
    String getId(); // Primary Key
    String getName();
    String getAddress();
    String getPhone();
    BigDecimal getWage();
    String getPositionName();
    IBranch getBranch(); // Foreign Key
}
