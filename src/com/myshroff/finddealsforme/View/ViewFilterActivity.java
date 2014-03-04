package com.myshroff.finddealsforme.View;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.myshroff.finddealsforme.R;
import com.myshroff.finddealsforme.Adapters.FilterAdapter;


public class ViewFilterActivity  extends ListActivity {


	protected static final int REQUEST_SELECTED_CATEGORY = 1;
	protected static final int RESULT_OK = 0;
	ArrayList<String> categorylist ;
	ArrayList<String> selected_categorylist = null;

	String categoryType;
	private ListAdapter adapter;
 
 
	/** Called when the activity is first created. */
	Button doneButton;
	CheckedTextView chkTextView;

	Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setupView();
		setContentView(R.layout.filterlist );

		//mylist = new ArrayList<HashMap<String, String>>();
		categorylist = new ArrayList<String>();
		selected_categorylist = new ArrayList<String>();

		ListView list = getListView(); //(ListView)findViewById(android.R.id.list);
		//display category
		Intent intent = getIntent();
		categorylist = intent.getStringArrayListExtra("CATEGORY_LIST");

		System.out.println("Category List : " );
		System.out.println("Top 1 Category list : " + categorylist.get(0));

		//currently just display images
		adapter = new FilterAdapter(this,categorylist);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		setupView();

	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

	//	Toast.makeText(ViewFilterActivity.this, adapter.getItem(position).toString(),Toast.LENGTH_SHORT).show(); 

		 chkTextView = (CheckedTextView) v
		.findViewById(R.id.categoryChk);
		
		if(chkTextView != null)
		{
			chkTextView.toggle();

			if(chkTextView.isChecked())
			{
				System.out.println("Selected Item : "+chkTextView.getText().toString());
				 selected_categorylist.add(chkTextView.getText().toString());
			}
			else
			{
				 selected_categorylist.remove(chkTextView.getText().toString());
			}
		}
	}

	public void setupView()
	{
		
		doneButton = (Button)findViewById(R.id.donebutton);
		chkTextView= (CheckedTextView)findViewById(R.id.categoryChk);

		doneButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				System.out.println("categorylist contains : "+ selected_categorylist.toString());
				
				//send filters selected back to main screen/activity
				Intent intent = new Intent(ViewFilterActivity.this, Main.class);
				intent.putExtra("CATEGORY_SELECTED", selected_categorylist);
				setResult(RESULT_OK, intent);
				 
				startActivity(intent);
				finish();
			}
		});

		 
	}
}