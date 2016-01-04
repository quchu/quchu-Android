package co.quchu.quchu.analysis;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.DateUtils;

/**
 * GatherCityModel
 * User: Chenhs
 * Date: 2016-01-04
 * 城市切换数据采集
 */
public class GatherCityModel {

    /**
     * entityId : user id  (用户城市切换)
     * entityType : user
     * event : $set
     * eventTime : 2015-10-05T21:02:49.228Z（timeStamp）
     * properties : {"city":"locationOfUser"}
     */

    private String entityId;
    private String entityType;
    private String event;
    private String eventTime;
    /**
     * city : locationOfUser
     */

    private PropertiesEntity properties;

    public GatherCityModel(int cityIndex) {
        entityId = AppContext.user.getUserId() + "";
        entityType = "user";
        event = "$set";
        eventTime = DateUtils.getUTCTime();
        properties=new PropertiesEntity();
        properties.setCity(cityIndex);
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

    public static class PropertiesEntity {
        private int city;

        public void setCity(int city) {
            this.city = city;
        }

        public int getCity() {
            return city;
        }
    }
}
