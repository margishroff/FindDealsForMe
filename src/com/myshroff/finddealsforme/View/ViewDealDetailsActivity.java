package com.myshroff.finddealsforme.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
 
 
import com.myshroff.finddealsforme.R;
import com.myshroff.finddealsforme.Cache.ImageLoader;
 
import android.widget.Button;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
 
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ViewDealDetailsActivity  extends Activity   {
	

	private Button filterButton;
	protected static final String FILTER_CATEGORY = null;
	protected static final int REQUEST_SELECTED_CATEGORY = 0;
	ArrayList<String> categorylist ;
	String categoryType;
	public ImageLoader imageLoader; //To Display image
	private Activity activity;
	ImageView deal_image;
	Button backButton, buyButton;
	String dealLink;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setupView();
        setContentView(R.layout.deal_details);
        
        Intent intent = getIntent();
        String imageURL,title,price,link,expiration,distance,description,name,website,address,discount;
        
        imageURL = intent.getStringExtra("IMAGE");
        title = intent.getStringExtra("DEAL_TITLE");
        price = intent.getStringExtra("DEAL_PRICE");
        link = intent.getStringExtra("LINK");
        expiration = intent.getStringExtra("EXPIRATION");
        distance = intent.getStringExtra("DISTANCE");
        description = intent.getStringExtra("DEAL_DESCRIPTION");
        name = intent.getStringExtra("MERCHANT_INFO");
        website = intent.getStringExtra("WEBSITE");
        address = intent.getStringExtra("ADDRESS");
        dealLink = intent.getStringExtra("LINK");
        discount = intent.getStringExtra("DISCOUNT");
        
        System.out.println("Expiration:"+ expiration);
        System.out.println("dealLink: "+ dealLink);
        
        //Toast.makeText(ViewDealDetailsActivity.this, dealLink,Toast.LENGTH_LONG).show(); 
        
        //Display Image
        /*
        imageLoader=new ImageLoader(activity.getApplicationContext());
        ImageView image=(ImageView)findViewById(R.id.deal_image);
		imageLoader.DisplayImage(imageURL, image);
        //Image Loader didn't work here so just pulling the image from URL
        
       */
       
        deal_image = (ImageView)findViewById(R.id.deal_image);
        
        try{
            Drawable image =ImageOperations(this,imageURL);
            deal_image.setImageDrawable(image);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }


        deal_image.setMinimumWidth(90);
        deal_image.setMinimumHeight(80);

        deal_image.setMaxWidth(90);
        deal_image.setMaxHeight(100);
        
         
        
        TextView titleTextView = (TextView)findViewById(R.id.deal_title);
        TextView priceTextView = (TextView)findViewById(R.id.price);
        TextView expirationTextView = (TextView)findViewById(R.id.expiration);
        TextView descriptionTextView = (TextView)findViewById(R.id.description);
        TextView nameTextView = (TextView)findViewById(R.id.name);
        TextView websiteTextView = (TextView)findViewById(R.id.website);
        TextView addressTextView = (TextView)findViewById(R.id.address);
        TextView discountTextView = (TextView)findViewById(R.id.discount);
        
        titleTextView.setText(title);
        priceTextView.setText(price);
        expirationTextView.setText(expiration);
        descriptionTextView.setText(description);
        nameTextView.setText(name);
        websiteTextView.setText(website);
        addressTextView.setText(address);
        discountTextView.setText(discount);
        
       setupView();
    }
    
    public Object fetch(String address) throws MalformedURLException,
    IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }  

    private Drawable ImageOperations(Context ctx, String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
    
    private void setupView(){
    	
    backButton = (Button)findViewById(R.id.backButton);
    buyButton = (Button)findViewById(R.id.buyButton);
	
    
    backButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
 
			finish();
		}
	});

    buyButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			
		
			Intent intent = new Intent(ViewDealDetailsActivity.this, WebViewBrowser.class);

			intent.putExtra("LINK",dealLink);
			startActivity(intent);
			//finish();
		}
	});
	 
}
}