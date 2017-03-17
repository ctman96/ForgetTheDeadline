package data;

public interface IProduct {
    public String getSKU(); // Primary Key
    public String getName();
    public double getPrice();
    public IDistributor getDistributor(); // Foreign Key
}
