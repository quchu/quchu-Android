package co.quchu.quchu.baselist.Sample;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import co.quchu.quchu.baselist.Base.BasePagerApi;
import co.quchu.quchu.baselist.Base.BaseRecyclerViewActivity;
import co.quchu.quchu.baselist.Base.BaseViewHolderFactory;

public class MainActivity extends BaseRecyclerViewActivity {

  @Override public RecyclerView.LayoutManager getLayoutManager() {
    return new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
  }

  @Override public BasePagerApi getAPI() {
    return new WeatherApi("1132");
  }

  @Override public BaseViewHolderFactory getViewHolderFactory() {
    return new SimpleViewHolderFactory();
  }


}
