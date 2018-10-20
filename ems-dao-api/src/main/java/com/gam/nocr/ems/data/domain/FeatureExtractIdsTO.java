package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
@Entity
@Table(name = "EMST_FEATURE_EXTRACT_IDS")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_FEATURE_EXTRACT_IDS", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "FeatureExtractIdsTO.findById"
                , query = "select fei" +
                " from FeatureExtractIdsTO fei" +
                " where fei.id=:id")
})
public class FeatureExtractIdsTO extends ExtEntityTO{

    private String featureExtractId = "02";
    private String featureExtractIdName = "متیران";

    public FeatureExtractIdsTO(){}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "FEI_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "FEI_FEATURE_EXTRACT_ID", nullable = false
            , length = 4, columnDefinition = "varchar2(4) default '02'")
    public String getFeatureExtractId() {
        return featureExtractId;
    }

    public void setFeatureExtractId(String featureExtractId) {
        this.featureExtractId = featureExtractId;
    }

    @Column(name = "FEI_FEATURE_EXTRACT_ID_NAME", length = 15, columnDefinition = "varchar2(15)")
    public String getFeatureExtractIdName() {
        return featureExtractIdName;
    }

    public void setFeatureExtractIdName(String featureExtractIdName) {
        this.featureExtractIdName = featureExtractIdName;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ReligionTO)) {
            return false;
        }
        ReligionTO other = (ReligionTO) obj;
        if (this.getId() == null ||
                other.getId() == null) {
            return false;
        }
        return this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }

}
