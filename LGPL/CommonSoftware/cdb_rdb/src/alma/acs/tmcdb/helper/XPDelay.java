package alma.acs.tmcdb;
// Generated Jun 5, 2017 7:15:51 PM by Hibernate Tools 4.3.1.Final


import alma.hibernate.util.StringEnumUserType;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * XPDelay generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name="`XPDELAY`"
    , uniqueConstraints =  @UniqueConstraint(columnNames={"`CONFIGURATIONID`", "`RECEIVERBAND`", "`SIDEBAND`", "`BASEBAND`"})
)
@TypeDefs({
@TypeDef(name="XPDelFreqBandEnum", typeClass=StringEnumUserType.class,
   parameters={ @Parameter(name="enumClassName", value="alma.acs.tmcdb.XPDelFreqBandEnum") }),
@TypeDef(name="XPDelSideBandEnum", typeClass=StringEnumUserType.class,
   parameters={ @Parameter(name="enumClassName", value="alma.acs.tmcdb.XPDelSideBandEnum") }),
@TypeDef(name="XPDelBaseBandEnum", typeClass=StringEnumUserType.class,
   parameters={ @Parameter(name="enumClassName", value="alma.acs.tmcdb.XPDelBaseBandEnum") })
})
public class XPDelay extends alma.acs.tmcdb.translator.TmcdbObject implements java.io.Serializable {


     protected Integer XPDelayId;
     protected HWConfiguration HWConfiguration;
     protected XPDelFreqBandEnum receiverBand;
     protected XPDelSideBandEnum sideBand;
     protected XPDelBaseBandEnum baseBand;
     protected Double delay;
     protected Double delayError;
     protected Long observationTime;
     protected String execBlockUID;
     protected Integer scanNumber;

    public XPDelay() {
    }
   
    @Id @GeneratedValue(generator="generator")
    @GenericGenerator(name="generator", strategy="native",
       parameters = {@Parameter(name="sequence", value="XPDelay_seq")}
	)

    
    @Column(name="`XPDELAYID`", unique=true, nullable=false)
    public Integer getXPDelayId() {
        return this.XPDelayId;
    }
    
    public void setXPDelayId(Integer XPDelayId) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("XPDelayId", this.XPDelayId, this.XPDelayId = XPDelayId);
        else
            this.XPDelayId = XPDelayId;
    }


@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="`CONFIGURATIONID`", nullable=false)
    public HWConfiguration getHWConfiguration() {
        return this.HWConfiguration;
    }
    
    public void setHWConfiguration(HWConfiguration HWConfiguration) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("HWConfiguration", this.HWConfiguration, this.HWConfiguration = HWConfiguration);
        else
            this.HWConfiguration = HWConfiguration;
    }


    
    @Column(name="`RECEIVERBAND`", nullable=false, length=128)
	@Type(type="XPDelFreqBandEnum")
    public XPDelFreqBandEnum getReceiverBand() {
        return this.receiverBand;
    }
    
    public void setReceiverBand(XPDelFreqBandEnum receiverBand) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("receiverBand", this.receiverBand, this.receiverBand = receiverBand);
        else
            this.receiverBand = receiverBand;
    }


    
    @Column(name="`SIDEBAND`", nullable=false, length=128)
	@Type(type="XPDelSideBandEnum")
    public XPDelSideBandEnum getSideBand() {
        return this.sideBand;
    }
    
    public void setSideBand(XPDelSideBandEnum sideBand) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("sideBand", this.sideBand, this.sideBand = sideBand);
        else
            this.sideBand = sideBand;
    }


    
    @Column(name="`BASEBAND`", nullable=false, length=128)
	@Type(type="XPDelBaseBandEnum")
    public XPDelBaseBandEnum getBaseBand() {
        return this.baseBand;
    }
    
    public void setBaseBand(XPDelBaseBandEnum baseBand) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("baseBand", this.baseBand, this.baseBand = baseBand);
        else
            this.baseBand = baseBand;
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


    
    @Column(name="`DELAYERROR`", precision=64, scale=0)
    public Double getDelayError() {
        return this.delayError;
    }
    
    public void setDelayError(Double delayError) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("delayError", this.delayError, this.delayError = delayError);
        else
            this.delayError = delayError;
    }


    
    @Column(name="`OBSERVATIONTIME`")
    public Long getObservationTime() {
        return this.observationTime;
    }
    
    public void setObservationTime(Long observationTime) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("observationTime", this.observationTime, this.observationTime = observationTime);
        else
            this.observationTime = observationTime;
    }


    
    @Column(name="`EXECBLOCKUID`", length=100)
    public String getExecBlockUID() {
        return this.execBlockUID;
    }
    
    public void setExecBlockUID(String execBlockUID) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("execBlockUID", this.execBlockUID, this.execBlockUID = execBlockUID);
        else
            this.execBlockUID = execBlockUID;
    }


    
    @Column(name="`SCANNUMBER`")
    public Integer getScanNumber() {
        return this.scanNumber;
    }
    
    public void setScanNumber(Integer scanNumber) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("scanNumber", this.scanNumber, this.scanNumber = scanNumber);
        else
            this.scanNumber = scanNumber;
    }



   public boolean equalsContent(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof XPDelay) ) return false;
		 XPDelay castOther = ( XPDelay ) other;

		 return ( (this.getHWConfiguration()==castOther.getHWConfiguration()) || ( this.getHWConfiguration()!=null && castOther.getHWConfiguration()!=null && this.getHWConfiguration().equals(castOther.getHWConfiguration()) ) )
 && ( (this.getReceiverBand()==castOther.getReceiverBand()) || ( this.getReceiverBand()!=null && castOther.getReceiverBand()!=null && this.getReceiverBand().equals(castOther.getReceiverBand()) ) )
 && ( (this.getSideBand()==castOther.getSideBand()) || ( this.getSideBand()!=null && castOther.getSideBand()!=null && this.getSideBand().equals(castOther.getSideBand()) ) )
 && ( (this.getBaseBand()==castOther.getBaseBand()) || ( this.getBaseBand()!=null && castOther.getBaseBand()!=null && this.getBaseBand().equals(castOther.getBaseBand()) ) );
   }

   public int hashCodeContent() {
         int result = 17;

         
         result = 37 * result + ( getHWConfiguration() == null ? 0 : this.getHWConfiguration().hashCode() );
         result = 37 * result + ( getReceiverBand() == null ? 0 : this.getReceiverBand().hashCode() );
         result = 37 * result + ( getSideBand() == null ? 0 : this.getSideBand().hashCode() );
         result = 37 * result + ( getBaseBand() == null ? 0 : this.getBaseBand().hashCode() );
         
         
         
         
         
         return result;
   }


}


