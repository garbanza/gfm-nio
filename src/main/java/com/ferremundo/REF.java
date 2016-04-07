package com.ferremundo;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class REF {
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Basic
	private int nexty;
	
	public static int next() {
     	EntityManager em=EMF.get(EMF.UNIT_REF).createEntityManager();
     	em.getTransaction().begin();
     	List<REF> list=em.createNativeQuery("select * from REF",REF.class).getResultList();
     	
		int next=list.get(0).nexty+1;
		list.get(0).setActual(next);
		em.getTransaction().commit();

	    em.close();

		return next;
	}
	
	public static String next36() {
		return BaseConverter.toBase36(next());
	}
	
	public int getActual() {
		return nexty;
	}
	
	public void setActual(int next) {
		this.nexty = next;
	}
	
	public Long getId() {
		return id;
	}
	
	private static final String basedigits="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	

	public static void main(String[] args) {
		int toSet=30000;
		EntityManager em=EMF.get(EMF.UNIT_REF).createEntityManager();
     	em.getTransaction().begin();
     	REF ref=new REF();
     	ref.setActual(toSet);
     	em.persist(ref);
     	em.getTransaction().commit();
	    em.close();
	    
		//System.out.println(BaseConverter.toBase36(toSet));
		/*for (int i = 0; true; i++) {
			if(BaseConverter.toBase36(i).equals("F8K")){System.out.println(i);break;}
			//System.out.println();
		}*/
	    
	}
	
	
	
}
