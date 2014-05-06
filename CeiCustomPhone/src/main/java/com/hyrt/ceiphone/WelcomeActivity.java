package com.hyrt.ceiphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.common.LoginActivityphone;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.TimeOutHelper;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.common.HomePageDZB;
import com.hyrt.ceiphone.phonestudy.SelfActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public class WelcomeActivity extends ContainerActivity {
	private ImageView progressIv;
	private AnimationDrawable animationDrawable;
	// 是否进入离线模式
	public static boolean isGoUnline;
	// 离线是否已经提示过了
	private boolean isNotice;
	private String str;
	private TextView tv;
	public static final String INITRESOURCES_FILENAME = "initResources.xml";
	public static final String INITSELFRESOURCES_FILENAME = "initSelfResources.xml";

    private ImageView loginImage;
    private RelativeLayout pb_loading;
    private TimeOutHelper timeOutHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome2);
        timeOutHelper = new TimeOutHelper(this);
//		isGoUnline = false;
//		installProAnim();
//		installData();
        pb_loading=(RelativeLayout)findViewById(R.id.pb_loading);
        loginImage=(ImageView)findViewById(R.id.login_bt2);
        loginImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeInitData();
            }
        });
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==2){
            welcomeInitData();
        }else{
            loginImage.setVisibility(View.VISIBLE);
            pb_loading.setVisibility(View.GONE);
        }
    }

    /**
     * 登录
     * */
    private void welcomeInitData(){
        loginImage.setVisibility(View.GONE);
        pb_loading.setVisibility(View.VISIBLE);
        isGoUnline = false;
        installProAnim();
        installData();
    }

    public static final int UPDATE_CENT = 1;
	public static final int GO_MAIN = 2;
	public static final int IS_NET = 3;
	public static final int NO_DATA = 4;
	public static final int USER_ERROR = 5;
	public static final int DEVICE_ERROR = 6;

    public static final int LS01=7;
    public static final int LF00=8;
    public static final int BE08=9;
    public static final int BE00=10;
    public static final int AE03=11;

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case UPDATE_CENT:
				// 更新百分数。
				str = msg.arg2 + "%";
				tv.setText(str);
				break;
			case GO_MAIN:
                if(((CeiApplication) getApplication()).isNet()){
                    Intent intent = new Intent(WelcomeActivity.this,
                            HomePageDZB.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this,
                            SelfActivity.class);
                    intent.putExtra("down",true);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
				break;
			case IS_NET:
				isNotice = true;
                alertIsSurePop("网络不通 \n 是否进入离线模式！",new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popWin.dismiss();
                        isGoUnline = true;
                        installData();
                    }
                });
				break;


			case NO_DATA:
                Message message = handler.obtainMessage();
                message.arg1 = GO_MAIN;
                handler.sendMessage(message);
				break;
			case DEVICE_ERROR:
                alertIsSurePop("设备号与用户不匹配 \n 请点确认进入默认版！", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popWin.dismiss();
                        WelcomeActivity.this.finish();
                    }
                });
				SharedPreferences settings1 = getSharedPreferences("loginInfo",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor1 = settings1.edit();
				editor1.putString("LOGINNAME", "");
				editor1.putString("PASSWORD", "");
				editor1.commit();
				break;
			case USER_ERROR:
                alertIsSurePop("用户名密码错误 \n 请重新登录!",new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popWin.dismiss();
                        startActivityForResult(new Intent(WelcomeActivity.this, LoginActivityphone.class), 1);
                    }
                });
                    SharedPreferences settings = getSharedPreferences("loginInfo",
                            Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("LOGINNAME", "");
                    editor.putString("PASSWORD", "");
                    editor.commit();
                    break;
                case BE00:
                    alertIsSurePop("登录失败 \n 请重新登录!",new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popWin.dismiss();
                            startActivityForResult(new Intent(WelcomeActivity.this, LoginActivityphone.class),1);
                        }
                    });
                    SharedPreferences settingsbe00 = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editorbe00 = settingsbe00.edit();
                    editorbe00.putString("LOGINNAME", "");
                    editorbe00.putString("PASSWORD", "");
                    editorbe00.commit();
                    break;

                case LS01:
                    SharedPreferences settingsbeLS01 = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editorbeLS01 = settingsbeLS01.edit();
                    editorbeLS01.putString("LOGINNAME", "");
                    editorbeLS01.putString("PASSWORD", "");
                    editorbeLS01.commit();

                    alertIsSurePop("用户已停用 \n 请重新登录!",new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popWin.dismiss();
                            startActivityForResult(new Intent(WelcomeActivity.this, LoginActivityphone.class),1);
                        }
                    });
                    break;
                case BE08:
                    SharedPreferences settingsbeBE08 = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editorbeBE08 = settingsbeBE08.edit();
                    editorbeBE08.putString("LOGINNAME", "");
                    editorbeBE08.putString("PASSWORD", "");
                    editorbeBE08.commit();
                    alertIsSurePop("网络平台发生异常 \n 请重新登录!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popWin.dismiss();
                            startActivityForResult(new Intent(WelcomeActivity.this, LoginActivityphone.class), 1);
                        }
                    });
                    break;

			}
		}
	};

	private void installData() {
		// 检查sd卡是否存在不存在的话，则退出
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "sd卡不存在！");
			this.finish();
			return;
		}
		final ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				// 如果判断用户是否有登陆
				SharedPreferences settings = getSharedPreferences("loginInfo",
						Activity.MODE_PRIVATE);
				if (settings.getString("LOGINNAME", "").equals("")) {
					columnEntry.setLoginName("");
					columnEntry.setPassword("");
				} else {
					columnEntry.setLoginName(settings
							.getString("LOGINNAME", ""));
					columnEntry.setPassword(settings.getString("PASSWORD", ""));
				}
				columnEntry.getColumnEntryChilds().clear();
				columnEntry.getSelectColumnEntryChilds().clear();
				if (((CeiApplication) getApplication()).isNet() && !isGoUnline) {
                    timeOutHelper.installTimerTask();
					// 请求初始化资源 50%
					long startTime = System.currentTimeMillis();
					String result = Service.initResources(columnEntry,
							WelcomeActivity.this);
					long endTime = System.currentTimeMillis();
                    timeOutHelper.uninstallTimerTask(null);
					if (endTime - startTime < 1000) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (XmlUtil.parseReturnCode(result).equals("AE03")) {//网络平台连接异常
						Message message = handler.obtainMessage();
						message.arg1 = IS_NET;
						handler.sendMessage(message);
						return;
					} else if (XmlUtil.parseReturnCode(result).equals("LF00")//登陆失败
							) {
						WriteOrRead.write(result, MyTools.nativeData,
								INITRESOURCES_FILENAME);
						XmlUtil.parseInitResources(result, columnEntry);
						Message message = handler.obtainMessage();
						message.arg1 = USER_ERROR;
						handler.sendMessage(message);
						return;
					} else if (XmlUtil.parseReturnCode(result).equals("2")) {
						Message message = handler.obtainMessage();
						WriteOrRead.write(result, MyTools.nativeData,
								INITRESOURCES_FILENAME);
						XmlUtil.parseInitResources(result, columnEntry);
						message.arg1 = DEVICE_ERROR;
						handler.sendMessage(message);
						return;
					}else if(XmlUtil.parseReturnCode(result).equals("LS01")){//用户停用
                        Message message = handler.obtainMessage();
                        message.arg1 = LS01;
                        handler.sendMessage(message);
                        return;
                    }else if(XmlUtil.parseReturnCode(result).equals("BE08")){//网络平台发生异常
                        Message message = handler.obtainMessage();
                        message.arg1 = BE08;
                        handler.sendMessage(message);
                        return;
                    }else if(XmlUtil.parseReturnCode(result).equals("BE00")){//异常
                        Message message = handler.obtainMessage();
                        message.arg1 = BE00;
                        handler.sendMessage(message);
                        return;
                    }else if(XmlUtil.parseReturnCode(result).equals("BE04")){//异常
                        Message message = handler.obtainMessage();
                        message.arg1 = BE00;
                        handler.sendMessage(message);
                        return;
                    }

					WriteOrRead.write(result, MyTools.nativeData,
							INITRESOURCES_FILENAME);
					XmlUtil.parseInitResources(result, columnEntry);
					Message message = handler.obtainMessage();
					message.arg1 = UPDATE_CENT;
					message.arg2 = 50;
					handler.sendMessage(message);
					// 请求个人资源100%
					result = Service.initSelfResources(columnEntry);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("XZUSERID",columnEntry.getXzuserid());
                    editor.commit();
					WriteOrRead.write(result, MyTools.nativeData,
							INITSELFRESOURCES_FILENAME);
					XmlUtil.parseInitSelfResources(result, columnEntry);

					// 请求智慧海业务
					// 获取版本号
					// 将设备号写入SDCARD中
					File deviceIdFile = new File(MyTools.RESOURCE_PATH
							+ "deviceId");
					if (!deviceIdFile.exists()) {
						FileWriter fw = null;
						try {
							fw = new FileWriter(deviceIdFile);
							TelephonyManager tm = (TelephonyManager) WelcomeActivity.this
									.getSystemService(Context.TELEPHONY_SERVICE);
							WifiManager wifi = (WifiManager) WelcomeActivity.this.getSystemService(Context.WIFI_SERVICE);
							WifiInfo info = wifi.getConnectionInfo();
							fw.write((info.getMacAddress()+tm.getDeviceId()));
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								fw.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					message = handler.obtainMessage();
					message.arg1 = UPDATE_CENT;
					message.arg2 = 100;
					handler.sendMessage(message);

				} else{
                    SharedPreferences settings1 = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
                    if(settings1.getString("LOGINNAME", "").equals("")) {
                        WelcomeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(WelcomeActivity.this,LoginActivityphone.class));
                                finish();

                            }
                        });

                        return;
                    }
					// 请求初始化资源 50%
					String result = WriteOrRead.read(MyTools.nativeData,
                            INITRESOURCES_FILENAME);
					if (!isNotice) {
						Message message = handler.obtainMessage();
						message.arg1 = IS_NET;
						handler.sendMessage(message);
						return;
					}
					if (result.equals("")
							|| XmlUtil.parseReturnCode(result).equals("-1")) {
						Message message = handler.obtainMessage();
						message.arg1 = NO_DATA;
						handler.sendMessage(message);
						return;
					} else if (XmlUtil.parseReturnCode(result).equals("-2")) {
						Message message = handler.obtainMessage();
						message.arg1 = DEVICE_ERROR;
						XmlUtil.parseInitResources(result, columnEntry);
						handler.sendMessage(message);
						return;
					}
					XmlUtil.parseInitResources(result, columnEntry);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("XZUSERID",columnEntry.getXzuserid());
                    editor.commit();
					Message message = handler.obtainMessage();
					message.arg1 = UPDATE_CENT;
					message.arg2 = 50;
					handler.sendMessage(message);
					// 请求个人资源100%
					result = WriteOrRead.read(MyTools.nativeData,
							INITSELFRESOURCES_FILENAME);
					XmlUtil.parseInitSelfResources(result, columnEntry);
					message = handler.obtainMessage();
					message.arg1 = UPDATE_CENT;
					message.arg2 = 100;
					handler.sendMessage(message);
				}
                Message message = handler.obtainMessage();
                if(columnEntry.getUserId() == null){
//                    startActivity(new Intent(WelcomeActivity.this, LoginActivityphone.class));
//                    WelcomeActivity.this.finish();
//                    return;
                }else{
                    message.arg1 = GO_MAIN;
                    handler.sendMessage(message);
                }

			}
		};
		new Thread(runnable).start();
	}

	private void installProAnim() {
		try {
			Typeface fontFace = Typeface.createFromAsset(getAssets(),
					"fonts/FZCQJW.TTF");
			tv = (TextView) findViewById(R.id.welcome_text_min);
			tv.setTypeface(fontFace);
			progressIv = (ImageView) findViewById(R.id.welcome_iv);
			animationDrawable = (AnimationDrawable) progressIv.getBackground();
			progressIv.getViewTreeObserver().addOnPreDrawListener(opdl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	OnPreDrawListener opdl = new OnPreDrawListener() {
		@Override
		public boolean onPreDraw() {
			animationDrawable.start();
			return true;
		}
	};

    private PopupWindow popWin;
    private void alertIsSurePop(String msg,View.OnClickListener clickListener) {
        View popView = this.getLayoutInflater().inflate(
                R.layout.phone_study_issure, null);
        ((TextView) popView.findViewById(R.id.issure_title))
                .setText(msg);
        popView.findViewById(R.id.phone_study_issure_sure_btn)
                .setOnClickListener(clickListener);
        popView.findViewById(R.id.phone_study_issure_cancel_btn)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popWin.dismiss();
                    }
                });
        popWin = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        popWin.setFocusable(true);
        popWin.showAtLocation(findViewById(R.id.welcome), Gravity.CENTER, 0,
                0);
    }

}