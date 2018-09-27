package dev.dashboard.entities;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;



/**
 * The persistent class for the TASKS database table.
 * 
 */
@Entity
@Table(name="TASKS")
@NamedQuery(name="Task.findAll", query="SELECT t FROM Task t")
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	private Double estimateh;
	@Id
	private Long id;

	private String iteration;

	private String parent;

	private String status;

	private String summary;

	private Double timespend;

	@Column(name="\"TYPE\"")
	private String type;

	public Task() {
	}

	public Double getEstimateh() {
		return this.estimateh;
	}

	public void setEstimateh(Double estimateh) {
		this.estimateh = estimateh;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIteration() {
		return this.iteration;
	}

	public void setIteration(String iteration) {
		this.iteration = iteration;
	}

	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Double getTimespend() {
		return this.timespend;
	}

	public void setTimespend(Double timespend) {
		this.timespend = timespend;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}