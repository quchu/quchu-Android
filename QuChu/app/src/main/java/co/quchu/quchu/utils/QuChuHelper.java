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
        resId = R.drawable.ic_chihuo_main;
        break;

      case "艺术家":
        resId = R.drawable.ic_wenyi_main;
        break;

      case "外交官":
        resId = R.drawable.ic_shejiao_main;
        break;

      case "时尚精":
        resId = R.drawable.ic_shishang_main;
        break;

      case "大财阀":
        resId = R.drawable.ic_tuhao_main;
        break;

      case "玩乐咖":
        resId = R.drawable.ic_haoqi_main;
        break;

      case "新生宝宝":
        resId = R.drawable.ic_xinshengbaby_main;
        break;
    }

    return resId;
  }

  public static int getGeneNameByMark(String mark) {
    int geneNameResId = -1;
    switch (mark) {
      case "小食神":
        geneNameResId = R.string.gene_en_name_chihuo;
        break;

      case "艺术家":
        geneNameResId = R.string.gene_en_name_wenyi;
        break;

      case "外交官":
        geneNameResId = R.string.gene_en_name_shejiao;
        break;

      case "时尚精":
        geneNameResId = R.string.gene_en_name_shishang;
        break;

      case "大财阀":
        geneNameResId = R.string.gene_en_name_tuhao;
        break;

      case "玩乐咖":
        geneNameResId = R.string.gene_en_name_haoqi;
        break;

      case "新生宝宝":
        geneNameResId = R.string.gene_en_name_baby;
        break;
    }

    return geneNameResId;
  }
}
