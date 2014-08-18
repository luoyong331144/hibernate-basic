
#### pom.xml
``` xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>name.luoyong.hibernate</groupId>
	<artifactId>hibernate-basic-example</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>hibernate-basic-example</name>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	</properties>

	<dependencies>
	
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.11</version>
			<scope>test</scope>
		</dependency>
		
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>4.3.6.Final</version>
		</dependency>
		
		<dependency>
    		<groupId>mysql</groupId>
    		<artifactId>mysql-connector-java</artifactId>
    		<version>5.1.6</version>
    	</dependency>
		
	</dependencies>
	
</project>
```

#### hibernate.cfg.xml
``` xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>

    <session-factory>

        <!-- Database connection settings -->
        <property name="connection.driver_class">com.mysql.jdbc.Driver</property>
        <property name="connection.url">jdbc:mysql://yourHost:3306/hibernate</property>
        <property name="connection.username">root</property>
        <property name="connection.password">****</property>

        <!-- JDBC connection pool (use the built-in) -->
        <property name="connection.pool_size">3</property>

        <!-- SQL dialect -->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>

        <!-- Disable the second-level cache  -->
        <property name="cache.provider_class">org.hibernate.cache.internal.NoCacheProvider</property>

        <!-- Echo all executed SQL to stdout -->
        <property name="show_sql">true</property>
        <property name="format_sql">true</property>

        <!-- Drop and re-create the database schema on startup -->
        <property name="hbm2ddl.auto">update</property>

        <mapping class="name.luoyong.hibernate.basic.entity.User"/>
        <mapping class="name.luoyong.hibernate.basic.entity.Group"/>
        
    </session-factory>

</hibernate-configuration>
```

#### java test
``` java
package name.luoyong.hibernate.basic;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import name.luoyong.hibernate.basic.entity.Group;
import name.luoyong.hibernate.basic.entity.User;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;


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
```