package com.ferremundo;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClientSample implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Basic
	private String code;
	@Basic
	private String consummer;
	@Basic
	private int consummerType;
	@Basic
	private String address;
	@Basic
	private String city;
	@Basic
	private String country;
	@Basic
	private String state;
	@Basic
	private String email;
	@Basic
	private String cp;
	@Basic
	private String rfc;
	@Basic
	private String tel;
	@Basic
	private int payment;//credit days
	
	public ClientSample() {
	}
	
	public ClientSample(String code, String consummer, int consummerType, String address,
			String city, String country, String state, String email, String cp,
			String rfc, String tel, int payment) {
		super();
		this.code=code;
		this.consummer = consummer;
		this.consummerType = consummerType;
		this.address = address;
		this.city = city;
		this.country = country;
		this.state = state;
		this.email = email;
		this.cp = cp;
		this.rfc = rfc;
		this.tel = tel;
		this.payment = payment;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getConsummer() {
		return consummer;
	}
	public void setConsummer(String consummer) {
		this.consummer = consummer;
	}
	public int getConsummerType() {
		return consummerType;
	}
	public void setConsummerType(int consummerType) {
		this.consummerType = consummerType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getPayment() {
		return payment;
	}
	public void setPayment(int payment) {
		this.payment = payment;
	}
	public Long getId() {
		return id;
	}
	
	public static void main(String[] args) {
     	EntityManager em=EMF.get(EMF.UNIT_CLIENT_SAMPLE).createEntityManager();
     	em.getTransaction().begin();
     	
	}

}
