package data;

import java.sql.Date;

public interface IMembership {
    public int getPoints();
    public Date getIssueDate();
    public Date getExpireDate();
    public ICustomer getCustomer(); // Primary Key, Foreign Key
}
