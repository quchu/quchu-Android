package co.quchu.quchu.net;

import org.json.JSONObject;
/**
 * 这是空请求 不期待回调结果
 */
public class ImplRequestListener implements IRequestListener {

	@Override
	public void onSuccess(JSONObject response) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(String status) {
		// TODO Auto-generated method stub
		return false;
	}

}
