package data;

import java.math.BigDecimal;

public interface IProduct {
    public String getSKU(); // Primary Key
    public String getName();
    public BigDecimal getPrice();
    public IDeveloper getDeveloper(); // Foreign Key
}
