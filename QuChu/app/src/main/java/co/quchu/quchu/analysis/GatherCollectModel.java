package co.quchu.quchu.analysis;

import java.io.Serializable;
import java.util.HashMap;

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
    public String eventTime;
    public String entityType;
    public String entityId;
    public String event;
    public String targetEntityType;
    public String targetEntityId;
    public HashMap<String, Integer> properties;

    public static int collectPlace = 0x00;
    public static int collectCard = 0x01;

    public GatherCollectModel(int collectType, int targetId) {
        if (AppContext.user != null) {
            entityId = AppContext.user.getUserId() + "";
        } else {
            entityId = 0 + "";
        }
        entityType = "user";
        eventTime = DateUtils.getUTCTime();
        if (collectType == collectPlace) {
            event = "storeCollect";
            targetEntityType = "item";
        } else {
            event = "cardCollect";
            targetEntityType = "cardItem";
        }
        targetEntityId = targetId + "";
        properties = new HashMap<>();
    }

    public void setEvent(String event) {
        this.event = event;
    }


    public void setTargetEntityType(String targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }


    public String getEvent() {
        return event;
    }

    public String getTargetEntityType() {
        return targetEntityType;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

}
