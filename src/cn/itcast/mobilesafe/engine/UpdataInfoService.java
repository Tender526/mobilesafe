package cn.itcast.mobilesafe.engine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.UpdataInfo;

public class UpdataInfoService {
	private static final String tag = "UpdataInfoService";
	private Context context ;
	

	public UpdataInfoService(Context context) {
		this.context = context;
	}


	/**
	 * 
	 * @param urlid ������·��string��Ӧ��id
	 * @return ���µ���Ϣ
	 */
	public UpdataInfo getUpdataInfo(int urlid) throws Exception{
		String path = context.getResources().getString(urlid);
		Log.i(tag, "��ȡ��ַ:"+path);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		Log.i(tag, "conn===="+conn);
		conn.setConnectTimeout(2000);
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		Log.i(tag, "��ȡ��is===="+is);
		return  UpdataInfoParser.getUpdataInfo(is);
	}
	
}
