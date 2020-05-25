package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program
{
	public static void main(String[] args)
	{
		Department obj = new Department(1, "Books");
		System.out.println(obj);
		System.out.println();
		
		Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.00, obj);
		System.out.println(seller);
		System.out.println();
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		Seller seller1 = sellerDao.findById(3);
		System.out.println("=== Teste do findById ===");
		System.out.println(seller1);
		System.out.println();
		
		System.out.println("=== Teste do findByDepartment ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj2 : list)
		{
			System.out.println(obj2);
		}
	}

}
