package dev.dashboard.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the STORIES database table.
 * 
 */
@Entity
@Table(name="STORIES")
@NamedQuery(name="Story.findAll", query="SELECT s FROM Story s")
public class Story implements Serializable {
	private static final long serialVersionUID = 1L;

	private String additionaleffort;

	private String blocks;

	@Column(name="DEVELOPMENT_SEQUENCE")
	private String developmentSequence;

	private Double estimate;
	@Id
	private Long id;

	private String iteration;

	private String maincomponent;

	@Column(name="OWNED_BY")
	private String ownedBy;

	private String parent;

	@Column(name="PL_DEV")
	private Long plDev;

	@Column(name="PL_SPRINT")
	private String plSprint;

	private String sprint;

	private String status;

	private String summary;

	@Column(name="\"TYPE\"")
	private String type;

	@Column(name="USER_STORY_TECHNICAL_OWNER")
	private String userStoryTechnicalOwner;

	public Story() {
	}

	public String getAdditionaleffort() {
		return this.additionaleffort;
	}

	public void setAdditionaleffort(String additionaleffort) {
		this.additionaleffort = additionaleffort;
	}

	public String getBlocks() {
		return this.blocks;
	}

	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}

	public String getDevelopmentSequence() {
		return this.developmentSequence;
	}

	public void setDevelopmentSequence(String developmentSequence) {
		this.developmentSequence = developmentSequence;
	}

	public Double getEstimate() {
		return this.estimate;
	}

	public void setEstimate(Double estimate) {
		this.estimate = estimate;
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

	public String getMaincomponent() {
		return this.maincomponent;
	}

	public void setMaincomponent(String maincomponent) {
		this.maincomponent = maincomponent;
	}

	public String getOwnedBy() {
		return this.ownedBy;
	}

	public void setOwnedBy(String ownedBy) {
		this.ownedBy = ownedBy;
	}

	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Long getPlDev() {
		return this.plDev;
	}

	public void setPlDev(Long plDev) {
		this.plDev = plDev;
	}

	public String getPlSprint() {
		return this.plSprint;
	}

	public void setPlSprint(String plSprint) {
		this.plSprint = plSprint;
	}

	public String getSprint() {
		return this.sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserStoryTechnicalOwner() {
		return this.userStoryTechnicalOwner;
	}

	public void setUserStoryTechnicalOwner(String userStoryTechnicalOwner) {
		this.userStoryTechnicalOwner = userStoryTechnicalOwner;
	}

}