package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao
{
	// Dependência do DAO com a conexão
		private Connection conn;
		
	// Para forçar uma injeção de dependência
	public DepartmentDaoJDBC(Connection conn)
	{
		this.conn = conn;
	}

	// MÉTODO DE INSERÇÃO DE DADOS
	
	@Override
	public void insert(Department obj)
	{
		PreparedStatement st = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"INSERT INTO department " +
					"(Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS
				);
			
			st.setString(1, obj.getName());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0)
			{
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next())
				{
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs); // Faltou no gabarito
			}
			else
			{
				throw new DbException("Erro inexperado! Nenhuma linha" +
					"afetada!");
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
	} // CONFERIDO - CORRETO

	// MÉTODO DE ATUALIZAÇÃO DE DADOS
	
	@Override
	public void update(Department obj)
	{
		PreparedStatement st = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"UPDATE department " +
					"SET Name = ? WHERE Id = ?" // referente ao Id do departamento
				);
			
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
		}		
	} // CONFERIDO - CORRETO

	// MÉTODO DE EXCLUSÃO DE DADOS
	
	@Override
	public void deleteById(Integer id)
	{
		PreparedStatement st = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"DELETE FROM department WHERE Id = ?"
				);
			
			st.setInt(1, id);			
			st.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DbIntegrityException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
		}		
	} // CONFERIDO - CORRETO

	// MÉTODO PARA BUSCA DE DEPARTAMENTO PELO ID
	
	@Override
	public Department findById(Integer id)
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"SELECT department.* FROM department WHERE Id = ?"
				);
			
			st.setInt(1, id);			
			rs = st.executeQuery();
			
			if (rs.next())
			{
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
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
	} // CONFERIDO - CORRETO
	
	// MÉTODO PARA LISTAR TODOS OS DEPARTAMENTOS
	
	@Override
	public List<Department> findAll()
	{
		PreparedStatement st = null;		
		ResultSet rs = null;
		
		try
		{
			st = conn.prepareStatement
				(
					"SELECT * FROM department ORDER BY Name"
				);
			
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<>();
			
			while (rs.next())
			{
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
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
	} // CONFERIDO - CORRETO
}
