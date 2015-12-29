package co.quchu.quchu.analysis;

/**
 * GatherViewModel
 * User: Chenhs
 * Date: 2015-12-29
 * 数据采集 页面浏览记录
 */
public class GatherViewModel {
    /**
     * entityId : 12
     * entityType : user
     * event : view
     * eventTime : 2015-10-05T21:02:49.228Z
     * properties : {"viewDuration":23200}
     * targetEntityId : 12
     * targetEntityType : item
     */

    private int entityId;
    public String entityType = "entityType";
    public String event = "view";
    private String eventTime;
    /**
     * viewDuration : 23200
     */

    private PropertiesEntity properties;
    private int targetEntityId;
    public String targetEntityType = "item";

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }


    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setProperties(PropertiesEntity properties) {
        this.properties = properties;
    }

    public void setTargetEntityId(int targetEntityId) {
        this.targetEntityId = targetEntityId;
    }


    public int getEntityId() {
        return entityId;
    }


    public String getEventTime() {
        return eventTime;
    }

    public PropertiesEntity getProperties() {
        return properties;
    }

    public int getTargetEntityId() {
        return targetEntityId;
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
