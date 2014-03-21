package com.hyrt.ceiphone.phonestudy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.common.LoginActivityphone;
import com.hyrt.cei.ui.phonestudy.CourseDetailActivityphone;
import com.hyrt.cei.update.UpdateManager;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.adapter.GoodClassAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动学习模块主界面
 * 
 * @author 叶朋
 * 
 */
public class PhoneStudyActivity extends FoundationActivity {

	private Gallery goodReport;
	private ImageView point1, point2, point3, point4, point5;
	private TextView title;
	// 精彩课件
	private List<Courseware> topCoursewares;
	// 精彩课件离线文件名称
	private static final String WELLCLASS_FILENAME = "WELL_CLASS.xml";
	// 最新课件离线文件名称
	public static final String NEWCLASS_FILENAME = "NEW_CLASS.xml";
	// 精彩课件更新视图标志
	private static final int TOP_KEY = 10;

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(final Message msg) {
			switch (msg.arg1) {
			case TOP_KEY:
				if (topCoursewares.size() < 5) {
					topCoursewares.clear();
					topCoursewares.add(new Courseware());
					topCoursewares.add(new Courseware());
					topCoursewares.add(new Courseware());
					topCoursewares.add(new Courseware());
					topCoursewares.add(new Courseware());
				}
				GoodClassAdapter adapter = new GoodClassAdapter(
						PhoneStudyActivity.this, topCoursewares, goodReport);
				goodReport.setAdapter(adapter);
				goodReport.setSelection(Integer.MAX_VALUE / 2 - 3);
				point1.setBackgroundResource(R.drawable.read_report_index_select);
				if (topCoursewares.size() >= 5)
					if (topCoursewares.get(0).getName() != null) {
						title.setText(topCoursewares.get(0).getName().length() > 10 ? topCoursewares
								.get(0).getName().substring(0, 9)
								+ "..."
								: topCoursewares.get(0).getName());
					}
				break;
			case NO_NET:
				if (topCoursewares.size() < 5) {
					topCoursewares.clear();
					topCoursewares.add(new Courseware());
					topCoursewares.add(new Courseware());
					topCoursewares.add(new Courseware());
					topCoursewares.add(new Courseware());
					topCoursewares.add(new Courseware());
				}
				GoodClassAdapter adapters = new GoodClassAdapter(
						PhoneStudyActivity.this, topCoursewares, goodReport);
				goodReport.setAdapter(adapters);
				goodReport.setSelection(Integer.MAX_VALUE / 2 - 3);
				point1.setBackgroundResource(R.drawable.read_report_index_select);
				if (topCoursewares.size() >= 5)
					if (topCoursewares.get(0).getName() != null) {
						title.setText(topCoursewares.get(0).getName().length() > 10 ? topCoursewares
								.get(0).getName().substring(0, 9)
								+ "..."
								: topCoursewares.get(0).getName());

					}
				/*
				 * Toast.makeText(PhoneStudyActivity.this, "网络有问题!",
				 * Toast.LENGTH_SHORT).show();
				 */
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.CURRENT_KEY = FoundationActivity.NEW_DATA_KEY;
		setContentView(R.layout.phone_study);
        findViewById(R.id.main_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneStudyActivity.this, LoginActivityphone.class);
                startActivity(intent);
            }
        });
        UpdateManager manager = new UpdateManager(this);
        // 检查软件更新
        manager.isUpdate();
		goodReport = (Gallery) findViewById(R.id.read_report_main_ga);
		point1 = (ImageView) findViewById(R.id.read_report_point1);
		point2 = (ImageView) findViewById(R.id.read_report_point2);
		point3 = (ImageView) findViewById(R.id.read_report_point3);
		point4 = (ImageView) findViewById(R.id.read_report_point4);
		point5 = (ImageView) findViewById(R.id.read_report_point5);
		title = (TextView) findViewById(R.id.read_report_title);
		goodReport.setOnItemClickListener(this);
		goodReport.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Courseware report = (Courseware) arg0.getAdapter()
						.getItem(arg2);
				if (report.getName() != null) {
					title.setText(report.getName() == null ? "" : report
							.getName().length() > 10 ? report.getName()
							.substring(0, 9) + "..." : report.getName());
				} else {
					title.setText("");
				}
				if (arg2 % 5 == 0) {
					point1.setBackgroundResource(R.drawable.read_report_index_select);
					point2.setBackgroundResource(R.drawable.home_img_ratio);
					point3.setBackgroundResource(R.drawable.home_img_ratio);
					point4.setBackgroundResource(R.drawable.home_img_ratio);
					point5.setBackgroundResource(R.drawable.home_img_ratio);
				}
				if (arg2 % 5 == 1) {
					point2.setBackgroundResource(R.drawable.read_report_index_select);
					point1.setBackgroundResource(R.drawable.home_img_ratio);
					point3.setBackgroundResource(R.drawable.home_img_ratio);
					point4.setBackgroundResource(R.drawable.home_img_ratio);
					point5.setBackgroundResource(R.drawable.home_img_ratio);
				}
				if (arg2 % 5 == 2) {
					point3.setBackgroundResource(R.drawable.read_report_index_select);
					point1.setBackgroundResource(R.drawable.home_img_ratio);
					point2.setBackgroundResource(R.drawable.home_img_ratio);
					point4.setBackgroundResource(R.drawable.home_img_ratio);
					point5.setBackgroundResource(R.drawable.home_img_ratio);

				}
				if (arg2 % 5 == 3) {
					point4.setBackgroundResource(R.drawable.read_report_index_select);
					point1.setBackgroundResource(R.drawable.home_img_ratio);
					point3.setBackgroundResource(R.drawable.home_img_ratio);
					point2.setBackgroundResource(R.drawable.home_img_ratio);
					point5.setBackgroundResource(R.drawable.home_img_ratio);

				}
				if (arg2 % 5 == 4) {
					point5.setBackgroundResource(R.drawable.read_report_index_select);
					point1.setBackgroundResource(R.drawable.home_img_ratio);
					point3.setBackgroundResource(R.drawable.home_img_ratio);
					point4.setBackgroundResource(R.drawable.home_img_ratio);
					point2.setBackgroundResource(R.drawable.home_img_ratio);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
        getWellCourses();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Courseware report = (Courseware) adapter.getAdapter().getItem(position);
		if (report.getClassId() == null)
			return;
		Intent intent = new Intent(this, CourseDetailActivityphone.class);// OpenBookActivity
		intent.putExtra("coursewareInfo", report);
//		startActivity(intent);
	}

	/**
	 * 获取精彩课件的数据
	 */
	private void getWellCourses() {
		new Thread(new Runnable() {
			public void run() {
				topCoursewares = new ArrayList<Courseware>();
				ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
				String wellClassParentId = "";
//                if(1==1){
//                    return;
//                }
//				if (columnEntry.getColByName(WELLCLASS_NAME, columnEntry
//						.getColByName(FoundationActivity.MODEL_NAME).getId()) != null)
//					wellClassParentId = columnEntry.getColByName(WELLCLASS_NAME, columnEntry
//							.getColByName(FoundationActivity.MODEL_NAME).getId()).getId();
				if (((CeiApplication) getApplication()).isNet()) {
					String result = Service.queryPhoneFunctionTree(
							wellClassParentId, "kj");
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, topCoursewares);
						WriteOrRead.write(result, MyTools.nativeData,
								WELLCLASS_FILENAME);
						Message message = handler.obtainMessage();
						message.arg1 = TOP_KEY;
						handler.sendMessage(message);
					} else {
						Message message = handler.obtainMessage();
						message.arg1 = NO_NET;
						handler.sendMessage(message);
					}
				} else {
					String result = WriteOrRead.read(MyTools.nativeData,
							WELLCLASS_FILENAME);
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, topCoursewares);
						Message message = handler.obtainMessage();
						message.arg1 = TOP_KEY;
						handler.sendMessage(message);
					} else {
						Message message = handler.obtainMessage();
						message.arg1 = NO_NET;
						handler.sendMessage(message);
					}
				}
			};
		}).start();
	}

    // 根据登陆与否判断是否显示登陆按钮
    private void showLoginBtnByUserName() {
        // 获取登陆名
        SharedPreferences settings = getSharedPreferences("loginInfo",
                Activity.MODE_PRIVATE);
        String loginName = settings.getString("LOGINNAME", "");
        // 根据登录名来显示登陆按钮那个位置
        if (!loginName.equals("")) {
            ((TextView) findViewById(R.id.main_login_tv))
                    .setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.main_login_tv)).setText(loginName);
        }else{
            findViewById(R.id.main_login).setVisibility(View.VISIBLE);
        }
    }
}
