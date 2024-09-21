import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);


        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.getById(3);
        System.out.println(seller);

        System.out.println();

        System.out.println("=== TEST 2: seller findByDepartment ===");
        Department department = new Department(2, "Eletronics");
        List<Seller> list = sellerDao.findByDepartment(department);
        for (Seller s : list) {
            System.out.println(s);
        }

        System.out.println();

        System.out.println("=== TEST 3: seller finAll ===");
        list = sellerDao.findAll();
        for (Seller s : list) {
            System.out.println(s);
        }

        System.out.println();

        System.out.println("=== TEST 4: seller insert ===");
        Seller newSeller = new Seller(null, "Davidson", "Davidson@gmail.com", new Date(), 4000.0, department);
       sellerDao.insert(newSeller);
       System.out.println("Inserted! New id = " + newSeller.getId());

       System.out.println("=== TEST 5: seller update ===");
       seller = sellerDao.getById(1); // selecionando qual vendedor por Id
       seller.setName("Marcelo");
        seller.setEmail("Marcelo@gmail.com"); // setando o novo nomeseller.
       sellerDao.update(seller);
       System.out.println("Updated! New id = " + seller.getId());

        System.out.println("=== TEST 5: seller update ===");
        System.out.println("Enter Id for delete: ");
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Delete completed");
        sc.close();
    }
}