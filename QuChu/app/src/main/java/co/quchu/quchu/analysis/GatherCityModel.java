package co.quchu.quchu.analysis;

import java.util.HashMap;

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
    public String event;
    public String eventTime;
    public String entityType;
    public String entityId;
    /**
     * city : locationOfUser
     */
    public HashMap<String, Integer> properties;

    public GatherCityModel(int cityIndex) {
        if (AppContext.user != null) {
            entityId = AppContext.user.getUserId()+"";
        } else {
            entityId = 0+"";
        }
        entityType = "user";
        eventTime = DateUtils.getUTCTime();
        event = "$set";
        properties = new HashMap<>();
        properties.put("city", cityIndex);
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

    public String getEntityType() {
        return entityType;
    }

    public String getEvent() {
        return event;
    }

    public String getEventTime() {
        return eventTime;
    }
}
