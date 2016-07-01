package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by no21 on 2016/6/29.
 * email:437943145@qq.com
 * desc :
 */
public class AreaBean  {
    /**
     * areaId : 48
     * areaName : 玄武区
     * circleList : [{"circleId":177,"circleName":"其他"}]
     */

    private String areaId;
    private String areaName;
    /**
     * circleId : 177
     * circleName : 其他
     */

    private List<CircleListBean> circleList;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<CircleListBean> getCircleList() {
        return circleList;
    }

    public void setCircleList(List<CircleListBean> circleList) {
        this.circleList = circleList;
    }

    public static class CircleListBean {
        private String circleId;
        private String circleName;

        public String getCircleId() {
            return circleId;
        }

        public void setCircleId(String circleId) {
            this.circleId = circleId;
        }

        public String getCircleName() {
            return circleName;
        }

        public void setCircleName(String circleName) {
            this.circleName = circleName;
        }
    }
}
