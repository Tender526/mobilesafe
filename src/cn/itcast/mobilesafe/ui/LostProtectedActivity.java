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
		// �ж��û��Ƿ��Ѿ����������� 
		if(isPWDSteup()){
			Log.i(TAG,"����������,������½�ĶԻ���");
			showNormalEntryDialog();
		}else{
			Log.i(TAG,"û����������,��ʾ��һ�ζԻ���");
			showFirstEntryDialog();
		}
		
	}

	/**
	 * ������½�ĶԻ���
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
	 * ��һ�ν������ʱ��ĶԻ���
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
	 * ���sharedpreference�����Ƿ������������
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
				Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 0).show();
				return;
			}else{
				if(pwd.equals(pwd_confirm)){
					Editor editor = sp.edit();
					editor.putString("password", MD5Encoder.encode(pwd));
					editor.commit();
				}
				else{
					Toast.makeText(getApplicationContext(), "�������벻ͬ", 0).show();
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
				Toast.makeText(getApplicationContext(), "����������", 0).show();
				return;
			}else{
				String realpwd = sp.getString("password", "");
				if (realpwd.equals(MD5Encoder.encode(password))){
					
					// ����û�û�����ù�����������Ե��������򵼽���  ��Ȼ����ֱ�ӽ���������
					if(isSetupGuide()){
						Log.i(TAG,"�����ù������򵼣������ֻ�����������");
						
					}else{
						
						Log.i(TAG,"�����ֻ������ý���");
						enterInfoSetupguide(LostProtectedActivity.this,SetupGudie1Activity.class);
					}
				}else{
					Toast.makeText(getApplicationContext(), "�������", 0).show();
					return;
				}
			}
			dialog.dismiss();
			break;
		}
		
	}

	/**
	 * ����activity����
	 */
	private void enterInfoSetupguide(Context context,Class clazz) {
		finish();
		Intent intent = new Intent(context, clazz);
		startActivity(intent);
	}

	/**
	 * �Ƿ����ù�������
	 * @return
	 */
	private boolean isSetupGuide() {
		boolean isSetup = sp.getBoolean(Constants.IS_STEUP_ALREADY, false);
		
		return isSetup;
	}
	
}
