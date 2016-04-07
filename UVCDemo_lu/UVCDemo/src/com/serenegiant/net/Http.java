package com.serenegiant.net;
//
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
public class Http {
//	private static HttpUtils mHttpUtils;
//	private static HttpMethod method;
//	
//	public static void requestNet(Context context, String url, RequestCallBack<?> callBack) {
//		init(context, url);
//		mHttpUtils.send(method, url, callBack);
//	}
//	
//	public static void requestNet(Context context, String url, RequestParams params, RequestCallBack<?> callBack) {
//		init(context, url, params);
//		mHttpUtils.send(method, url, params, callBack);
//	}
//	
//	private static void init(Context context, String url) {
//		mHttpUtils = new HttpUtils();
//		mHttpUtils.configRequestRetryCount(10);
//		mHttpUtils.configTimeout(5000);
//		method = HttpMethod.POST;
//		Log.d("http-post:【" + context.getClass().getSimpleName() + "】", "url= " + url);
//	}
//	
//	private static void init(Context context, String url, RequestParams params) {
//		mHttpUtils = new HttpUtils();
//		mHttpUtils.configRequestRetryCount(10);
//		mHttpUtils.configTimeout(5000);
//		method = HttpMethod.POST;
//		
//		StringBuffer sb = new StringBuffer();
//		List<NameValuePair> list = params.getQueryStringParams();
//		for (int i = 0; i < list.size(); i++) {
//			sb.append(list.get(i).getName() + "=" + list.get(i).getValue());
//		}
//		Log.d("http-post:【" + context.getClass().getSimpleName() + "】",  "url= " + url + "  params: " + sb.toString());
//	}
}
