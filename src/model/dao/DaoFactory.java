package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

/* A classe DaoFactory vai expor um método que retorna
 * o tipo da interface (SellerDao), mas internamente vai
 * instanciar uma implementação  */

public class DaoFactory
{
	public static SellerDao createSellerDao()
	{
		return new SellerDaoJDBC(DB.getConnection());
	}
}
