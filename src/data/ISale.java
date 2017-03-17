package data;

import java.util.Date;

public interface ISale {
    public String getSaleNumber(); // Primary Key
    public String getPayment();
    public Date getDate();
    public IProduct getProduct(); // Foreign Key
    public ICustomer getCustomer(); // Foreign Key
    public IEmployee getEmployee(); // Foreign Key
}
