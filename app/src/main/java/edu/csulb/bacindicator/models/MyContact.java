package edu.csulb.bacindicator.models;

public class MyContact {

	String 	idContact;
	String	name;
	String	number;
	
	
	
	
	public MyContact(String idContact, String name, String number) {
		super();
		this.idContact = idContact;
		this.name = name;
		this.number = number;
	}
	/**
	 * @return the idContact
	 */
	public String getIdContact() {
		return idContact;
	}
	/**
	 * @param idContact the idContact to set
	 */
	public void setIdContact(String idContact) {
		this.idContact = idContact;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	
	
}
