package com.gam.nocr.ems.data.domain;

import com.gam.nocr.ems.data.enums.BatchState;
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
@Table(name = "EMST_BATCH")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_BATCH", allocationSize = 1)
public class BatchTO extends CardContainerTO implements JSONable {

    private BatchState state;
    private BoxTO box;
    private String cmsID;
    private DepartmentTO nextReceiver;
    private List<CardTO> cards;
//    private Long totalCardsCount;
    private Boolean isLostBatchConfirmed;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "BAT_ID")
    public Long getId() {
        return super.getId();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "BAT_STATE", nullable = false)
    public BatchState getState() {
        return state;
    }

    public void setState(BatchState state) {
        this.state = state;
    }

    @ManyToOne
    @JoinColumn(name = "BAT_BOX_ID")
    @JSON(include = false)
    public BoxTO getBox() {
        return box;
    }

    public void setBox(BoxTO box) {
        this.box = box;
    }

    @Column(name = "BAT_CMS_ID", length = 40, unique = true, nullable = false)
    public String getCmsID() {
        return cmsID;
    }

    public void setCmsID(String cmsID) {
        this.cmsID = cmsID;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BAT_NEXT_RECEIVER_ID")
    @JSON(include = false)
    public DepartmentTO getNextReceiver() {
        return nextReceiver;
    }

    public void setNextReceiver(DepartmentTO nextReceiver) {
        this.nextReceiver = nextReceiver;
    }

    @OneToMany(mappedBy = "batch")
    public List<CardTO> getCards() {
        return cards;
    }

    public void setCards(List<CardTO> cards) {
        this.cards = cards;
    }
//    @Column(name = "BAT_TOTAL_CARD_COUNT")
//    public Long getTotalCardsCount()
//    {
//    	return this.totalCardsCount;
//    }
//    
//    public void setTotalCardsCount(Long totalCardsCount)
//    {
//    	this.totalCardsCount = totalCardsCount;
//    }
    @Column(name = "BAT_LOSTCONFIRM")
    public Boolean getIsLostBatchConfirmed() {
		return isLostBatchConfirmed;
	}

	public void setIsLostBatchConfirmed(Boolean isLostBatchConfirmed) {
		this.isLostBatchConfirmed = isLostBatchConfirmed;
	}
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
        if (box == null) {
            jsonObject += "," + "boxId:" + box;
        } else {
            jsonObject += "," + "boxId:" + box.getId() + "," + "boxCMSId:" + box.getCmsID();
        }
        jsonObject += "}";
        return jsonObject;
    }
}
