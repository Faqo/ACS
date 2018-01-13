package alma.acs.tmcdb;
// Generated Jun 5, 2017 7:15:51 PM by Hibernate Tools 4.3.1.Final


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * AssemblyRole generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name="`ASSEMBLYROLE`"
)
public class AssemblyRole extends alma.acs.tmcdb.translator.TmcdbObject implements java.io.Serializable {


     protected String roleName;
     protected AssemblyType assemblyType;
     private Set<AssemblyStartup> assemblyStartups = new HashSet<AssemblyStartup>(0);

    public AssemblyRole() {
    }
   
       @Id 

    
    @Column(name="`ROLENAME`", unique=true, nullable=false, length=128)
    public String getRoleName() {
        return this.roleName;
    }
    
    public void setRoleName(String roleName) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("roleName", this.roleName, this.roleName = roleName);
        else
            this.roleName = roleName;
    }


@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="`ASSEMBLYTYPENAME`", nullable=false)
    public AssemblyType getAssemblyType() {
        return this.assemblyType;
    }
    
    public void setAssemblyType(AssemblyType assemblyType) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("assemblyType", this.assemblyType, this.assemblyType = assemblyType);
        else
            this.assemblyType = assemblyType;
    }


@OneToMany(fetch=FetchType.LAZY, mappedBy="assemblyRole")
    public Set<AssemblyStartup> getAssemblyStartups() {
        return this.assemblyStartups;
    }
    
    public void setAssemblyStartups(Set<AssemblyStartup> assemblyStartups) {    
    	this.assemblyStartups = assemblyStartups;
    }

	public void addAssemblyStartups(Set<AssemblyStartup> elements) {
		if( this.assemblyStartups != null )
			for(Iterator<AssemblyStartup> it = elements.iterator(); it.hasNext(); )
				addAssemblyStartupToAssemblyStartups((AssemblyStartup)it.next());
	}

	public void addAssemblyStartupToAssemblyStartups(AssemblyStartup element) {
		if( !this.assemblyStartups.contains(element) ) {
			this.assemblyStartups.add(element);
		}
	}





}

