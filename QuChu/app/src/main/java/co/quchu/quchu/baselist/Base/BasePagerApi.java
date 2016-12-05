package co.quchu.quchu.baselist.Base;

import rx.Observable;

/**
 * Created by Nico on 16/12/2.
 */

public abstract class BasePagerApi<A> {

  public abstract Observable<A> fetch(int pageNo);

}
