package alma.acs.tmcdb;
// Generated Jun 5, 2017 7:15:51 PM by Hibernate Tools 4.3.1.Final


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * AntennaToPad generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name="`ANTENNATOPAD`"
    , uniqueConstraints =  @UniqueConstraint(columnNames={"`ANTENNAID`", "`PADID`", "`STARTTIME`"})
)
public class AntennaToPad extends alma.acs.tmcdb.translator.TmcdbObject implements java.io.Serializable {


     protected Integer antennaToPadId;
     protected Antenna antenna;
     protected Pad pad;
     protected Long startTime;
     protected Long endTime;
     protected Boolean planned;
     protected Double mountMetrologyAN0Coeff;
     protected Double mountMetrologyAW0Coeff;
     protected Boolean locked;
     protected Boolean increaseVersion;
     protected Integer currentVersion;
     protected String who;
     protected String changeDesc;

    public AntennaToPad() {
    }
   
    @Id @GeneratedValue(generator="generator")
    @GenericGenerator(name="generator", strategy="native",
       parameters = {@Parameter(name="sequence", value="AntennaToPad_seq")}
	)

    
    @Column(name="`ANTENNATOPADID`", unique=true, nullable=false)
    public Integer getAntennaToPadId() {
        return this.antennaToPadId;
    }
    
    public void setAntennaToPadId(Integer antennaToPadId) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("antennaToPadId", this.antennaToPadId, this.antennaToPadId = antennaToPadId);
        else
            this.antennaToPadId = antennaToPadId;
    }


@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="`ANTENNAID`", nullable=false)
    public Antenna getAntenna() {
        return this.antenna;
    }
    
    public void setAntenna(Antenna antenna) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("antenna", this.antenna, this.antenna = antenna);
        else
            this.antenna = antenna;
    }


@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="`PADID`", nullable=false)
    public Pad getPad() {
        return this.pad;
    }
    
    public void setPad(Pad pad) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("pad", this.pad, this.pad = pad);
        else
            this.pad = pad;
    }


    
    @Column(name="`STARTTIME`", nullable=false)
    public Long getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(Long startTime) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("startTime", this.startTime, this.startTime = startTime);
        else
            this.startTime = startTime;
    }


    
    @Column(name="`ENDTIME`")
    public Long getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(Long endTime) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("endTime", this.endTime, this.endTime = endTime);
        else
            this.endTime = endTime;
    }


    
    @Column(name="`PLANNED`", nullable=false)
    public Boolean getPlanned() {
        return this.planned;
    }
    
    public void setPlanned(Boolean planned) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("planned", this.planned, this.planned = planned);
        else
            this.planned = planned;
    }


    
    @Column(name="`MOUNTMETROLOGYAN0COEFF`", precision=64, scale=0)
    public Double getMountMetrologyAN0Coeff() {
        return this.mountMetrologyAN0Coeff;
    }
    
    public void setMountMetrologyAN0Coeff(Double mountMetrologyAN0Coeff) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("mountMetrologyAN0Coeff", this.mountMetrologyAN0Coeff, this.mountMetrologyAN0Coeff = mountMetrologyAN0Coeff);
        else
            this.mountMetrologyAN0Coeff = mountMetrologyAN0Coeff;
    }


    
    @Column(name="`MOUNTMETROLOGYAW0COEFF`", precision=64, scale=0)
    public Double getMountMetrologyAW0Coeff() {
        return this.mountMetrologyAW0Coeff;
    }
    
    public void setMountMetrologyAW0Coeff(Double mountMetrologyAW0Coeff) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("mountMetrologyAW0Coeff", this.mountMetrologyAW0Coeff, this.mountMetrologyAW0Coeff = mountMetrologyAW0Coeff);
        else
            this.mountMetrologyAW0Coeff = mountMetrologyAW0Coeff;
    }


    
    @Column(name="`LOCKED`")
    public Boolean getLocked() {
        return this.locked;
    }
    
    public void setLocked(Boolean locked) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("locked", this.locked, this.locked = locked);
        else
            this.locked = locked;
    }


    
    @Column(name="`INCREASEVERSION`")
    public Boolean getIncreaseVersion() {
        return this.increaseVersion;
    }
    
    public void setIncreaseVersion(Boolean increaseVersion) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("increaseVersion", this.increaseVersion, this.increaseVersion = increaseVersion);
        else
            this.increaseVersion = increaseVersion;
    }


    
    @Column(name="`CURRENTVERSION`")
    public Integer getCurrentVersion() {
        return this.currentVersion;
    }
    
    public void setCurrentVersion(Integer currentVersion) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("currentVersion", this.currentVersion, this.currentVersion = currentVersion);
        else
            this.currentVersion = currentVersion;
    }


    
    @Column(name="`WHO`", length=128)
    public String getWho() {
        return this.who;
    }
    
    public void setWho(String who) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("who", this.who, this.who = who);
        else
            this.who = who;
    }


    
    @Column(name="`CHANGEDESC`", length=16777216)
    public String getChangeDesc() {
        return this.changeDesc;
    }
    
    public void setChangeDesc(String changeDesc) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("changeDesc", this.changeDesc, this.changeDesc = changeDesc);
        else
            this.changeDesc = changeDesc;
    }



   public boolean equalsContent(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AntennaToPad) ) return false;
		 AntennaToPad castOther = ( AntennaToPad ) other;

		 return ( (this.getAntenna()==castOther.getAntenna()) || ( this.getAntenna()!=null && castOther.getAntenna()!=null && this.getAntenna().equals(castOther.getAntenna()) ) )
 && ( (this.getPad()==castOther.getPad()) || ( this.getPad()!=null && castOther.getPad()!=null && this.getPad().equals(castOther.getPad()) ) )
 && ( (this.getStartTime()==castOther.getStartTime()) || ( this.getStartTime()!=null && castOther.getStartTime()!=null && this.getStartTime().equals(castOther.getStartTime()) ) );
   }

   public int hashCodeContent() {
         int result = 17;

         
         result = 37 * result + ( getAntenna() == null ? 0 : this.getAntenna().hashCode() );
         result = 37 * result + ( getPad() == null ? 0 : this.getPad().hashCode() );
         result = 37 * result + ( getStartTime() == null ? 0 : this.getStartTime().hashCode() );
         
         
         
         
         
         
         
         
         
         return result;
   }


}


