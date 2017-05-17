package br.com.framework.implementacao.crud;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.interfac.crud.InterfaceCrud;

@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static SessionFactory sessionFactory = HibernateUtil.getSessionfactory();
	
	@Autowired
	private JdbcTemplateImpl jdbcTemplate;
	
	@Autowired
	private SimpleJdbcTemplateImpl simpleJdbcTemplate;
	
	@Autowired
	private SimpleJdbcInsertImplents simpleJdbcInsert;
	
	@Autowired
	private SimpleJdbcTemplateImpl simpleJdbcTemplateImpl;
	
	@SuppressWarnings("unused")
	private SimpleJdbcTemplate getSimpleJdbcTemplateImpl() {
		return simpleJdbcTemplateImpl;

	}
	
	@Override
	public void save(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFluhSession();
	}

	@Override
	public void persist(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFluhSession();
	}

	@Override
	public void saveOrUpdate(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFluhSession();
	}

	@Override
	public void update(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().update(obj);
		executeFluhSession();
	}

	@Override
	public void delete(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFluhSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T obj) throws Exception {
		validaSessionFactory();
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFluhSession();
		return obj;
	}

	@Override
	public List<T> findList(Class<T> entidade) throws Exception {
		validaSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append("select distinct (entity) from ").append(entidade.getSimpleName()).append(" entity ");
		
		@SuppressWarnings("unchecked")
		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		
		return lista;
	}

	@Override
	public Object findById(Class<T> entidade, Long id) throws Exception {
		validaSessionFactory();
		Object obj = sessionFactory.getCurrentSession().load(getClass(), id);
		
		return obj;
	}

	@Override
	public T findByPorId(Class<T> entidade, Long id) throws Exception {
		validaSessionFactory();
		@SuppressWarnings("unchecked")
		T obj = (T) sessionFactory.getCurrentSession().load(getClass(), id);
		
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListByQueryDinamica(String s) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(s).list();
		return lista;
	}

	@Override
	public void executeUpdateQueryDinamica(String s) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();
		executeFluhSession();
		
	}

	@Override
	public void executeUdateSQLDinamica(String s) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();
		executeFluhSession();
		
	}

	@Override
	public void clearSession() throws Exception {
		sessionFactory.getCurrentSession().clear();
		
	}

	@Override
	public void evit(Object objs) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().evict(objs);
		
	}

	@Override
	public Session getSession() throws Exception {
		validaSessionFactory();
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<?> getListSQLDinamica(String sql) throws Exception {
		validaSessionFactory();
		List<?> lista = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return lista;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJDBCTemplate() {
		return simpleJdbcTemplate;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcInsert;
	}

	@Override
	public Long totalRegistro(String table) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(1) from ").append(table);
		return jdbcTemplate.queryForLong(sql.toString());
	}

	@Override
	public Query obterQuery(String query) throws Exception {
		validaSessionFactory();
		Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());
		return queryReturn;
	}

	/**
	 * Realiza consulta a un banco de datos, iniciando desde un registro pasado por 
	 * parámetro -> iniciaNoRegistro y obteniendo el máximo de resultados
	 * en -> maximoResultado
	 * 
	 * @param query
	 * @param iniciaNoRegistro
	 * @param maximoResultado
	 * @return List<T>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findlistbyQueryDinamia(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		validaSessionFactory();
		List<T> list = new ArrayList<T>();
		list = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(iniciaNoRegistro).setMaxResults(maximoResultado).list();
		return list;
	}
	
	private void validaSessionFactory(){
		if (sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionfactory();
			
		}
		
		validaTransaction();
	}
	
	private void validaTransaction(){
		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}
	
	@SuppressWarnings("unused")
	private void commitProcessoAjax (){
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}
	
	@SuppressWarnings("unused")
	private void rollBackProcesoAjax(){
		sessionFactory.getCurrentSession().beginTransaction().rollback();
	}
	
	/**
	 * Ejecuta al instante SQL en la base de datos
	 */
	private void executeFluhSession(){
		sessionFactory.getCurrentSession().flush();
		
	}
	
	List<Object[]> getListSQLDinamicaArray(String sql) throws Exception {
		validaSessionFactory();
		@SuppressWarnings("unchecked")
		List<Object[]> lista = (List<Object[]>) sessionFactory.getCurrentSession().createQuery(sql).list();
		return lista;
	}

}
