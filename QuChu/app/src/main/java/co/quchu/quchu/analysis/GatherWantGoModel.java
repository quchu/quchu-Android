package co.quchu.quchu.analysis;

import co.quchu.quchu.utils.DateUtils;

/**
 * GatherWantGoModel
 * User: Chenhs
 * Date: 2016-02-24
 */
public class GatherWantGoModel {

    /**
     * event : wantGo
     * entityType : user
     * entityId : user的id
     * targetEntityType : item
     * targetEntityId : 店铺id
     * eventTime : 2015-10-05T21:02:49.228Z（timeStamp）
     */

    private String event;
    private String entityType;
    private String entityId;
    private String targetEntityType;
    private String targetEntityId;
    private String eventTime;

    public GatherWantGoModel(int userId, int pId) {
        this.event = "wantGo";
        this.entityType = "user";
        this.targetEntityType = "item";
        this.entityId = String.valueOf(userId);
        this.targetEntityId = String.valueOf(pId);
        this.eventTime = DateUtils.getUTCTime();

    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setTargetEntityType(String targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEvent() {
        return event;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getTargetEntityType() {
        return targetEntityType;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public String getEventTime() {
        return eventTime;
    }
}
