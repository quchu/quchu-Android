package co.quchu.quchu.utils;

import co.quchu.quchu.R;

/**
 * Created by mwb on 16/11/16.
 */
public class QuChuHelper {

  public static int getUserAvatarByGene(String mark) {
    int resId = -1;
    switch (mark) {
      case "小食神":
        resId = R.mipmap.ic_chihuo_main;
        break;

      case "艺术家":
        resId = R.mipmap.ic_wenyi_main;
        break;

      case "外交官":
        resId = R.mipmap.ic_shejiao_main;
        break;

      case "时尚精":
        resId = R.mipmap.ic_shishang_main;
        break;

      case "大财阀":
        resId = R.mipmap.ic_tuhao_main;
        break;

      case "玩乐咖":
        resId = R.mipmap.ic_haoqi_main;
        break;

      case "新生宝宝":
        resId = R.mipmap.ic_xinshengbaby_main;
        break;
    }

    return resId;
  }
}
