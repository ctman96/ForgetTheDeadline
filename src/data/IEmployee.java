package data;

public interface IEmployee {
    public String getId(); // Primary Key
    public String getName();
    public String getAddress();
    public String getPhone();
    public double getWage();
    public String getPositionName();
    public IBranch getBranch(); // Foreign Key
}
