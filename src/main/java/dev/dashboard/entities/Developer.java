package dev.dashboard.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the DEVELOPERS database table.
 * 
 */
@Entity
@Table(name="DEVELOPERS")
@NamedQuery(name="Developer.findAll", query="SELECT d FROM Developer d")
public class Developer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="DEVLEOPERS_GEN" , strategy="increment")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "DEVLEOPERS_GEN")
	@SequenceGenerator(name = "DEVLEOPERS_GEN", sequenceName = "DEVLEOPERS_SEQ")
	private long id;

	private String name;

	protected Developer() {
	}
	
	private Developer (String name) {
		this.name = name;
	}
	private Developer (Long id) {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Developer [id=" + id + ", name=" + name + "]";
	}
	
	public static DeveloperBuilder builder() {
        return new DeveloperBuilder();
    }
	
	public static class DeveloperBuilder {
		
		private String nameb;
		private long idb;
		
		public DeveloperBuilder setDeveloperName (String name) {
			this.nameb = name;
			return this;
		}
		public DeveloperBuilder setDeveloperId (Long id) {
			this.idb = id;
			return this;
		}
		  public Developer build() {
	            return new Developer(nameb);
	        }
		  public Developer buildIDonly() {
	            return new Developer(idb);
	        }
		
		
	}
	

}