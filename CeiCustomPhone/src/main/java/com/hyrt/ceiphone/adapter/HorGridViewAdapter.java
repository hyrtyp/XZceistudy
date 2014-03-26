package com.hyrt.ceiphone.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
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

	public HorGridViewAdapter(Context context, List<ColumnEntry> columnEntries) {
		this.columnEntries = columnEntries;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		width = metric.widthPixels;
	}
	
	public HorGridViewAdapter(Context context, List<ColumnEntry> columnEntries,boolean isFristBlue) {
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
		item.setLayoutParams(new GridView.LayoutParams(width / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
		TextView tv = (TextView) item.getChildAt(1);
		if(position == 0 && isFristBlue){
//			 ((ImageView)item.getChildAt(0)).setImageResource(R.drawable.phone_study_menu_select);
            tv.setBackgroundResource(R.color.phone_study_kinds_bg);
			 tv.setTextColor(Color.WHITE);
		}else{
			tv.setTextColor(Color.BLUE);
		}
		tv.setText(columnEntries.get(position).getName());
		return item;
	}

}
