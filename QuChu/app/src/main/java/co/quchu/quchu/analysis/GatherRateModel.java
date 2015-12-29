package co.quchu.quchu.analysis;

/**
 * GatherRateModel
 * User: Chenhs
 * Date: 2015-12-29
 * 用户评分
 */
public class GatherRateModel {
    /**
     * entityId : 12
     * entityType : user
     * event : rate
     * eventTime : 2015-10-05T21:02:49.228Z
     * properties : {"rating":3}
     * targetEntityId : 12
     * targetEntityType : item
     */
    private int entityId;
    public String entityType = "user";
    public String event = "rate";
    private String eventTime;
    /**
     * rating : 3
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
        private int rating;

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getRating() {
            return rating;
        }
    }
}
