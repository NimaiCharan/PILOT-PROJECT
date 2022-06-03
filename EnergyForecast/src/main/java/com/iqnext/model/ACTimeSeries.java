package com.iqnext.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "iq_ac_time_Series")
public class ACTimeSeries {
	
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String date;
	private String time;
	private int ac_mode;
	private String ac_Id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAc_Id() {
		return ac_Id;
	}
	public void setAc_Id(String ac_Id) {
		this.ac_Id = ac_Id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getAc_mode() {
		return ac_mode;
	}
	public void setAc_mode(int ac_mode) {
		this.ac_mode = ac_mode;
	}
	
	

}
