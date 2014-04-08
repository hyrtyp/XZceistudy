package com.hyrt.cei.ui.phonestudy;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.R;

/**
 * Created by yepeng on 13-9-13.
 */
public class BaseActivity extends ActivityGroup {

    @Override
    protected void onStart() {
        super.onStart();
        HomePageActivity.phoneStudyContainer.add(this);
    }

    @Override
    protected void onResume() {
        ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
        ImageResourse imageResource = new ImageResourse();
        imageResource.setIconUrl(columnEntry.getLogo());
        imageResource.setIconId(columnEntry.getLogo());
        ((CeiApplication) (this.getApplication())).asyncImageLoader.loadDrawable(imageResource,
                new AsyncImageLoader.ImageCallback() {

                    @Override
                    public void imageLoaded(Drawable drawable, String path) {
                        ImageView imageView = (ImageView)findViewById(R.id.phone_study_icon);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(int i=1;i<HomePageActivity.phoneStudyContainer.size();i++){
                                    HomePageActivity.phoneStudyContainer.get(i).finish();
                                }
                            }
                        });
                        imageView.setImageBitmap(((BitmapDrawable) drawable).getBitmap());
                    }
                });
        super.onResume();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.KEYCODE_SOFT_LEFT || event.getAction() == KeyEvent.KEYCODE_BACK){
            for(int i=0;i<HomePageActivity.phoneStudyContainer.size();i++){
                if(HomePageActivity.phoneStudyContainer.get(i) instanceof  HomePageActivity)
                    System.out.println();
                else
                    HomePageActivity.phoneStudyContainer.get(i).finish();
            }
            if(this instanceof HomePageActivity)
                this.finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HomePageActivity.phoneStudyContainer.remove(this);
    }
}
