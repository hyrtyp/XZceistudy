package com.hyrt.cei.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.AnnouncementListAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.phonestudy.BaseActivity;
import com.hyrt.cei.ui.phonestudy.FreeActivity;
import com.hyrt.cei.ui.phonestudy.HomePageActivity;
import com.hyrt.cei.ui.phonestudy.KindsActivity;
import com.hyrt.cei.ui.phonestudy.NominateActivity;
import com.hyrt.cei.ui.phonestudy.PlayRecordCourseActivity;
import com.hyrt.cei.ui.phonestudy.PreloadActivity;
import com.hyrt.cei.ui.phonestudy.SayGroupListActivity;
import com.hyrt.cei.ui.phonestudy.SelfSelectCourseActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.AnnouncementNews;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.webservice.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 通知公告
 * 
 * @author Administrator
 * 
 */
public class Announcement extends BaseActivity implements OnClickListener {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private String htmlHade = MyTools.noticeHtml;
	private ListView list;
	private List<AnnouncementNews> announcementNews;
	private Intent i;
	private WebView view;
	private String loginName;
	private boolean isload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		init();
		view = (WebView) findViewById(R.id.tzgg_web);
		view.getSettings().setDefaultTextEncodingName("gbk");
		WebSettings webSettings = view.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		view.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				view.loadUrl(url);// 载入网页
				return true;
			}// 重写点击动作,用webview载入
		});
		SharedPreferences settings1 = getSharedPreferences("announcementCount",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings1.edit();
		editor.putInt("old", settings1.getInt("new", 0));
		editor.commit();
	}

	public void init() {
        TextView headIv = (TextView) findViewById(R.id.phone_study_notice);
        TextView newIv = (TextView) findViewById(R.id.phone_study_new);
        TextView nominateIv = (TextView) findViewById(R.id.phone_study_nominate);
        TextView freeIv = (TextView) findViewById(R.id.phone_study_free);
        TextView kindIv = (TextView) findViewById(R.id.phone_study_kind);
        TextView selfIv = (TextView) findViewById(R.id.phone_study_self);
        TextView studyIv = (TextView) findViewById(R.id.phone_study_study);
        TextView sayIv = (TextView) findViewById(R.id.phone_study_say);
        TextView personcenterIv = (TextView) findViewById(R.id.phone_study_personcenter);

        findViewById(R.id.phone_study_notice).setBackgroundResource(R.drawable.pad_bottom_tv_select);
        headIv.setTextColor(getResources().getColor(R.color.pad_bottomandtop_bg));

//        headIv.setOnClickListener(this);
        TextView aboutIv = (TextView) findViewById(R.id.phone_study_about);
        aboutIv.setOnClickListener(this);
        newIv.setOnClickListener(this);
        nominateIv.setOnClickListener(this);
        freeIv.setOnClickListener(this);
        kindIv.setOnClickListener(this);
        selfIv.setOnClickListener(this);
        studyIv.setOnClickListener(this);
        sayIv.setOnClickListener(this);
        personcenterIv.setOnClickListener(this);
		list = (ListView) findViewById(R.id.tzgg_list);
		refreshListData();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				for (int i = 0; i < list.getChildCount(); i++) {
					if (i == position)
						list.getChildAt(i).setBackgroundColor(Color.WHITE);
					else
						list.getChildAt(i).setBackgroundDrawable(null);
				}
				view.loadUrl(htmlHade + announcementNews.get(position).getId());
			}
		});


		
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			AnnouncementListAdapter adapter = new AnnouncementListAdapter(
					Announcement.this, R.layout.tzgg_list_item,
					announcementNews);
			list.setAdapter(adapter);
			if(!isload)
				view.loadUrl("file:///android_asset/load.html");
			isload = true;
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if (announcementNews.size() > 0) {
						view.loadUrl(htmlHade + announcementNews.get(0).getId());
					}
				}
			}, 1000);
			
		}
	};

	private void refreshListData() {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				announcementNews = new ArrayList<AnnouncementNews>();
				String rs = "";
				ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
				rs = Service.queryNotice(columnEntry.getUserId());
				try {
					announcementNews = XmlUtil.getAnnouncement(rs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		});
	}

	@Override
	public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.phone_study_study:
                    intent = new Intent(this, PlayRecordCourseActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_downmanager:
                    intent = new Intent(this, PreloadActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_new:
                    intent = new Intent(this, HomePageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_nominate:
                    intent = new Intent(this, NominateActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_free:
                    intent = new Intent(this, FreeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_kind:
                    intent = new Intent(this, KindsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_self:
                    intent = new Intent(this, SelfSelectCourseActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_refresh:
                    intent = new Intent(this, PlayRecordCourseActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_say:
                    intent = new Intent(this, SayGroupListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_personcenter:
                    intent = new Intent(this, PersonCenter.class);
                    startActivity(intent);
                    break;
                case R.id.phone_study_about:
                    intent = new Intent(this, Disclaimer.class);
                    startActivity(intent);
                    break;
            }
	}
	
	@Override
	protected void onDestroy() {
		executorService.shutdown();
		super.onDestroy();
	}

}
