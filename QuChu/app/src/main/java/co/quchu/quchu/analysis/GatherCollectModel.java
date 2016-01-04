package co.quchu.quchu.analysis;

import java.io.Serializable;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.DateUtils;

/**
 * GatherCollectModel
 * User: Chenhs
 * Date: 2015-12-29
 * 收藏 数据采集
 */
public class GatherCollectModel implements Serializable {


    /**
     * event : storeCollect
     * entityType : user
     * entityId : user的id
     * targetEntityType : storeItem
     * targetEntityId : 店铺id
     * properties : {}
     * eventTime : 2015-10-05T21:02:49.228Z（timeStamp）
     */

    private String event;
    private String entityType;
    private String entityId;
    private String targetEntityType;
    private String targetEntityId;
    private PropertiesEntity properties;
    private String eventTime;

    public static int collectPlace = 0x00;
    public static int collectCard = 0x01;

    public GatherCollectModel(int collectType, String targetId) {
        if (collectType == collectPlace) {
            event = "storeCollect";
            targetEntityType = "storeItem";
        } else {
            event = "cardCollect";
            targetEntityType = "cardItem";
        }
        entityId = AppContext.user.getUserId() + "";
        targetEntityId = targetId;
        entityType = "user";
        eventTime = DateUtils.getUTCTime();
        properties=new PropertiesEntity();
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

    public void setProperties(PropertiesEntity properties) {
        this.properties = properties;
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

    public PropertiesEntity getProperties() {
        return properties;
    }

    public String getEventTime() {
        return eventTime;
    }

    public static class PropertiesEntity {
    }
}
