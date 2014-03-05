package com.hyrt.cei.ui.personcenter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.phonestudy.FoundationActivity;

/**
 * 个人中心
 * 
 * @author Administrator
 * 
 */
public class PersonCenter extends FoundationActivity implements OnClickListener {
	private Button person_info, qccount_info, change_password;
	private RelativeLayout re;
	private Intent intent;
	private String loginName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        CURRENT_KEY = PERSON_DATA_KEY;
		setContentView(R.layout.personcentered);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		init();
	}

	private void init() {
		findViewById(R.id.person_info).setOnClickListener(this);
		findViewById(R.id.qccount_info).setOnClickListener(this);
		findViewById(R.id.change_password).setOnClickListener(this);
		re = (RelativeLayout) findViewById(R.id.pc_re);
		person_info = (Button) findViewById(R.id.person_info);
		qccount_info = (Button) findViewById(R.id.qccount_info);
		change_password = (Button) findViewById(R.id.change_password);
		SwitchActivity(0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		PersonCenter.this.finish();
	}

	void SwitchActivity(int id) {
		re.removeAllViews();
		Intent intent = null;
		if (id == 0) {
			intent = new Intent(PersonCenter.this, PersonInfo.class);
		} else if (id == 1) {
			intent = new Intent(PersonCenter.this, QccountInfo.class);
		} else if (id == 2) {
			intent = new Intent(PersonCenter.this, ChangePassword.class);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window subActivity = getLocalActivityManager().startActivity(
				"subActivity", intent);
		re.addView(subActivity.getDecorView(), LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

	@Override
	public void onClick(View v) {
        super.onClick(v);
		switch (v.getId()) {
		case R.id.person_info:
            //换底色
            person_info.setBackgroundResource(R.drawable.phone_study_tab_bg);
            qccount_info.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            change_password.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            //换字
            person_info.setTextColor(getResources().getColor(R.color.phone_study_color_black));
            qccount_info.setTextColor(getResources().getColor(R.color.phone_study_color_White));
            change_password.setTextColor(getResources().getColor(R.color.phone_study_color_White));
			SwitchActivity(0);
			break;
		case R.id.qccount_info:
            person_info.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            qccount_info.setBackgroundResource(R.drawable.phone_study_tab_bg);
            change_password.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            //换字
            person_info.setTextColor(getResources().getColor(R.color.phone_study_color_White));
            qccount_info.setTextColor(getResources().getColor(R.color.phone_study_color_black));
            change_password.setTextColor(getResources().getColor(R.color.phone_study_color_White));
			SwitchActivity(1);
			break;
		case R.id.change_password:
            person_info.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            qccount_info.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            change_password.setBackgroundResource(R.drawable.phone_study_tab_bg);
            //换字
            person_info.setTextColor(getResources().getColor(R.color.phone_study_color_White));
            qccount_info.setTextColor(getResources().getColor(R.color.phone_study_color_White));
            change_password.setTextColor(getResources().getColor(R.color.phone_study_color_black));
			SwitchActivity(2);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		ContainerActivity.activities.remove(this);
		super.onDestroy();
	}
}
