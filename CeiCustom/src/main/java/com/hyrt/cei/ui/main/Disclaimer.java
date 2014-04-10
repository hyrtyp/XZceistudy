package com.hyrt.cei.ui.main;

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
import android.widget.TextView;

import com.hyrt.cei.R;
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
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.webservice.service.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 免责声明
 * 
 * @author Administrator
 * 
 */
public class Disclaimer extends BaseActivity implements OnClickListener {
	private Intent i;
	private ColumnEntry columnEntry;
	private String rs;
	public static final String MODEL_NAME = "关于我们";
	private List<InfoNew> news = new ArrayList<InfoNew>();
	private WebView view;
	private String htmlHade = MyTools.newsHtml;
	// 用户名
	private String loginName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disclaimer);
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
		findViewById(R.id.abouts_us).setOnClickListener(this);
		findViewById(R.id.usesinfo).setOnClickListener(this);
		findViewById(R.id.mazminfo).setOnClickListener(this);
        TextView headIv = (TextView) findViewById(R.id.phone_study_notice);
        TextView newIv = (TextView) findViewById(R.id.phone_study_new);
        TextView nominateIv = (TextView) findViewById(R.id.phone_study_nominate);
        TextView freeIv = (TextView) findViewById(R.id.phone_study_free);
        TextView kindIv = (TextView) findViewById(R.id.phone_study_kind);
        TextView selfIv = (TextView) findViewById(R.id.phone_study_self);
        TextView studyIv = (TextView) findViewById(R.id.phone_study_study);
        TextView sayIv = (TextView) findViewById(R.id.phone_study_say);
        TextView personcenterIv = (TextView) findViewById(R.id.phone_study_personcenter);
        personcenterIv.setOnClickListener(this);
        TextView aboutIv = (TextView) findViewById(R.id.phone_study_about);

        findViewById(R.id.phone_study_about).setBackgroundResource(R.drawable.pad_bottom_tv_select);
        aboutIv.setTextColor(getResources().getColor(R.color.pad_bottomandtop_bg));


//        aboutIv.setOnClickListener(this);
        headIv.setOnClickListener(this);
        newIv.setOnClickListener(this);
        nominateIv.setOnClickListener(this);
        freeIv.setOnClickListener(this);
        kindIv.setOnClickListener(this);
        selfIv.setOnClickListener(this);
        studyIv.setOnClickListener(this);
        sayIv.setOnClickListener(this);
		refreshListData();
	}

	private void refreshListData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				columnEntry = ((CeiApplication) getApplication()).columnEntry;
//				if(columnEntry.getColByName(Disclaimer.MODEL_NAME) == null)
//					return;
				rs = Service.queryNewsByFunctionId(
                        "",
                        "",
                        columnEntry.getUserId());
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

	protected void onPause() {
		super.onPause();
		Disclaimer.this.finish();
	}

	@Override
	public void onClick(View v) {
        Intent intent = null;
		switch (v.getId()) {
            case R.id.phone_study_notice:
                intent = new Intent(this, Announcement.class);
                if (!loginName.equals(""))
                    startActivity(intent);
                else
                    MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "请登陆后查看！");
                break;
            case R.id.phone_study_downmanager:
                intent = new Intent(this,
                        PreloadActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_study_new :
                intent = new Intent(this,HomePageActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_study_nominate :
                intent = new Intent(this,NominateActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_study_free :
                intent = new Intent(this,FreeActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_study_kind :
                intent = new Intent(this,KindsActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_study_self :
                intent = new Intent(this,SelfSelectCourseActivity.class);
                if (!loginName.equals(""))
                    startActivity(intent);
                else
                    MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "请登陆后查看！");
                break;
            case R.id.phone_study_study :
                intent = new Intent(this,PlayRecordCourseActivity.class);
                if (!loginName.equals(""))
                    startActivity(intent);
                else
                    MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "请登陆后查看！");
                break;
            case R.id.phone_study_say :
                intent = new Intent(this,SayGroupListActivity.class);
                if (!loginName.equals(""))
                    startActivity(intent);
                else
                    MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "请登陆后查看！");
                break;
            case R.id.phone_study_personcenter:
                intent = new Intent(this, PersonCenter.class);
                if (!loginName.equals(""))
                    startActivity(intent);
                else
                    MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "请登陆后查看！");
                break;
            case R.id.phone_study_about:
                intent = new Intent(this, Disclaimer.class);
                if (!loginName.equals(""))
                    startActivity(intent);
                else
                    MyTools.exitShow(this,
                            ((Activity)this).getWindow()
                                    .getDecorView(), "请登陆后查看！");
                break;
		case R.id.abouts_us:
            ((Button)findViewById(R.id.abouts_us)).setBackgroundResource(R.drawable.pad_study_tab_bg);
            ((Button)findViewById(R.id.usesinfo)).setBackgroundResource(R.drawable.pad_study_tab_bg2);
            ((Button)findViewById(R.id.mazminfo)).setBackgroundResource(R.drawable.pad_study_tab_bg2);

            ((Button)findViewById(R.id.abouts_us)).setTextColor(getResources().getColor(R.color.pad_study_color_black));
            ((Button)findViewById(R.id.usesinfo)).setTextColor(getResources().getColor(R.color.pad_study_color_White));
            ((Button)findViewById(R.id.mazminfo)).setTextColor(getResources().getColor(R.color.pad_study_color_White));
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith(MODEL_NAME)) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
			break;
		case R.id.usesinfo:
            ((Button)findViewById(R.id.abouts_us)).setBackgroundResource(R.drawable.pad_study_tab_bg2);
            ((Button)findViewById(R.id.usesinfo)).setBackgroundResource(R.drawable.pad_study_tab_bg);
            ((Button)findViewById(R.id.mazminfo)).setBackgroundResource(R.drawable.pad_study_tab_bg2);

            ((Button)findViewById(R.id.abouts_us)).setTextColor(getResources().getColor(R.color.pad_study_color_White));
            ((Button)findViewById(R.id.usesinfo)).setTextColor(getResources().getColor(R.color.pad_study_color_black));
            ((Button)findViewById(R.id.mazminfo)).setTextColor(getResources().getColor(R.color.pad_study_color_White));
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith("使用说明")) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
			break;
		case R.id.mazminfo:
            ((Button)findViewById(R.id.abouts_us)).setBackgroundResource(R.drawable.pad_study_tab_bg2);
            ((Button)findViewById(R.id.usesinfo)).setBackgroundResource(R.drawable.pad_study_tab_bg2);
            ((Button)findViewById(R.id.mazminfo)).setBackgroundResource(R.drawable.pad_study_tab_bg);

            ((Button)findViewById(R.id.abouts_us)).setTextColor(getResources().getColor(R.color.pad_study_color_White));
            ((Button)findViewById(R.id.usesinfo)).setTextColor(getResources().getColor(R.color.pad_study_color_White));
            ((Button)findViewById(R.id.mazminfo)).setTextColor(getResources().getColor(R.color.pad_study_color_black));
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
