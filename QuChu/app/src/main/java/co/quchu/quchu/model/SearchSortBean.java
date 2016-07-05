package co.quchu.quchu.model;

/**
 * Created by no21 on 2016/7/4.
 * email:437943145@qq.com
 * desc :
 */
public class SearchSortBean {

    /**
     * sortId : 0
     * sortName : 智能
     */

    private int sortId;
    private String sortName;

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public String getSortName() {
        return sortName+"排序";
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
}
