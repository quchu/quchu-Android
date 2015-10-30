package co.quchu.quchu.net;

import org.json.JSONObject;


public interface IRequestListener {
	/**
	 * 返回数据
	 * @param response
	 */
	void onSuccess(JSONObject response);
	boolean onError(String error);
}
