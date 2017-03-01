package com.gam.nocr.ems.data.domain;

import com.gam.nocr.ems.data.enums.BoxState;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 * @Edited By : H.Adldoost on : 29 June 2015
 */
@Entity
@Table(name = "EMST_BOX")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_BOX", allocationSize = 1)
public class BoxTO extends CardContainerTO implements JSONable {

    private String cmsID;
    private BoxState state;
    private DepartmentTO nextReceiver;
    private List<BatchTO> batches;
    
//    private Long totalBatchesCount;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "BOX_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "BOX_CMS_ID", length = 20, unique = true, nullable = false)
    public String getCmsID() {
        return cmsID;
    }

    public void setCmsID(String cmsID) {
        this.cmsID = cmsID;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "BOX_STATE")
    public BoxState getState() {
        return state;
    }

    public void setState(BoxState state) {
        this.state = state;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOX_NEXT_RECEIVER_ID")
    @JSON(include = false)
    public DepartmentTO getNextReceiver() {
        return nextReceiver;
    }

    public void setNextReceiver(DepartmentTO nextReceiver) {
        this.nextReceiver = nextReceiver;
    }

    @OneToMany(mappedBy = "box")
    public List<BatchTO> getBatches() {
        return batches;
    }

    public void setBatches(List<BatchTO> batches) {
        this.batches = batches;
    }
//    @Column(name="BOX_TOTAL_BATCH_COUNT")
//    public Long getTotalBatchesCount() {
//		return totalBatchesCount;
//	}
//
//	public void setTotalBatchesCount(Long totalBatchesCount) {
//		this.totalBatchesCount = totalBatchesCount;
//	}

	@Override
    public String toString() {
        return toJSON();
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String}
     *
     * @return an instance of type {@link String}
     */
    @Override
    public String toJSON() {
        String jsonObject = EmsUtil.toJSON(this);
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        if (nextReceiver == null) {
            jsonObject += "," + "nextReceiverId:" + nextReceiver;
        } else {
            jsonObject += "," + "nextReceiverId:" + nextReceiver.getId();
        }
        jsonObject += "}";
        return jsonObject;
    }
}
