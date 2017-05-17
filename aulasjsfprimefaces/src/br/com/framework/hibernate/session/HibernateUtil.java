package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;

/**
 * Responsable de establecer la conexión con hibernate
 * @author Luis
 *
 */

@ApplicationScoped
public class HibernateUtil implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	
	/**
	 * Responsable del archivo de configuración hibernate.cfg.xml
	 * @return SessionFactory
	 */
	private static SessionFactory buildSessionFactory(){
		try{
			if(sessionFactory == null){
				sessionFactory = new Configuration().configure().buildSessionFactory();
				
			}
			
			return sessionFactory;
			
		}catch (Exception e){
			e.printStackTrace();
			throw new ExceptionInInitializerError("error al crear la conexion");
		}
		
	}
	
	/**
	 *  Retorna un SessionFactory
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionfactory(){
		return sessionFactory;
	}
	
	/**
	 * Retorna una SessionFactory
	 * @return Session
	 */
	public static Session getCurrentSession(){		
		return getSessionfactory().getCurrentSession();
		
	}
	
	/**
	 * Abre una nueva SessionFactory
	 * return Session
	 */
	public static Session openSession(){
		if(sessionFactory == null){
			buildSessionFactory();
			
		}
		
		return sessionFactory.openSession();
	}
	
	
	/**
	 * Obtiene una conexión con el proveedor de configuración de conexiones
	 * @return Conexión SQL
	 * @throws SQLException
	 */
	public static Connection getConnectionProvider () throws SQLException {
		
		return ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
		
	}
	
	/**
	 * 
	 * @return conexión al contexto inicial java:/comp/env/jdbc/datasource
	 * @throws Exception
	 */
	public static Connection getConnection () throws Exception {
		InitialContext context = new InitialContext();
		
		DataSource ds = (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		return ds.getConnection();
		
	}
	
	/**
	 * 
	 * @return DataSource JNDI Tomcat
	 * @throws NamingException
	 */
	public DataSource getDataSourceJndi () throws NamingException {
		InitialContext context = new InitialContext();
		
		return (DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);
	}

}