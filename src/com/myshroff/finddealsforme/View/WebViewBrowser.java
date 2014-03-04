package com.myshroff.finddealsforme.View;


import com.myshroff.finddealsforme.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class WebViewBrowser extends Activity {
	private class MyWebViewClient extends WebViewClient {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
          view.loadUrl(url);
          return true;
      }
  }
	private WebView webView;
 
	private String deal_link ;
 //   private Button goButton;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Intent intent = getIntent();
		deal_link = intent.getStringExtra("LINK");
		
		
		// Create reference to UI elements
        webView  = (WebView) findViewById(R.id.webview_compontent);
 
       // goButton = (Button)findViewById(R.id.go_button);
        
 
        
        // workaround so that the default browser doesn't take over
        webView.setWebViewClient(new MyWebViewClient());
        
        if(!deal_link.isEmpty() && deal_link.contains("http://"))
        {
        	webView.loadUrl(deal_link);
        }
        else
        {
        	//No screen to display
        }
        
    	webView.requestFocus();
    	
        /*
        // Setup click listener
        goButton.setOnClickListener( new OnClickListener() {
        	public void onClick(View view) {
        		openURL();
        	}
        });
        */
 
 
    }
    
    /** Opens the URL in a browser */
    private void openURL() {
    	webView.loadUrl(deal_link);
    	webView.requestFocus();
    }    
}
