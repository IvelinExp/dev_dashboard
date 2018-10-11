package dev.dashboard.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the SPRINTS database table.
 * 
 */
@Entity
@Table(name="SPRINTS")
@NamedQuery(name="Sprint.findAll", query="SELECT s FROM Sprint s")
public class Sprint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String sprint;

	@Column(name="ACT_EFFORT")
	private double actEffort;

	@Column(name="ACTUAL__FOCUS_FACTOR")
	private double actualFocusFactor;

	private double capacity;

	private double developers;

	@Column(name="DEVELOPMENT_DAYS")
	private BigDecimal developmentDays;

	@Temporal(TemporalType.DATE)
	@Column(name="DEVELOPMENT_START")
	private Date developmentStart;

	@Temporal(TemporalType.DATE)
	@Column(name="DEVELOPMENT_STOP")
	private Date developmentStop;

	@Column(name="TARGET_FOCUS_FACTOR")
	private double targetFocusFactor;

	@Column(name="VACATION_SICKNESS")
	private String vacationSickness;

	public Sprint() {
	}

	public String getSprint() {
		return this.sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
	}

	public double getActEffort() {
		return this.actEffort;
	}

	public void setActEffort(double actEffort) {
		this.actEffort = actEffort;
	}

	public double getActualFocusFactor() {
		return this.actualFocusFactor;
	}

	public void setActualFocusFactor(double actualFocusFactor) {
		this.actualFocusFactor = actualFocusFactor;
	}

	public double getCapacity() {
		return this.capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getDevelopers() {
		return this.developers;
	}

	public void setDevelopers(double developers) {
		this.developers = developers;
	}

	public BigDecimal getDevelopmentDays() {
		return this.developmentDays;
	}

	public void setDevelopmentDays(BigDecimal developmentDays) {
		this.developmentDays = developmentDays;
	}

	public Date getDevelopmentStart() {
		return this.developmentStart;
	}

	public void setDevelopmentStart(Date developmentStart) {
		this.developmentStart = developmentStart;
	}

	public Date getDevelopmentStop() {
		return this.developmentStop;
	}

	public void setDevelopmentStop(Date developmentStop) {
		this.developmentStop = developmentStop;
	}

	public double getTargetFocusFactor() {
		return this.targetFocusFactor;
	}

	public void setTargetFocusFactor(double targetFocusFactor) {
		this.targetFocusFactor = targetFocusFactor;
	}

	public String getVacationSickness() {
		return this.vacationSickness;
	}

	public void setVacationSickness(String vacationSickness) {
		this.vacationSickness = vacationSickness;
	}

}