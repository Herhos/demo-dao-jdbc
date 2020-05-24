package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB
{
	
	// Declaração de objeto do JDBC do tipo Connection 	 
	private static Connection conn = null;
	
	/* Método para conectar com o banco de dados */
	public static Connection getConnection()
	{
		if (conn == null)
		{
			try
			{
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			}
			catch (SQLException e)
			{
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	
	/* Método para desconectar do banco de dados */
	public static Connection closeConnection()
	{
		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	
	/* Método para carregar as informações contidas no arquivo
	 * db.properties */
	private static Properties loadProperties()
	{
		try (FileInputStream fs = new FileInputStream("db.properties"))
		{
			Properties props = new Properties();
			/* O método load faz a leitura do arquivo properties, apontado
			 * pelo InputStream fs e guarda os dados dentro do objeto props */
			props.load(fs);
			return props;
		}
		catch (IOException e)
		{
			throw new DbException(e.getMessage());
		}
	}
	
	/* Método para fechar um Statement */
	public static void closeStatement(Statement st)
	{
		if (st != null)
		{
			try
			{
				st.close();
			}
			catch (SQLException e)
			{
				throw new DbException(e.getMessage());
			}
		}
	}
	
	/* Método para fechar um ResultSet */
	public static void closeResultSet(ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (SQLException e)
			{
				throw new DbException(e.getMessage());
			}
		}
	}
}
