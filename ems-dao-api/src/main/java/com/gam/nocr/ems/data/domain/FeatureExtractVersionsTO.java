package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
@Entity
@Table(name = "EMST_FEATURE_EXTRACT_VERSIONS")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_FEATURE_EXTRACT_VERSIONS", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "FeatureExtractVersionsTO.findById"
        , query = "select fev" +
                " from FeatureExtractVersionsTO fev" +
                " where fev.id=:id")
})
public class FeatureExtractVersionsTO extends ExtEntityTO {

    private String featureExtractVersion = "0.0";

    public FeatureExtractVersionsTO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "FEV_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "FEV_FEATURE_EXTRACT_VERSION", nullable = false, length = 5, columnDefinition = "varchar2(5) default '1'")
    public String getFeatureExtractVersion() {
        return featureExtractVersion;
    }

    public void setFeatureExtractVersion(String featureExtractVersion) {
        this.featureExtractVersion = featureExtractVersion;
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
