package com.myshroff.finddealsforme.View;

import java.util.ArrayList;
import java.util.List;
 
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
 
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
 
 
public class ViewMapActivity extends  MapActivity {
	
	
  private MapView map=null;
  private MyLocationOverlay me=null;
  Button listButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
	  
    super.onCreate(savedInstanceState);
    setContentView(R.layout.display_map);
    
    map=(MapView)findViewById(R.id.map);
    
   
    Intent intent = getIntent();
    
    Double currentLat, currentLong;
 
    currentLat = Double.valueOf( intent.getStringExtra("CURR_LAT")); 
    currentLong = Double.valueOf( intent.getStringExtra("CURR_LONG")); 
    
    System.out.println("lat"+currentLat);
    System.out.println("long"+currentLong);
    
    
    map.getController().setCenter(getPoint(Double.valueOf(currentLat),Double.valueOf(currentLong)));
   // map.getController().setCenter(getPoint(37.422006,-122.084095));
    
    map.getController().setZoom(10);
    map.setBuiltInZoomControls(true);
   
    /*
    Drawable marker=getResources().getDrawable(R.drawable.marker);
    Drawable deal_marker=getResources().getDrawable(R.drawable.rest_marker);
    
    marker.setBounds(0, 0, marker.getIntrinsicWidth(),
                            marker.getIntrinsicHeight());
    
    deal_marker.setBounds(0, 0, marker.getIntrinsicWidth(),
            marker.getIntrinsicHeight());
    
     map.getOverlays().add(new SitesOverlay(marker));
    
     me=new MyLocationOverlay(this, map);
     map.getOverlays().add(me);
    
    */
    
 
    /*
    map.getController().setCenter(getPoint(37.422006,-122.084095));
    map.getController().setZoom(10);
    map.setBuiltInZoomControls(true);
    */
 
    Drawable marker=getResources().getDrawable(R.drawable.me_marker);
    Drawable deal_marker=getResources().getDrawable(R.drawable.rest_marker);
    
    
    marker.setBounds(0, 0, marker.getIntrinsicWidth(),
                            marker.getIntrinsicHeight());
    
    deal_marker.setBounds(0, 0, marker.getIntrinsicWidth(),
            marker.getIntrinsicHeight());
    
    
    map.getOverlays().add(new SitesOverlay(deal_marker));
    
    me=new MyLocationOverlay(this, map);
    map.getOverlays().add(me);
    
    setupView();
  }
  
  public void setupView()
	{
		listButton = (Button)findViewById(R.id.list_button);

		listButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//Show filter screen
				 Intent intent = new Intent(ViewMapActivity.this, Main.class);	
				 startActivity(intent);
				 finish();
			}
		});
	}

  
  @Override
  public void onResume() {
    super.onResume();
    
    me.enableCompass();
  }   
  
  @Override
  public void onPause() {
    super.onPause();
    
     me.disableCompass();
  }   
  
  @Override
  protected boolean isRouteDisplayed() {
    return(false);
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_S) {
      map.setSatellite(!map.isSatellite());
      return(true);
    }
    else if (keyCode == KeyEvent.KEYCODE_Z) {
      map.displayZoomControls(true);
      return(true);
    }
    
    return(super.onKeyDown(keyCode, event));
  }

  private GeoPoint getPoint(double lat, double lon) {
    return(new GeoPoint((int)(lat*1000000.0),
                          (int)(lon*1000000.0)));
  }
    
  private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
    private List<OverlayItem> items=new ArrayList<OverlayItem>();
    
    public SitesOverlay(Drawable marker) {
      super(marker);
      
      boundCenterBottom(marker);
      
       
      Intent intent = getIntent();
 
      ArrayList<String> dealLocations = (intent.getStringArrayListExtra("MAP_INFO"));
      
      System.out.println("dealLocations: " + dealLocations.toString());
      int totaldeals = dealLocations.size();
      String[] currentdealInfo = new String[totaldeals];
      
       
      for(int i=0;i<totaldeals;i++)
      {
    	  currentdealInfo =  dealLocations.get(i).split("#");
    	  
    	  System.out.println("currentdeal:"+currentdealInfo[0] + " , " +currentdealInfo[1] +currentdealInfo[2] +currentdealInfo[3]  );
    	  
    	  //currentdealInfo : 0 - lat, 1 - longitude, 2 - name of merchant, 3 - deal
    	  items.add(new OverlayItem(getPoint(Double.valueOf(currentdealInfo[0]),
    			  					Double.valueOf(currentdealInfo[1])),
    			  					currentdealInfo[2], currentdealInfo[3]));               
      }
  
      
      populate();
    }
    
   
    
    
    @Override
    protected OverlayItem createItem(int i) {
      return(items.get(i));
    }
    
    @Override
    protected boolean onTap(int i) {
      Toast.makeText(ViewMapActivity.this ,
                      items.get(i).getSnippet(),
                      Toast.LENGTH_SHORT).show();
      
      return(true);
    }
    
    @Override
    public int size() {
      return(items.size());
    }
  }
}