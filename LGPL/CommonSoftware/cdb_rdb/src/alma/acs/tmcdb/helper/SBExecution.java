package alma.acs.tmcdb;
// Generated Jun 5, 2017 7:15:51 PM by Hibernate Tools 4.3.1.Final


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SBExecution generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name="`SBEXECUTION`"
)
public class SBExecution extends alma.acs.tmcdb.translator.TmcdbObject implements java.io.Serializable {


     protected SBExecutionId id;
     protected Array array;
     protected Long endTime;
     protected Boolean normalTermination;

    public SBExecution() {
    }
   
       @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="`arrayId`", column=@Column(name="ARRAYID`", nullable=false) ), 
        @AttributeOverride(name="sbUID`", column=@Column(name="SBUID`", nullable=false, length=256) ), 
        @AttributeOverride(name="startTime`", column=@Column(name="STARTTIME`", nullable=false) ) } )
    public SBExecutionId getId() {
        return this.id;
    }
    
    public void setId(SBExecutionId id) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("id", this.id, this.id = id);
        else
            this.id = id;
    }


@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="`ARRAYID`", nullable=false, insertable=false, updatable=false)
    public Array getArray() {
        return this.array;
    }
    
    public void setArray(Array array) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("array", this.array, this.array = array);
        else
            this.array = array;
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


    
    @Column(name="`NORMALTERMINATION`", nullable=false)
    public Boolean getNormalTermination() {
        return this.normalTermination;
    }
    
    public void setNormalTermination(Boolean normalTermination) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("normalTermination", this.normalTermination, this.normalTermination = normalTermination);
        else
            this.normalTermination = normalTermination;
    }





}


