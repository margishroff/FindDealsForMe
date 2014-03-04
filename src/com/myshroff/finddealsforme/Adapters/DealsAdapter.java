package com.myshroff.finddealsforme.Adapters;

 
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myshroff.finddealsforme.R;
import com.myshroff.finddealsforme.Cache.ImageLoader;



public class DealsAdapter extends BaseAdapter {

	private Activity activity;
	private String[] images;
	//private String dealInfo;
	private  ArrayList<HashMap<String, String>> mylist;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader; 

	public DealsAdapter(Activity a, String[] image_list, ArrayList<HashMap<String, String>> dealInfo ) {
		activity = a;
		images=image_list;
		mylist = dealInfo;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return images.length;
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
				vi = inflater.inflate(R.layout.item, null);

			TextView text=(TextView)vi.findViewById(R.id.text);
			ImageView image=(ImageView)vi.findViewById(R.id.image);
		 
			String dealDetails = mylist.get(position).get("source").toString() + "\n" +
			mylist.get(position).get("title").toString() + "\n" +
			mylist.get(position).get("price").toString() + "\t\t\t" +
			//mylist.get(position).get("discount").toString() + "\n" +  
			mylist.get(position).get("distance").toString() + " (" +
			mylist.get(position).get("city").toString() + ")" ;
			
			text.setText(dealDetails);
			text.setMaxHeight(70);
			
			imageLoader.DisplayImage(images[position], image);

		}catch(Exception e)        {
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return vi;
	}
}
