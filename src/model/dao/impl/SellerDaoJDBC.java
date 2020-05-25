package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao
{
	// Dependência do DAO com a conexão
	private Connection conn;
	
	// Para forçar uma injeção de dependência
	public SellerDaoJDBC(Connection conn)
	{
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj)
	{
		PreparedStatement st = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"INSERT INTO seller " +
					"(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
					"VALUES	(?, ?, ?, ?, ?)", // ? -> chama-se placeholder
					Statement.RETURN_GENERATED_KEYS
				);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0)
			{
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next())
				{
					int id = rs.getInt(1); // 1ª coluna de getGeneratedKeys
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else
			{
				throw new DbException("Erro inexperado! Nenhuma linha afetada!");
			}
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id)
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"SELECT seller.*,department.Name as DepName " +
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " +
					"WHERE seller.Id = ?"
				);
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			/* O ResultSet retorna a consulta SQL no formato de tabela, mas como estamos
			 * programando com orientação a objetos, a classe DAO fica responsável por 
			 * transformar os dados do BD relacional em objetos associados */
			
			if(rs.next())
			{
				Department dep = instantiateDepartment(rs);				
				Seller obj = instantiateSeller(rs, dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}	

	// METODO AUXILIAR PARA INSTANCIAR O DEPARTMENT DE findById
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException
	{
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}
	
	// METODO AUXILIAR PARA INSTANCIAR O SELLER DE findById
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException
	{
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartment(dep); // Objeto Department já montado
		return obj;
	}

	@Override
	public List<Seller> findAll()
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"SELECT seller.*,department.Name as DepName " +
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " +
					"ORDER BY Name"
				);
			
			rs = st.executeQuery();
			
			/* O ResultSet retorna a consulta SQL no formato de tabela, mas como estamos
			 * programando com orientação a objetos, a classe DAO fica responsável por 
			 * transformar os dados do BD relacional em objetos associados */
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next())
			{
				/* Se o departamento já existir, o map.get vai capturá-lo;
				 * o if será falso e o dep será reaproveitado. Se o departamento
				 * não existir, map.get retorna nulo para a variável dep, o if será
				 * verdadeiro, vai instanciar e salvar o departamento no dep */
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null)
				{
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department)
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"SELECT seller.*,department.Name as DepName " +
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " +
					"WHERE DepartmentId = ? ORDER BY Name"
				);
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			/* O ResultSet retorna a consulta SQL no formato de tabela, mas como estamos
			 * programando com orientação a objetos, a classe DAO fica responsável por 
			 * transformar os dados do BD relacional em objetos associados */
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next())
			{
				/* Se o departamento já existir, o map.get vai capturá-lo;
				 * o if será falso e o dep será reaproveitado. Se o departamento
				 * não existir, map.get retorna nulo para a variável dep, o if será
				 * verdadeiro, vai instanciar e salvar o departamento no dep */
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null)
				{
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}	
}
