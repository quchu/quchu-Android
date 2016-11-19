package co.quchu.quchu.widget.DropDownMenu;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.model.AreaBean;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchSortBean;

/**
 * Created by mwb on 16/11/15.
 */
public class MDropHelper {

  /**
   * 分类
   *
   * @param categoryList
   * @return
   */
  public static List<MDropBean> processCategory(List<SearchCategoryBean> categoryList) {
    if (categoryList == null || categoryList.size() == 0) {
      return null;
    }

    List<MDropBean> dropBeanList = new ArrayList<>();

    SearchCategoryBean parentAll = new SearchCategoryBean();
    parentAll.setTagId(-1);
    parentAll.setZh("全部分类");

    List<DetailModel.TagsEntity> tagsEntities = new ArrayList<>();
    DetailModel.TagsEntity tagsEntity = new DetailModel.TagsEntity();
    tagsEntity.setTagId(-1);
    tagsEntity.setZh("全部");

    parentAll.setDatas(tagsEntities);
    categoryList.add(0, parentAll);

    for (SearchCategoryBean bean : categoryList) {
      bean.getDatas().add(0, tagsEntity);
    }

    for (SearchCategoryBean categoryBean : categoryList) {
      MDropBean dropBean = new MDropBean();
      dropBean.setId(String.valueOf(categoryBean.getTagId()));
      dropBean.setType(MDropBean.DATA_TYPE_CATEGORY);
      dropBean.setText(categoryBean.getZh());

      List<DetailModel.TagsEntity> children = categoryBean.getDatas();
      if (children == null || children.size() == 0) {
        break;
      }
      List<MDropBean> newChildren = new ArrayList<>();
      for (DetailModel.TagsEntity child : children) {
        MDropBean childDropBean = new MDropBean();
        childDropBean.setId(String.valueOf(child.getTagId()));
        childDropBean.setText(child.getZh());
        childDropBean.setType(MDropBean.DATA_TYPE_CATEGORY);
        newChildren.add(childDropBean);
      }
      dropBean.setChildren(newChildren);
      dropBeanList.add(dropBean);
    }

    return dropBeanList;
  }

  /**
   * 商圈
   *
   * @param areaList
   * @return
   */
  public static List<MDropBean> processArea(List<AreaBean> areaList) {
    if (areaList == null || areaList.size() == 0) {
      return null;
    }

    List<MDropBean> dropBeanList = new ArrayList<>();

    AreaBean parentAll = new AreaBean();
    parentAll.setAreaId("-1");
    parentAll.setAreaName("全部商圈");

    List<AreaBean.CircleListBean> circleListBeen = new ArrayList<>();
    AreaBean.CircleListBean circleListBean = new AreaBean.CircleListBean();
    circleListBean.setCircleId("-1");
    circleListBean.setCircleName("全部");

    parentAll.setCircleList(circleListBeen);
    areaList.add(0, parentAll);

    for (AreaBean bean : areaList) {
      bean.getCircleList().add(0, circleListBean);
    }

    for (AreaBean areaBean : areaList) {
      MDropBean dropBean = new MDropBean();
      dropBean.setId(areaBean.getAreaId());
      dropBean.setText(areaBean.getAreaName());
      dropBean.setType(MDropBean.DATA_TYPE_AREA);

      List<AreaBean.CircleListBean> children = areaBean.getCircleList();
      if (children == null || children.size() == 0) {
        break;
      }
      List<MDropBean> newChildren = new ArrayList<>();
      for (AreaBean.CircleListBean child : children) {
        MDropBean childDropBean = new MDropBean();
        childDropBean.setId(child.getCircleId());
        childDropBean.setText(child.getCircleName());
        childDropBean.setType(MDropBean.DATA_TYPE_AREA);
        newChildren.add(childDropBean);
      }
      dropBean.setChildren(newChildren);
      dropBeanList.add(dropBean);
    }
    return dropBeanList;
  }

  /**
   * 排序
   *
   * @param sortList
   * @return
   */
  public static List<MDropBean> processSort(List<SearchSortBean> sortList) {
    if (sortList == null || sortList.size() == 0) {
      return null;
    }

    List<MDropBean> dropBeanList = new ArrayList<>();

    for (SearchSortBean searchSortBean : sortList) {
      MDropBean dropBean = new MDropBean();
      dropBean.setId(String.valueOf(searchSortBean.getSortId()));
      dropBean.setText(searchSortBean.getSortName());
      dropBean.setType(MDropBean.DATA_TYPE_SORT);
      dropBean.setChildren(null);
      dropBeanList.add(dropBean);
    }
    return dropBeanList;
  }
}
