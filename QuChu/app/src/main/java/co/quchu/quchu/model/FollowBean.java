package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by no21 on 2016/4/28.
 * email:437943145@qq.com
 * desc :
 */
public class FollowBean {


    private int followNum;
    private Object heads;
    private int hostNum;

    private UsersBean users;

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public Object getHeads() {
        return heads;
    }

    public void setHeads(Object heads) {
        this.heads = heads;
    }

    public int getHostNum() {
        return hostNum;
    }

    public void setHostNum(int hostNum) {
        this.hostNum = hostNum;
    }

    public UsersBean getUsers() {
        return users;
    }

    public void setUsers(UsersBean users) {
        this.users = users;
    }

    public static class UsersBean {
        private int pageCount;
        private int pageSize;
        private int pagesNo;
        private int resultCount;
        private int rowCount;
        private int rowEnd;
        private int rowStart;


        private List<FollowUserModel> result;

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPagesNo() {
            return pagesNo;
        }

        public void setPagesNo(int pagesNo) {
            this.pagesNo = pagesNo;
        }

        public int getResultCount() {
            return resultCount;
        }

        public void setResultCount(int resultCount) {
            this.resultCount = resultCount;
        }

        public int getRowCount() {
            return rowCount;
        }

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public int getRowEnd() {
            return rowEnd;
        }

        public void setRowEnd(int rowEnd) {
            this.rowEnd = rowEnd;
        }

        public int getRowStart() {
            return rowStart;
        }

        public void setRowStart(int rowStart) {
            this.rowStart = rowStart;
        }

        public List<FollowUserModel> getResult() {
            return result;
        }

        public void setResult(List<FollowUserModel> result) {
            this.result = result;
        }


    }
}
