package com.hyrt.cei.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.common.HomePageDZB;

/**
 * 登录界面
 * 
 */
public class LoginActivityphone extends ContainerActivity implements OnClickListener{
	private int i1, i2;
	private EditText accountEt;
	private EditText passwordEt;
	// 用户名
	private String loginName;

	@Override
	protected void onDestroy() {
//		HomePageDZB.commonActivities.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login2);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		for (int i = 0; i < HomePageDZB.commonActivities.size(); i++) {
			try {
				LoginActivityphone isLoginActivityphone = (LoginActivityphone) (HomePageDZB.commonActivities
						.get(i));
				isLoginActivityphone.finish();
			} catch (Exception e) {
			}
		}
		HomePageDZB.commonActivities.add(this);
		accountEt = (EditText) findViewById(R.id.ui_login_username_et);
		passwordEt = (EditText) findViewById(R.id.ui_login_password_et);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		// accountEt.setText(settings.getString("LOGINNAME", ""));
		// passwordEt.setText(settings.getString("PASSWORD", ""));
		accountEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i1 == 0) {
					accountEt.setText("");
					i1++;
				}
			}
		});
		passwordEt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i2 == 0) {
					passwordEt.setText("");
					i2++;
				}
			}
		});
		findViewById(R.id.ui_login_regist_tv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(LoginActivityphone.this,
								RegistActivity.class);
						startActivity(intent);
					}
				});
        //设置下划线
        TextView ulrtv=(TextView) findViewById(R.id.ui_login_regist_tv);
        ulrtv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        TextView fgpd=(TextView) findViewById(R.id.ui_getpassword_tv);
        fgpd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        findViewById(R.id.ui_getpassword_tv).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivityphone.this,
                                GetpasswordActivity.class);
                        startActivity(intent);
                    }
                });
        Button ui_login_login_bt=(Button)findViewById(R.id.ui_login_login_bt);
        ui_login_login_bt.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		LoginActivityphone.this.finish();
	}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ui_login_login_bt:
                SharedPreferences settings = getSharedPreferences(
                        "loginInfo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                if (accountEt.getText().toString().trim().equals("")
                        || passwordEt.getText().toString().trim()
                        .equals("")) {
                    Toast.makeText(LoginActivityphone.this, "用户名密码不能为空!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putString("LOGINNAME", accountEt.getText()
                        .toString().trim());
                editor.putString("PASSWORD", passwordEt.getText()
                        .toString().trim());
                editor.commit();
//						Intent intent = new Intent(LoginActivityphone.this,
//								WelcomeActivity.class);
//						startActivity(intent);
                Intent intentdata= new Intent();
                intentdata.putExtra("codeid","200");
                setResult(2,intentdata);
                LoginActivityphone.this.finish();
                break;
        }
    }
}
