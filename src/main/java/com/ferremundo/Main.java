package com.ferremundo;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
	private static final Logger logger = Logger.getLogger("com.ferremundo.greeting");
	private static final String PERSISTENCE_UNIT_NAME = "com.ferremundo";
	private EntityManagerFactory emf;
	private EntityManager em;

	public static void main(String[] args) {
		EntityManagerFactory emf= Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		for(int i=0;i<10;i++){
			Greeting g=new Greeting();
			g.setMessage("greeting : "+i);
			em.persist(g);
		}
		List<Greeting> lg=em.createNativeQuery("select * from Greeting",Greeting.class).getResultList();
		for (int i=0;i<lg.size();i++) {
			
			Greeting greeting = lg.get(i);
			System.out.println(greeting.toString());
		}
		em.getTransaction().commit();
		/*Main main = new Main();
		main.initEntityManager();
		main.createAndRead();
		main.createAndRollback();
		*/
	}

	private void createAndRead() {
		Greeting g = new Greeting();
		g.setId(1L);
		g.setMessage("hello, createAndRead");
		em.getTransaction().begin();
		em.persist(g);
		em.getTransaction().commit();

		// g should be written to database now.
		// Read it from db (no transaction context needed for em.find method)
		Greeting g2 = em.find(Greeting.class, g.getId());
		logger.info("Greeting " + g.getId() + " from db: " + g2);
	}

	private void createAndRollback() {
		Greeting g = new Greeting();
		g.setId(2L);
		g.setMessage("hello, createAndRollback");
		em.getTransaction().begin();
		em.persist(g);
		em.getTransaction().rollback();

		logger
				.info("Persisted " + g
						+ ", but the transaction was rolled back.");
		Greeting g2 = em.find(Greeting.class, g.getId());
		logger.info("Greeting " + g.getId() + " from db: " + g2); // should be
																	// null
	}

	private void initEntityManager() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		em = emf.createEntityManager();
	}
}