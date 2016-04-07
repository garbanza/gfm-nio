package com.ferremundo;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InvoiceSerial {
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Basic private int serial;
	
	public static int next(){
     	EntityManager em=EMF.get(EMF.UNIT_INVOICE_SERIAL).createEntityManager();
     	em.getTransaction().begin();
     	List<InvoiceSerial> list=em.createNativeQuery("select * from InvoiceSerial",InvoiceSerial.class).getResultList();
     	
		int next=list.get(0).serial+1;
		list.get(0).setSerial(next);
		em.getTransaction().commit();

	    em.close();

		return next;
	}
	
	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}
	
	public static void main(String[] args) {
		int toSet=12965;
		EntityManager em=EMF.get(EMF.UNIT_INVOICE_SERIAL).createEntityManager();
     	em.getTransaction().begin();
     	InvoiceSerial serial=new InvoiceSerial();
     	serial.setSerial(toSet);
     	em.persist(serial);
     	em.getTransaction().commit();
	    em.close();
	    
	}
}
