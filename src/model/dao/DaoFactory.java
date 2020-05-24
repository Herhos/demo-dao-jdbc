package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

/* A classe DaoFactory vai expor um m�todo que retorna
 * o tipo da interface (SellerDao), mas internamente vai
 * instanciar uma implementa��o  */

public class DaoFactory
{
	public static SellerDao createSellerDao()
	{
		return new SellerDaoJDBC(DB.getConnection());
	}
}
