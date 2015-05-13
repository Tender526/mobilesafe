package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.util.Constants;
import cn.itcast.mobilesafe.util.MD5Encoder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LostProtectedActivity extends Activity implements OnClickListener {
	private static final String TAG = "LostProtectedActivity";
	private SharedPreferences sp;
	private Dialog dialog;
	private EditText et_pwd;
	private EditText et_pwd_confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		// 判断用户是否已经设置了密码 
		if(isPWDSteup()){
			Log.i(TAG,"设置了密码,正常登陆的对话框");
			showNormalEntryDialog();
		}else{
			Log.i(TAG,"没有设置密码,显示第一次对话框");
			showFirstEntryDialog();
		}
		
	}

	/**
	 * 正常登陆的对话框
	 */
	private void showNormalEntryDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
		//dialog.setContentView(R.layout.first_entry_dialog);
		View view = View.inflate(this, R.layout.normal_entry_dialog, null);
		et_pwd = (EditText) view.findViewById(R.id.et_normal_entry_pwd);
		Button bt_normal_ok = (Button) view.findViewById(R.id.bt_normal_dialog_ok);
		Button bt_normal_cancle =  (Button) view.findViewById(R.id.bt_normal_dialog_cancle);
		bt_normal_ok.setOnClickListener(this);
		bt_normal_cancle.setOnClickListener(this);
		dialog.setContentView(view);
		dialog.show();
		
	}


	/**
	 * 第一次进入程序时候的对话框
	 */
	private void showFirstEntryDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
		//dialog.setContentView(R.layout.first_entry_dialog);
		View view = View.inflate(this, R.layout.first_entry_dialog, null);
		et_pwd = (EditText) view.findViewById(R.id.et_first_entry_pwd);
		et_pwd_confirm = (EditText) view.findViewById(R.id.et_first_entry_pwd_confirm);
		Button bt_ok = (Button) view.findViewById(R.id.bt_first_dialog_ok);
		Button bt_cancle =  (Button) view.findViewById(R.id.bt_first_dialog_cancle);
		bt_ok.setOnClickListener(this);
		bt_cancle.setOnClickListener(this);
		dialog.setContentView(view);
		dialog.show();
	}


	/**
	 * 检查sharedpreference里面是否有密码的设置
	 * @return
	 */
	private boolean isPWDSteup(){
	   String password =	sp.getString("password",null);
	   if(password==null){
		   return false;
	   }else{
		   if("".equals(password)){
			   return false;
		   }else{
			   return true;
		   }
	   }
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_first_dialog_cancle:
			dialog.dismiss();
			break;
		case R.id.bt_first_dialog_ok:
			String pwd = et_pwd.getText().toString().trim();
			String pwd_confirm = et_pwd_confirm.getText().toString().trim();
			if("".equals(pwd)||"".equals(pwd_confirm)){
				Toast.makeText(getApplicationContext(), "密码不能为空", 0).show();
				return;
			}else{
				if(pwd.equals(pwd_confirm)){
					Editor editor = sp.edit();
					editor.putString("password", MD5Encoder.encode(pwd));
					editor.commit();
				}
				else{
					Toast.makeText(getApplicationContext(), "两次密码不同", 0).show();
					return;
				}
			}
			dialog.dismiss();
			break;
		case R.id.bt_normal_dialog_cancle:
			dialog.dismiss();
			break;
		case R.id.bt_normal_dialog_ok:
			String password = et_pwd.getText().toString().trim();
			if("".equals(password)){
				Toast.makeText(getApplicationContext(), "请输入密码", 0).show();
				return;
			}else{
				String realpwd = sp.getString("password", "");
				if (realpwd.equals(MD5Encoder.encode(password))){
					
					// 如果用户没有设置过设置向导则可以弹出设置向导界面  不然可以直接进入主界面
					if(isSetupGuide()){
						Log.i(TAG,"已设置过设置向导，加载手机防盗主界面");
						
					}else{
						
						Log.i(TAG,"进入手机向导设置界面");
						enterInfoSetupguide(LostProtectedActivity.this,SetupGudie1Activity.class);
					}
				}else{
					Toast.makeText(getApplicationContext(), "密码错误", 0).show();
					return;
				}
			}
			dialog.dismiss();
			break;
		}
		
	}

	/**
	 * 进入activity界面
	 */
	private void enterInfoSetupguide(Context context,Class clazz) {
		finish();
		Intent intent = new Intent(context, clazz);
		startActivity(intent);
	}

	/**
	 * 是否设置过设置向导
	 * @return
	 */
	private boolean isSetupGuide() {
		boolean isSetup = sp.getBoolean(Constants.IS_STEUP_ALREADY, false);
		
		return isSetup;
	}
	
}
