package co.quchu.quchu.analysis;

import java.util.HashMap;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.DateUtils;

/**
 * GatherRateModel
 * User: Chenhs
 * Date: 2015-12-29
 * 用户评分
 */
public class GatherRateModel {

    /**
     * entityId : user的id
     * entityType : user
     * event : rate
     * eventTime : 2015-10-05T21:02:49.228Z（timeStamp）
     * properties : {"rating":3}
     * targetEntityId : 店铺id
     * targetEntityType : item
     */
    public String eventTime;
    public String entityType;
    public int entityId;
    public String event;
    /**
     * rating : 3
     */

    public HashMap<String, Integer> properties;
    public String targetEntityId;
    public String targetEntityType;

    public GatherRateModel(String placeId, int placeRate) {
        if (AppContext.user != null) {
            entityId = AppContext.user.getUserId();
        } else {
            entityId = 0;
        }
        entityType = "user";
        eventTime = DateUtils.getUTCTime();
        event = "rate";
        targetEntityType = "item";
        targetEntityId = placeId;
        properties = new HashMap<>();
        properties.put("rating", placeRate);
    }


    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public void setTargetEntityType(String targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEvent() {
        return event;
    }

    public String getEventTime() {
        return eventTime;
    }
    public String getTargetEntityId() {
        return targetEntityId;
    }
    public String getTargetEntityType() {
        return targetEntityType;
    }
}
