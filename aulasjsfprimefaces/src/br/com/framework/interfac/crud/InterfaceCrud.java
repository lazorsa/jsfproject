package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public interface InterfaceCrud<T> extends Serializable{
	
	// salvar datos
	void save(T obj) throws Exception;
	
	void persist(T obj) throws Exception;
	
	// salvar o actualizar
	void saveOrUpdate (T obj) throws Exception;
	
	// actualización de datos
	void update (T obj) throws Exception; 
	
	// borrado de datos
	void delete (T obj) throws Exception;
	
	// salvar o actualiza y retorna objecto en estado persistente 
	T merge (T obj) throws Exception;
	
	// agrega una lista de datos de determinada clase
	List<T> findList(Class<T> objs) throws Exception;
	
	Object findById(Class<T> entidade, Long id) throws Exception;
	
	T findByPorId(Class<T> entidade, Long id) throws Exception;
	
	List<T> findListByQueryDinamica(String s) throws Exception;
	
	// ejecuta  actualizar con HQL
	void executeUpdateQueryDinamica(String s) throws Exception;
	
	// ejecuta actualizar con HQL
	void executeUdateSQLDinamica(String s) throws Exception;
	
	// limpia la session de hibernate
	void clearSession() throws Exception;
	
	// retira un objeto de session hibernate
	void evit (Object objs) throws Exception;
	
	Session getSession () throws Exception;
	
	List<?> getListSQLDinamica(String sql) throws Exception;
	
	JdbcTemplate getJdbcTemplate();
	
	SimpleJdbcTemplate getSimpleJDBCTemplate();
	
	SimpleJdbcInsert getSimpleJdbcInsert();
	
	Long totalRegistro(String table) throws Exception;
	
	Query obterQuery(String query) throws Exception;
	
//	List<Object[]> getListSQLDinamicaArray(String sql) throws Exception;
	
	// Carga dinámica de JSF and Primefaces
	List<T> findlistbyQueryDinamia(String query, int iniciaNoRegistro, int maximoResultado) throws Exception;
	
	
}
















