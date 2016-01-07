package co.quchu.quchu.analysis;

import java.util.HashMap;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.DateUtils;

/**
 * GatherViewModel
 * User: Chenhs
 * Date: 2015-12-29
 * 数据采集 页面浏览记录
 */
public class GatherViewModel{

    /**
     * entityId :  user的id
     * entityType : user
     * event : view
     * eventTime : 2015-10-05T21:02:49.228Z（timeStamp）
     * properties : {"viewDuration":1002}
     * targetEntityId : 店铺id
     * targetEntityType : item
     */
    public String eventTime;
    public String entityType;
    public String entityId;
    public String event;
    /**
     * viewDuration : 1002
     */
    public HashMap<String, Long> properties;
    public String targetEntityId;
    public String targetEntityType;

    public GatherViewModel(String placeId) {
        if (AppContext.user != null) {
            entityId = AppContext.user.getUserId()+"";
        } else {
            entityId = 0+"";
        }
        entityType = "user";
        eventTime = DateUtils.getUTCTime();
        event = "view";
        targetEntityType = "item";
        targetEntityId = placeId;
    }

    public void setViewDuration(long vDuration) {
        if (properties == null)
            properties = new HashMap<>();
        properties.put("viewDuration", vDuration);
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
