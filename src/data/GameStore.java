package data;

import sql.GameStoreDB;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameStore {
    public List<IBranch> branch;
    public List<ICustomer> customer;
    public List<IDeveloper> developer;
    public List<IEmployee> employee;
    public List<IProduct> product;
    public List<ISale> sale;
    public List<IStock> stock;

    static public GameStore getGameStore() throws SQLException {
        GameStore gameStore = new GameStore();
        gameStore.branch = GameStoreDB.getBranch();
        gameStore.customer = GameStoreDB.getCustomer();
        gameStore.developer = GameStoreDB.getDeveloper();

        Map<String, IDeveloper> idDeveloperMap = new HashMap<>();
        for (IDeveloper developer : gameStore.developer) {
            idDeveloperMap.put(developer.getId(), developer);
        }

        Map<String, ICustomer> idCustomerMap = new HashMap<>();
        for (ICustomer customer : gameStore.customer) {
            idCustomerMap.put(customer.getId(), customer);
        }

        Map<String, IBranch> idBranchMap = new HashMap<>();
        for (IBranch branch : gameStore.branch) {
            idBranchMap.put(branch.getId(), branch);
        }

        gameStore.employee = GameStoreDB.getEmployee(idBranchMap);
        gameStore.product = GameStoreDB.getProduct(idDeveloperMap);

        Map<String, IEmployee> idEmployeeMap = new HashMap<>();
        for (IEmployee employee : gameStore.employee) {
            idEmployeeMap.put(employee.getId(), employee);
        }

        Map<String, IProduct> idProductMap = new HashMap<>();
        for (IProduct product : gameStore.product) {
            idProductMap.put(product.getSKU(), product);
        }

        gameStore.sale = GameStoreDB.getSale(idProductMap, idCustomerMap, idEmployeeMap);
        gameStore.stock = GameStoreDB.getStock(idProductMap, idBranchMap);

        return gameStore;
    }
}
