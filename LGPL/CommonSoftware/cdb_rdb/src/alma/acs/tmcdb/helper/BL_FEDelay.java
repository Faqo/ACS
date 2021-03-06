package alma.acs.tmcdb;
// Generated Jun 5, 2017 7:15:51 PM by Hibernate Tools 4.3.1.Final


import alma.hibernate.util.StringEnumUserType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;

/**
 * BL_FEDelay generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name="`BL_FEDELAY`"
)
@TypeDef(name="BL_FEDelayOp", typeClass=StringEnumUserType.class,
   parameters={ @Parameter(name="enumClassName", value="alma.acs.tmcdb.BL_FEDelayOp") })
public class BL_FEDelay extends alma.acs.tmcdb.translator.TmcdbObject implements java.io.Serializable {


     protected BL_FEDelayId id;
     protected String who;
     protected String changeDesc;
     protected Integer antennaId;
     protected String receiverBand;
     protected String polarization;
     protected String sideBand;
     protected Double delay;

    public BL_FEDelay() {
    }
   
       @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="`version`", column=@Column(name="VERSION`", nullable=false) ), 
        @AttributeOverride(name="modTime`", column=@Column(name="MODTIME`", nullable=false) ), 
        @AttributeOverride(name="operation`", column=@Column(name="OPERATION`", nullable=false, length=1) ), 
        @AttributeOverride(name="FEDelayId`", column=@Column(name="FEDELAYID`", nullable=false) ) } )
    public BL_FEDelayId getId() {
        return this.id;
    }
    
    public void setId(BL_FEDelayId id) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("id", this.id, this.id = id);
        else
            this.id = id;
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


    
    @Column(name="`ANTENNAID`", nullable=false)
    public Integer getAntennaId() {
        return this.antennaId;
    }
    
    public void setAntennaId(Integer antennaId) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("antennaId", this.antennaId, this.antennaId = antennaId);
        else
            this.antennaId = antennaId;
    }


    
    @Column(name="`RECEIVERBAND`", nullable=false, length=128)
    public String getReceiverBand() {
        return this.receiverBand;
    }
    
    public void setReceiverBand(String receiverBand) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("receiverBand", this.receiverBand, this.receiverBand = receiverBand);
        else
            this.receiverBand = receiverBand;
    }


    
    @Column(name="`POLARIZATION`", nullable=false, length=128)
    public String getPolarization() {
        return this.polarization;
    }
    
    public void setPolarization(String polarization) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("polarization", this.polarization, this.polarization = polarization);
        else
            this.polarization = polarization;
    }


    
    @Column(name="`SIDEBAND`", nullable=false, length=128)
    public String getSideBand() {
        return this.sideBand;
    }
    
    public void setSideBand(String sideBand) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("sideBand", this.sideBand, this.sideBand = sideBand);
        else
            this.sideBand = sideBand;
    }


    
    @Column(name="`DELAY`", nullable=false, precision=64, scale=0)
    public Double getDelay() {
        return this.delay;
    }
    
    public void setDelay(Double delay) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("delay", this.delay, this.delay = delay);
        else
            this.delay = delay;
    }





}


