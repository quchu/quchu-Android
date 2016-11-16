package co.quchu.quchu.widget.DropDownMenu;

import java.util.List;

/**
 * Created by mwb on 16/11/15.
 */
public class MDropBean {

  public static final int DATA_TYPE_CATEGORY = 1;//分类
  public static final int DATA_TYPE_AREA = 2;//商圈
  public static final int DATA_TYPE_SORT = 3;//排序

  private String id;
  private String text;
  private int type;
  private List<MDropBean> children;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public List<MDropBean> getChildren() {
    return children;
  }

  public void setChildren(List<MDropBean> children) {
    this.children = children;
  }
}
