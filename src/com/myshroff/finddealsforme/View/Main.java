package com.myshroff.finddealsforme.View;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myshroff.finddealsforme.R;
import com.myshroff.finddealsforme.Adapters.DealsAdapter;
import com.myshroff.finddealsforme.Utils.JSONfunctions;


public class Main extends ListActivity implements LocationListener{


	private Button filterButton, mapButton;

	protected static final String DEAL_PRICE = "DEAL_PRICE";

	protected static final int REQUEST_SELECTED_CATEGORY = 0;
	ArrayList<String> categorylist ;
	ArrayList<String> filterlist ;
	String categoryType;
	private ListAdapter adapter;
	ArrayList<HashMap<String, String>> mylist;
	ArrayList<String> mapInfo;


	private LocationManager locationManager;
	private Location currentLocation; 
	private String locationString = "";

	TextView locationText  ;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.listplaceholder);

		//find current GPS location
		setUpLocation();

		//display deals around me
		loadAcitivity();

	}


	private void setUpLocation() {

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				60,
				5,
				this);

		currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		adapter.getItem(position).toString();


		//Toast.makeText(Main.this, mylist.get(position).get("price").toString(),Toast.LENGTH_SHORT).show(); 

		Intent intent = new Intent(Main.this, ViewDealDetailsActivity.class);

		intent.putExtra("IMAGE",mylist.get(position).get("image_thumb_retina").toString());
		intent.putExtra("DEAL_TITLE",mylist.get(position).get("title").toString());
		intent.putExtra("DEAL_PRICE",mylist.get(position).get("price").toString());
		intent.putExtra("LINK",mylist.get(position).get("link").toString());
		intent.putExtra("EXPIRATION",mylist.get(position).get("coupon_expiration").toString());
		intent.putExtra("DISTANCE",mylist.get(position).get("distance").toString());
		intent.putExtra("DEAL_DESCRIPTION",mylist.get(position).get("description").toString());
		intent.putExtra("MERCHANT_INFO",mylist.get(position).get("name").toString());
		intent.putExtra("WEBSITE",mylist.get(position).get("website").toString());
		intent.putExtra("ADDRESS",mylist.get(position).get("address").toString());

		//added discount as well
		intent.putExtra("DISCOUNT", mylist.get(position).get("discount").toString());

		startActivity(intent);
	}

	public void setupView()
	{

		filterButton = (Button)findViewById(R.id.filterbutton);
		mapButton = (Button)findViewById(R.id.mapbutton);
		locationText = (TextView)findViewById(R.id.location_text);

		locationString =  String.format(
				"@ %f, %f +/- %fm",
				currentLocation.getLatitude(),
				currentLocation.getLongitude(),
				currentLocation.getAccuracy());

		locationText.setText(locationString);

		filterButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Show filter screen
				Intent intent = new Intent(Main.this, ViewFilterActivity.class);	

				System.out.println("Top 1 Category list : " + categorylist.get(0));

				//pass list of categories pulled
				intent.putExtra("CATEGORY_LIST",categorylist);
				startActivityForResult(intent, REQUEST_SELECTED_CATEGORY);
				//finish();
			}
		});

		mapButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Show filter screen
				Intent intent = new Intent(Main.this, ViewMapActivity.class);	

				//also add current Lat & Long and array of lats,long,store name, deal info
				intent.putExtra("MAP_INFO", mapInfo);
				
				System.out.println("Map_info"+ mapInfo.toString());
				System.out.println("Current_lat"+ Double.toString(currentLocation.getLatitude()));
				System.out.println("CURR_LONG"+ Double.toString(currentLocation.getLongitude()));
				
				intent.putExtra("CURR_LAT" ,Double.toString(currentLocation.getLatitude()));
				intent.putExtra("CURR_LONG",Double.toString(currentLocation.getLongitude()));
 

				startActivity(intent);
			}
		});

	}

	private void getFilterSelected()
	{
		Intent intent = getIntent();
		if(intent != null)
		{
			filterlist = intent.getStringArrayListExtra("CATEGORY_SELECTED");

			if(filterlist != null)
				System.out.println("filterlist :"+ filterlist.toString());
		}


	}


	@Override
	protected void onResume() {
		//reload the page
		super.onResume();
		//finish();

		//finish();
		 loadAcitivity();
		// startActivity(getIntent());// finish();	 
	}

	@Override
	public void onLocationChanged(Location location) {

		locationString = String.format(
				"@ %f, %f +/- %fm",
				location.getLatitude(),
				location.getLongitude(),
				location.getAccuracy());

		locationText.setText(locationString);
	}

	public void loadAcitivity(){

		mylist = new ArrayList<HashMap<String, String>>();
		categorylist = new ArrayList<String>();
		filterlist = new ArrayList<String>();

		//get filters selected from filter activity
		getFilterSelected();


		double newLatitude,newLongitude;
		double currentLatitude,currentLongitude;

		double distance = 0.0; 

		mapInfo = new ArrayList<String>();
		//For now lat & long are hard coded
		// currentLatitude = 37.39337135179526;
		//currentLongitude = -121.96000099182129;

		//create current location object and assign the lat & long

		//Location currentLocation = new Location("Me");
		Location merchantLocation;

		//currentLocation.setLatitude(currentLatitude);
		//currentLocation.setLongitude(currentLongitude);

		currentLatitude = currentLocation.getLatitude();
		currentLongitude = currentLocation.getLongitude();

		//Initially for testing I was using zip code. Changed to use current location
		//JSONObject json = JSONfunctions.getJSONfromURL("http://buydeals.in/api.getDealsZip/95054/json/?");
		//http://buydeals.in/api.getDealsLatLon/{format}/?callback={callback}&lat={lat}&lon={lon}

		JSONObject json = JSONfunctions.getJSONfromURL("http://buydeals.in/api.getDealsLatLon/json/?lat=" 
				+ Double.toString(currentLatitude) 
				+ "&lon=" +Double.toString(currentLongitude) );

		String[] image_link_list = null;
		try{

			JSONArray  items = json.getJSONArray("items");
			image_link_list = new String[items.length()];

			for(int i=0;i<items.length();i++){						
				HashMap<String, String> map = new HashMap<String, String>();	

				JSONObject subitems = items.getJSONObject(i);
				JSONObject deal = subitems.getJSONObject("deal");
				JSONObject merchant = subitems.getJSONObject("merchant");

				/* Example of the JSON output
				 *  "deal": {
	            "id": "767013",
	            "source": "Groupon",
	            "title": "Save 84% at Allied Skin Institute",
	            "price": "$99",
	            "value": "$600",
	            "discount": "84%",
	            "description": "Six Laser Hair-Removal Treatments at Allied Skin Institute in San Jose (Up to 84% Off). Five Options Available.. Limit 1 per person, may buy 3 additional as gifts. Limit 1 per visit. Valid only for option purchased. 24hr cancellation notice required or fee up to Groupon price may apply. Valid only for select treatment areas. Must use all treatments on same area. May redeem across visits.",
	            "link": "http:\/\/buydeals.in\/deal\/767013\/?key=1&source=api",
	            "image": "https:\/\/s3.grouponcdn.com\/images\/site_images\/2149\/1737\/IMAGE-Allied-Skin-Institute_medium.jpg",
	            "image_thumb": "http:\/\/buydeals.in\/includes\/scripts\/timthumb.php?src=https%3A%2F%2Fs3.grouponcdn.com%2Fimages%2Fsite_images%2F2149%2F1737%2FIMAGE-Allied-Skin-Institute_medium.jpg&w=100&h=63&zc=1&q=90",
	            "image_thumb_retina": "http:\/\/buydeals.in\/includes\/scripts\/timthumb.php?src=https%3A%2F%2Fs3.grouponcdn.com%2Fimages%2Fsite_images%2F2149%2F1737%2FIMAGE-Allied-Skin-Institute_medium.jpg&w=200&h=126&zc=1&q=90",
	            "coupon_expiration": "2013-05-02T06:59:59Z",
	            "category": "Health & Beauty"
	        },
	        "merchant": {
	            "name": "Allied Skin Institute",
	            "website": "http:\/\/alliedskininstitute.com\/",
	            "address": "2730 Aborn Rd.",
	            "city": "San Jose",
	            "state": "CA",
	            "zip": "95121",
	            "latitude": "37.3136",
	            "longitude": "-121.793"
	        }
				 * 
				 * 
				 */

				System.out.println("deal: " + deal.getString("category"));

				if(filterlist != null && !filterlist.isEmpty())
					System.out.println("filterlist : "+ filterlist.toString());


				//ONLY ADD DEALS WHICH PASS THE FILTER CHECK OR IF NO FILTER HAS BEEN SELECTED YET
				if((filterlist == null) || (!filterlist.isEmpty() && filterlist.contains(deal.getString("category"))))
				{
					//deal detail
					map.put("id",  String.valueOf(i));

					map.put("source", "Source :" + deal.getString("source"));
					map.put("title",  deal.getString("title"));
					map.put("price", "Price :" + deal.getString("price"));
					map.put("discount", "Discount :" + deal.getString("discount"));

					map.put("description","Description : \n" + deal.getString("description"));
					map.put("link",deal.getString("link"));
					map.put("image",  deal.getString("image"));
					map.put("coupon_expiration", "Expires :" + deal.getString("coupon_expiration"));
					map.put("price", "Price :" + deal.getString("price"));
					map.put("category", deal.getString("category"));

					//first check if category already exists in the array if not then add
					if (categorylist.contains( deal.getString("category")))
					{
						//do nothing
					}
					else
					{
						categorylist.add(deal.getString("category"));
					}
					//merchant detail
					map.put("name", "Name : " + merchant.getString("name"));
					map.put("website","Website : " + merchant.getString("website"));
					map.put("address","Address: \n" + merchant.getString("address") + "\n"
							+ merchant.getString("city") + "\n"
							+ merchant.getString("state")+ "\n"
							+ merchant.getString("zip") );

					map.put("city", merchant.getString("city"));

					//Get lat & long to calculate distance from current location
					map.put("latitude",merchant.getString("latitude"));
					map.put("latitude",merchant.getString("latitude"));

					//concatenate details into a string for map display
					String mapInfodetails = merchant.getString("latitude")+"#"+
					merchant.getString("longitude") + "#" +
					merchant.getString("name") + "#" +
					deal.getString("title");

					mapInfo.add(mapInfodetails);

					newLatitude = Double.valueOf(merchant.getString("latitude"));
					newLongitude = Double.valueOf(merchant.getString("longitude"));

					merchantLocation = new Location("deal");

					merchantLocation.setLatitude(newLatitude);
					merchantLocation.setLongitude(newLongitude);

					//distance calculated in meters
					distance = currentLocation.distanceTo(merchantLocation);

					/* convert meters to miles
					 *  1 meter = 0.000621371192 miles
					 */
					distance = Math.round((distance * 0.000621371192));

					// map.put("distance", Double.toString(distance));
					map.put("distance", "Distance:" + Double.toString(distance) + "mi");
					map.put("image_thumb_retina",deal.getString("image_thumb_retina") );
					map.put("image",deal.getString("image") );
					//image link
					image_link_list[i] = deal.getString("image_thumb_retina");

					//Only include deals which are in 15 miles radius
					if(distance <= 15.0)
						mylist.add(map);
				}
			}		
		}catch(JSONException e)        {
			Log.e("log_tag", "Error parsing data "+e.toString());
		}

		//currently just display images and text
		adapter = new DealsAdapter(this,image_link_list,mylist);
		setListAdapter(adapter);

		setupView();
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}