package data;

public interface IStock {
    public IBranch getBranch(); // Primary Key, Foreign Key
    public IProduct getProduct(); // Primary Key, Foreign Key
    public int getQuantity();
    public int getMaxQuantity();
}
