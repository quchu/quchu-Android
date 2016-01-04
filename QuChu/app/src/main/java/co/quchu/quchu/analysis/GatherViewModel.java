package co.quchu.quchu.analysis;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.DateUtils;

/**
 * GatherViewModel
 * User: Chenhs
 * Date: 2015-12-29
 * 数据采集 页面浏览记录
 */
public class GatherViewModel {

    /**
     * entityId :  user的id
     * entityType : user
     * event : view
     * eventTime : 2015-10-05T21:02:49.228Z（timeStamp）
     * properties : {"viewDuration":1002}
     * targetEntityId : 店铺id
     * targetEntityType : item
     */

    private String entityId;
    private String entityType;
    private String event;
    private String eventTime;
    /**
     * viewDuration : 1002
     */
    private PropertiesEntity properties;
    private String targetEntityId;
    private String targetEntityType;

    public GatherViewModel(String placeId) {
        entityId = AppContext.user.getUserId() + "";
        entityType = "user";
        event = "view";
        eventTime = DateUtils.getUTCTime();
        targetEntityType = "item";
        targetEntityId = placeId;
    }

    public void setViewDuration(long vDuration) {
        if (properties == null)
            properties = new PropertiesEntity();
        properties.setViewDuration(vDuration);
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
        private long viewDuration;

        public void setViewDuration(long viewDuration) {
            this.viewDuration = viewDuration;
        }

        public long getViewDuration() {
            return viewDuration;
        }
    }
}
