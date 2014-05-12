package com.hyrt.ceiphone.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.phonestudy.FoundationActivity;
import com.hyrt.ceiphone.phonestudy.PhoneStudyActivity;
import com.hyrt.ceiphone.phonestudy.SelfActivity;

/**
 * 通知公告
 * 
 * @author Administrator
 * 
 */
public class AnnouncementRead extends FoundationActivity{
	private WebView view;
	private Intent intent;
	private String url;
	private String htmlHade = MyTools.noticeHtml;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcementread);
        findViewById(R.id.phone_study_morebtn).setVisibility(View.GONE);
        overridePendingTransition(R.anim.push_in, R.anim.push_out);
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
		intent = getIntent();
		url = intent.getStringExtra("extra")+"&t="+System.currentTimeMillis();
		view.loadUrl(htmlHade + url);
        ImageView spinner = (ImageView)findViewById(R.id.phone_study_more);
        spinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPopMore();
            }
        });

        findViewById(R.id.phone_study_back_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AnnouncementRead.this, PhoneStudyActivity.class));
                for (int i = 1; i < FoundationActivity.activitys.size(); i++) {
                    FoundationActivity.activitys.get(i).finish();
                }
            }
        });
	}

    private PopupWindow popWin;
    private void alertIsSurePopExit() {
        View popView = this.getLayoutInflater().inflate(
                R.layout.phone_study_issure, null);
        ((TextView) popView.findViewById(R.id.issure_title))
                .setText("确认注销帐号吗?");
        popView.findViewById(R.id.phone_study_issure_sure_btn)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<FoundationActivity.activitys.size();i++){
                            FoundationActivity.activitys.get(i).finish();
                        }
                        SharedPreferences settings = getSharedPreferences(
                                "loginInfo", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("LOGINNAME","");
                        editor.putString("PASSWORD", "");
                        editor.commit();
                        popWin.dismiss();
                    }
                });
        popView.findViewById(R.id.phone_study_issure_cancel_btn)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popWin.dismiss();
                    }
                });
        popWin = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        popWin.setFocusable(true);
        popWin.showAtLocation(findViewById(R.id.full_view), Gravity.CENTER, 0,
                0);
    }


    private PopupWindow popWinMore;
    private void alertPopMore() {
        View popView = this.getLayoutInflater().inflate(
                R.layout.more_ll, null);
        popView.findViewById(R.id.login_out).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertIsSurePopExit();
                popWinMore.dismiss();
            }
        });

        popView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popWinMore.dismiss();
            }
        });

        popView.findViewById(R.id.my_down).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnnouncementRead.this,SelfActivity.class));
                popWinMore.dismiss();
            }
        });
        popWinMore = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        popWinMore.setFocusable(true);
        popWinMore.setTouchable(true);
        popWinMore.setOutsideTouchable(true);
        popWinMore.showAtLocation(findViewById(R.id.phone_study_more), Gravity.BOTTOM|Gravity.LEFT,0,0);
    }

	protected void onPause() {
		super.onPause();
		AnnouncementRead.this.finish();
	}


}
