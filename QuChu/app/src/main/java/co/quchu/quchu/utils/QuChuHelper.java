package co.quchu.quchu.utils;

import co.quchu.quchu.R;

/**
 * Created by mwb on 16/11/16.
 */
public class QuChuHelper {

  public static int getUserAvatar(String mark) {
    int resId = -1;
    switch (mark) {
      case "小食神":
        resId = R.mipmap.ic_chihuo_dialog;
        break;

      case "艺术家":
        resId = R.mipmap.ic_yishu_dialog;
        break;

      case "外交官":
        resId = R.mipmap.ic_shejiao_dialog;
        break;

      case "时尚精":
        resId = R.mipmap.ic_shishang_dialog;
        break;

      case "大财阀":
        resId = R.mipmap.ic_tuhao_dialog;
        break;

      case "玩乐咖":
        resId = R.mipmap.ic_haoqi_dialog;
        break;

      default:
        resId = -1;
        break;
    }

    return resId;
  }
}
