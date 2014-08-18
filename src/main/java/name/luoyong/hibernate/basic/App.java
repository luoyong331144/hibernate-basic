package name.luoyong.hibernate.basic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import name.luoyong.hibernate.basic.entity.Group;
import name.luoyong.hibernate.basic.entity.User;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;

/**
 * Hello world!
 * 
 */
public class App {

	private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

	public static void main(String[] args) throws Exception {

		save();
		// get();
		// update();
		// hqlQuery();
		// nativeSQLQuery_EntityResult();
		// nativeSQLQuery_MapResult();

		sessionFactory.close();
	}

	private static void save() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		User ly = new User();
		ly.setUsername("luoyong");
		ly.setPassword("asdfasf");

		Group group = new Group();
		group.setName("第一突击队");

		ly.setGroup(group);
		group.getUsers().add(ly);

		session.save(ly);
		session.save(group);

		session.getTransaction().commit();
		session.close();
	}

	private static void get() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		User user = (User) session.get(User.class, 1L);

		System.out.println("uername : " + user.getUsername());
		System.out.println("password: " + user.getPassword());
		System.out.println("createDate : " + user.getCreateTime());

		session.getTransaction().commit();
		session.close();
	}

	private static void update() throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		User user = (User) session.get(User.class, 1L);

		user.setPassword("security");

		session.getTransaction().commit();
		session.close();
	}

	private static void hqlQuery() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.createQuery("from User");

		// 每次查询一批
		List<User> list = query.list();
		for (User user : list) {
			System.out.println(user.getUsername());
			System.out.println(user.getPassword());
		}

		// 每次查询一个
		Iterator<User> iterator = query.iterate();
		while (iterator.hasNext()) {
			User user = iterator.next();  // 每次查询 一个
			System.out.println(user.getUsername());
			System.out.println(user.getPassword());
		}

		/* query.executeUpdate() 可以执行 select delete update */

		session.getTransaction().commit();
		session.close();
	}

	private static void nativeSQLQuery_EntityResult() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		SQLQuery query = session.createSQLQuery("select * from user2");
		query.addEntity(User.class);
		List<User> list = query.list();
		for (User user : list) {
			System.out.println(user.getUsername());
			System.out.println(user.getPassword());
		}

		session.getTransaction().commit();
		session.close();
	}

	private static void nativeSQLQuery_MapResult() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		SQLQuery query = session.createSQLQuery("select username, password from user2");
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.list();
		for (Map userMap : list) {
			System.out.println(userMap.get("username"));
			System.out.println(userMap.get("password"));
		}

		session.getTransaction().commit();
		session.close();
	}

}
