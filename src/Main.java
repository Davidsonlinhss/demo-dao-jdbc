import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Main {
    public static void main(String[] args) {

        Department obj = new Department(1, "Books");
        Seller seller = new Seller(12, "John Doe", "john@gmail.com", new Date(), 300.0, obj);

        SellerDao sellerDao = DaoFactory.createSellerDao();
    }
}