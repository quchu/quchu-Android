package co.quchu.quchu.model;

import java.util.ArrayList;

import co.quchu.quchu.utils.LogUtils;

/**
 * SearchModel
 * User: Chenhs
 * Date: 2015-12-10
 */
public class SearchModel {

    /**
     * serachStr : luxury
     */

    private ArrayList<SearchListEntity> searchList;

    public SearchModel() {

    }

    public void setSearchList(ArrayList<SearchListEntity> searchList) {
        if (searchList == null)
            searchList = new ArrayList<SearchListEntity>();
        this.searchList = searchList;
    }

    public ArrayList<SearchListEntity> getSearchList() {
        return searchList;
    }

    public boolean removeSearchHistory(int index) {
        if (searchList == null)
            searchList = new ArrayList<SearchListEntity>();
        LogUtils.json("start  size=" + searchList.size() + "///index=" + index);
        if (index < searchList.size()) {
            searchList.remove(index);
        }
        LogUtils.json("start  size=" + searchList.size() + "///index=" + index);
        if (searchList.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<SearchListEntity> removeAllSearchHistory() {
        if (searchList == null)
            searchList = new ArrayList<SearchListEntity>();
        int sCount = searchList.size();
        for (int i = 0; i < sCount; i++) {
            searchList.remove(0);
        }
        return searchList;
    }


    public ArrayList<SearchListEntity> addSearchHistory(String str) {
        if (searchList == null)
            searchList = new ArrayList<SearchListEntity>();
        if (searchList.size() == 0) {
            SearchListEntity entity = new SearchListEntity();
            entity.setSerachStr(str);
            searchList.add(entity);
        } else if (searchList.size() == 10) {
            int equalsIndex = -1;
            for (int i = 0; i < searchList.size(); i++) {
                if (searchList.get(i).getSerachStr().equals(str)) {
                    equalsIndex = i;
                }
            }
            if (equalsIndex != -1) {
                searchList.remove(equalsIndex);
            } else {
                searchList.remove(0);
            }
            SearchListEntity entity = new SearchListEntity();
            entity.setSerachStr(str);
            searchList.add(entity);
        } else if (searchList.size() > 0 && searchList.size() < 10) {
            int equalsIndex = -1;
            for (int i = 0; i < searchList.size(); i++) {

                if (searchList.get(i).getSerachStr().equals(str)) {
                    equalsIndex = i;
                }
            }
            if (equalsIndex != -1) {
                searchList.remove(equalsIndex);
            }
            SearchListEntity entity = new SearchListEntity();
            entity.setSerachStr(str);
            searchList.add(entity);
        }


        return searchList;
    }

    public static class SearchListEntity {
        private String serachStr;

        public void setSerachStr(String serachStr) {
            this.serachStr = serachStr;
        }

        public String getSerachStr() {
            return serachStr;
        }

        @Override
        public String toString() {
            return "{serachStr=" + serachStr + "}";
        }
    }

    @Override
    public String toString() {
        if (searchList == null)
            searchList = new ArrayList<SearchListEntity>();
        return searchList.toString();
    }
}