package com.hyrt.cei.ui.phonestudy;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
                                startActivity(new Intent(BaseActivity.this,HomePageActivity.class));
                                for(int i=0;i<HomePageActivity.phoneStudyContainer.size();i++){
                                    HomePageActivity.phoneStudyContainer.get(i).finish();
                                }
                            }
                        });
                        imageView.setImageBitmap(((BitmapDrawable) drawable).getBitmap());
                    }
                });
        super.onResume();
    }
}
