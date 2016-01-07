package co.quchu.quchu.analysis;

import org.json.JSONObject;

import java.util.Map;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.DateUtils;

/**
 * GatherModel
 * User: Chenhs
 * Date: 2016-01-07
 */
public class GatherModel extends JSONObject {
    public String eventTime;
    public String entityType;
    public int entityId;
    public Map<String, Integer> properties;

    public GatherModel() {
        if (AppContext.user != null) {
            entityId = AppContext.user.getUserId();
        } else {
            entityId = 0;
        }
        entityType = "user";
        eventTime = DateUtils.getUTCTime();
    }

}
