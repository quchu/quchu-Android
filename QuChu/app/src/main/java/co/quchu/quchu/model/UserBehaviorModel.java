package co.quchu.quchu.model;



/**
 * Created by Nico on 16/6/2.
 */
public class UserBehaviorModel {

        public int pageId;
        public String userBehavior;
        public String arguments;
        public long timestamp;


        @Override
        public String toString() {
                return "UserBehaviorModel{" +
                        "pageId=" + pageId +
                        ", userBehavior='" + userBehavior + '\'' +
                        ", arguments='" + arguments + '\'' +
                        ", timestamp=" + timestamp +
                        '}';
        }
}
