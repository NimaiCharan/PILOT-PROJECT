package com.iqnext.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "iq_ac_details")
public class ACDetails {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String ac_id;
	private String ac_make;
	private int ac_manufactured_year;
	private String ac_type;
	private float ac_capacity;
	private int ac_energy_rating;
	private int ac_cooling_capacity ;
	private int ac_power_consumption;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAc_id() {
		return ac_id;
	}
	public void setAc_id(String ac_id) {
		this.ac_id = ac_id;
	}
	public String getAc_make() {
		return ac_make;
	}
	public void setAc_make(String ac_make) {
		this.ac_make = ac_make;
	}
	public int getAc_manufactured_year() {
		return ac_manufactured_year;
	}
	public void setAc_manufactured_year(int ac_manufactured_year) {
		this.ac_manufactured_year = ac_manufactured_year;
	}
	public String getAc_type() {
		return ac_type;
	}
	public void setAc_type(String ac_type) {
		this.ac_type = ac_type;
	}
	public float getAc_capacity() {
		return ac_capacity;
	}
	public void setAc_capacity(float ac_capacity) {
		this.ac_capacity = ac_capacity;
	}
	public int getAc_energy_rating() {
		return ac_energy_rating;
	}
	public void setAc_energy_rating(int ac_energy_rating) {
		this.ac_energy_rating = ac_energy_rating;
	}
	public int getAc_cooling_capacity() {
		return ac_cooling_capacity;
	}
	public void setAc_cooling_capacity(int ac_cooling_capacity) {
		this.ac_cooling_capacity = ac_cooling_capacity;
	}
	public int getAc_power_consumption() {
		return ac_power_consumption;
	}
	public void setAc_power_consumption(int ac_power_consumption) {
		this.ac_power_consumption = ac_power_consumption;
	}
	

}
