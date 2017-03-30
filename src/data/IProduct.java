package data;

import java.math.BigDecimal;

public interface IProduct {
    String getSKU(); // Primary Key
    String getName();
    BigDecimal getPrice();
    IDeveloper getDeveloper(); // Foreign Key
}
