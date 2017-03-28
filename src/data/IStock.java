package data;

public interface IStock {
    IBranch getBranch(); // Primary Key, Foreign Key
    IProduct getProduct(); // Primary Key, Foreign Key
    int getQuantity();
    int getMaxQuantity();
}
