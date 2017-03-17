package data;

import java.util.Date;

public interface IMembership {
    public int getPoints();
    public Date getIssueDate();
    public Date getExpireDate();
    public ICustomer getCustomer(); // Primary Key, Foreign Key
}
