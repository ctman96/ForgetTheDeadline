package data;

import java.sql.Date;

public interface IMembership {
    int getPoints();
    Date getIssueDate();
    Date getExpireDate();
    ICustomer getCustomer(); // Primary Key, Foreign Key
}
