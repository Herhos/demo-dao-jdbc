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
		System.out.println();
		
		System.out.println("=== Teste do findAll ===");
		list = sellerDao.findAll();
		for (Seller obj3 : list)
		{
			System.out.println(obj3);
		}
		System.out.println();
		
		System.out.println("=== Teste do insert ===");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.00, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserido! Novo id: " + newSeller.getId());
		System.out.println();
		
		System.out.println("=== Teste do update ===");
		seller = sellerDao.findById(1);
		seller.setName("Martha Wayne");
		sellerDao.update(seller);
		System.out.println("Atualização efetuada!");
		System.out.println();
	}

}
