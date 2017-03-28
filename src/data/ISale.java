package data;

import java.util.Date;

public interface ISale {
    String getSaleNumber(); // Primary Key
    String getPayment();
    Date getDate();
    IProduct getProduct(); // Foreign Key
    ICustomer getCustomer(); // Foreign Key
    IEmployee getEmployee(); // Foreign Key
}
