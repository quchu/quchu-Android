package co.quchu.quchu.analysis;

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

    private String entityId;
    private String entityType;
    private String event;
    private String eventTime;
    /**
     * rating : 3
     */

    private PropertiesEntity properties;
    private String targetEntityId;
    private String targetEntityType;

    public GatherRateModel(String placeId, int placeRate) {
        event = "rate";
        entityType = "user";
        entityId = AppContext.user.getUserId() + "";
        targetEntityType = "item";
        targetEntityId = placeId;
        eventTime = DateUtils.getUTCTime();
        properties=new PropertiesEntity();
        properties.setRating(placeRate);
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
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

    public void setProperties(PropertiesEntity properties) {
        this.properties = properties;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public void setTargetEntityType(String targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public String getEntityId() {
        return entityId;
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

    public PropertiesEntity getProperties() {
        return properties;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public String getTargetEntityType() {
        return targetEntityType;
    }

    public static class PropertiesEntity {
        private int rating;

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getRating() {
            return rating;
        }
    }
}
