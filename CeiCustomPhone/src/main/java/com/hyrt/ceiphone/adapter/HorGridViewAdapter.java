package com.hyrt.ceiphone.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.ceiphone.R;

import java.util.List;

public class HorGridViewAdapter extends BaseAdapter {

	private List<ColumnEntry> columnEntries;
	private LayoutInflater inflater;
	private int width;
	private boolean isFristBlue = true;
    private Context context;

	public HorGridViewAdapter(Context context, List<ColumnEntry> columnEntries) {
        this.context = context;
		this.columnEntries = columnEntries;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		width = metric.widthPixels;
	}
	
	public HorGridViewAdapter(Context context, List<ColumnEntry> columnEntries,boolean isFristBlue) {
        this.context = context;
		this.columnEntries = columnEntries;
		this.isFristBlue = isFristBlue;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		width = metric.widthPixels;
	}

	@Override
	public int getCount() {
		return columnEntries.size();
	}

	@Override
	public Object getItem(int position) {
		return columnEntries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.phone_study_gridview_item2, null);
		item.setLayoutParams(new GridView.LayoutParams(width / 4,(int)context.getResources().getDimension(R.dimen.phone_study_custom_tabheight)));
        item.setGravity(Gravity.CENTER);
        item.setBackgroundResource(R.drawable.phone_study_tab_bg);
		TextView tv = (TextView) item.getChildAt(0);
        tv.setTextSize(15);
		if(position == 0 && isFristBlue){
            item.setBackgroundResource(R.drawable.phone_study_tab_bg2);
            tv.setTextColor(Color.WHITE);
		}else{
            tv.setTextColor(Color.BLACK);


		}
		tv.setText(columnEntries.get(position).getName());
		return item;
	}

}
