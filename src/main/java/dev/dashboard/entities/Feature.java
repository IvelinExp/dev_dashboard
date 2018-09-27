package dev.dashboard.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FEATURES database table.
 * 
 */
@Entity
@Table(name="FEATURES")
@NamedQuery(name="Feature.findAll", query="SELECT f FROM Feature f")
public class Feature implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String blocks;

	private String children;

	private BigDecimal estimate;

	@Column(name="FEATURE_SEQUENCE")
	private BigDecimal featureSequence;

	@Column(name="OWNED_BY")
	private String ownedBy;

	private BigDecimal parent;

	private String status;

	private String summary;

	public Feature() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBlocks() {
		return this.blocks;
	}

	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}

	public String getChildren() {
		return this.children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public BigDecimal getEstimate() {
		return this.estimate;
	}

	public void setEstimate(BigDecimal estimate) {
		this.estimate = estimate;
	}

	public BigDecimal getFeatureSequence() {
		return this.featureSequence;
	}

	public void setFeatureSequence(BigDecimal featureSequence) {
		this.featureSequence = featureSequence;
	}

	public String getOwnedBy() {
		return this.ownedBy;
	}

	public void setOwnedBy(String ownedBy) {
		this.ownedBy = ownedBy;
	}

	public BigDecimal getParent() {
		return this.parent;
	}

	public void setParent(BigDecimal parent) {
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

}