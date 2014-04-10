package com.hyrt.ceiphone.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.phonestudy.FoundationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于我们
 * 
 * @author Administrator
 * 
 */
public class Disclaimer extends FoundationActivity implements OnClickListener {
	private Intent intent;
	private ColumnEntry columnEntry;
	private String rs;
	public static final String MODEL_NAME = "关于我们";
	private List<InfoNew> news = new ArrayList<InfoNew>();
	private WebView view;
	private String htmlHade = MyTools.newsHtml;
	private String loginName;

    private Button abouts_us,usesinfo,mazminfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        CURRENT_KEY = ABOUT_DATA_KEY;
		setContentView(R.layout.disclaimer2);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		init();
		view = (WebView) findViewById(R.id.disclaimer_web);
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
	}

	private void init() {
        abouts_us=(Button)findViewById(R.id.abouts_us);
        abouts_us.setOnClickListener(this);
        usesinfo=(Button)findViewById(R.id.usesinfo);
        usesinfo.setOnClickListener(this);
        mazminfo=(Button)findViewById(R.id.mazminfo);
        mazminfo.setOnClickListener(this);
		refreshListData();
	}

	private void refreshListData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				columnEntry = ((CeiApplication) getApplication()).columnEntry;
//				if (columnEntry.getColByName(Disclaimer.MODEL_NAME) == null)
//					return;
				rs = Service
						.queryNewsByFunctionId(
								"", "", columnEntry.getUserId());
				XmlUtil.getNewsList(rs, news);
				news.size();
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		}).start();
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith(MODEL_NAME)) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
        super.onClick(v);
		switch (v.getId()) {
		case R.id.abouts_us:
            //换底色
            abouts_us.setBackgroundResource(R.drawable.phone_study_tab_bg);
            usesinfo.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            mazminfo.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            //换字
            abouts_us.setTextColor(getResources().getColor(R.color.phone_study_color_black));
            usesinfo.setTextColor(getResources().getColor(R.color.phone_study_color_White));
            mazminfo.setTextColor(getResources().getColor(R.color.phone_study_color_White));
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith(MODEL_NAME)) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
			break;
		case R.id.usesinfo:
            //换底色
            abouts_us.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            usesinfo.setBackgroundResource(R.drawable.phone_study_tab_bg);
            mazminfo.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            //换字
            abouts_us.setTextColor(getResources().getColor(R.color.phone_study_color_White));
            usesinfo.setTextColor(getResources().getColor(R.color.phone_study_color_black));
            mazminfo.setTextColor(getResources().getColor(R.color.phone_study_color_White));
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith("使用说明")) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
			break;
		case R.id.mazminfo:
            //换底色
            abouts_us.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            usesinfo.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            mazminfo.setBackgroundResource(R.drawable.phone_study_tab_bg);
            //换字
            abouts_us.setTextColor(getResources().getColor(R.color.phone_study_color_White));
            usesinfo.setTextColor(getResources().getColor(R.color.phone_study_color_White));
            mazminfo.setTextColor(getResources().getColor(R.color.phone_study_color_black));
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith("免责声明")) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
			break;
		}
	}
}
