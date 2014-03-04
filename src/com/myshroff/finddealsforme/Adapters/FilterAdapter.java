package com.myshroff.finddealsforme.Adapters;
 
import java.util.ArrayList;

import com.myshroff.finddealsforme.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
 
//Currently the list is not being populated correctly to do the filtering
public class FilterAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<String> filter_list;
 
	private static LayoutInflater inflater=null;
 

	public FilterAdapter(Activity a, ArrayList<String> categorylist  ) {
		activity = a;
		filter_list = categorylist;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return filter_list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public void forceReload() {
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi=convertView;
		try
		{
			if(convertView==null)
				vi = inflater.inflate(R.layout.filter_item, null);

			CheckedTextView chkTextView = (CheckedTextView)vi.findViewById(R.id.categoryChk);
			chkTextView.setText(filter_list.get(position).toString());

			
		}catch(Exception e)        {
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return vi;
	}
}
