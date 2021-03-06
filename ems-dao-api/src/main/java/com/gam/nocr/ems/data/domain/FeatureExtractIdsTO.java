package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.FeatureExtractType;
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

    private String featureExtractName;
    private FeatureExtractType featureExtractType;
    private String featureExtractId;

    public FeatureExtractIdsTO(){}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "FEI_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "FEI_FEATURE_EXTRACT_NAME", length = 20, columnDefinition = "varchar2(20)")
    public String getFeatureExtractName() {
        return featureExtractName;
    }

    public void setFeatureExtractName(String featureExtractName) {
        this.featureExtractName = featureExtractName;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "FEI_FEATURE_EXTRACT_TYPE", nullable = false)
    public FeatureExtractType getFeatureExtractType() {
        return featureExtractType;
    }

    public void setFeatureExtractType(FeatureExtractType featureExtractType) {
        this.featureExtractType = featureExtractType;
    }

    @Column(name = "FEI_FEATURE_EXTRACT_ID", length = 4, columnDefinition = "varchar2(4)")
    public String getFeatureExtractId() {
        return featureExtractId;
    }

    public void setFeatureExtractId(String featureExtractId) {
        this.featureExtractId = featureExtractId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FeatureExtractIdsTO)) {
            return false;
        }
        FeatureExtractIdsTO other = (FeatureExtractIdsTO) obj;
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
