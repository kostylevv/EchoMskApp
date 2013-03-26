package com.unionfind.android.echomskapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.example.echomskapp.R;

public class MainActivity extends ListActivity {
	
	private static final String URL = "http://sevkin.ru/flights.xml";
	private static ArrayList<Trip> trips = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        loadPage(); //trying to load page
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean isOnline() {
		ConnectivityManager connectivityManager = (ConnectivityManager) 
							getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;		
	}
	
	/**
	 * Shows flights list if connection established or displays error layout
	 */
	private void loadPage(){
		if (isOnline()){
			displayLoading();
			new DownloadXmlTask().execute(URL);
		} else {
			displayError();
		}
	}
	
	/**
	 * Changes layout visibility in abnormal situations (shows error msg and try again button)
	 */
	private void displayError(){
		this.getListView().setVisibility(View.INVISIBLE);
		LinearLayout errorLayout = (LinearLayout) this.findViewById(R.id.error_layout);
		errorLayout.setVisibility(View.VISIBLE);
		LinearLayout downloadLayout = (LinearLayout) this.findViewById(R.id.download_layout);
		downloadLayout.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Changes layouts visibility to downloading mode (shows spinner)
	 */
	private void displayLoading(){
		this.getListView().setVisibility(View.INVISIBLE);
		LinearLayout errorLayout = (LinearLayout) this.findViewById(R.id.error_layout);
		errorLayout.setVisibility(View.INVISIBLE);
		LinearLayout downloadLayout = (LinearLayout) this.findViewById(R.id.download_layout);
		downloadLayout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Changes layouts visibility to normal, i.e. to display list and sorting buttons
	 */
	private void displayList(){
		this.getListView().setVisibility(View.VISIBLE);
		LinearLayout errorLayout = (LinearLayout) this.findViewById(R.id.error_layout);
		errorLayout.setVisibility(View.INVISIBLE);
		LinearLayout downloadLayout = (LinearLayout) this.findViewById(R.id.download_layout);
		downloadLayout.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * displaySorted(String param) - sorts array in adapter according to given parameter
	 * @param param - field to sort by: price or duration
	 */
	
	private void displaySorted(String param){
		if (trips != null){ // Checks if we already have our flights list
			TripsAdapter adapter = new TripsAdapter(MainActivity.this, R.id.programTitle, trips);
			if (param == "price"){
				adapter.sort(new Comparator<Trip>() { 
					public int compare(Trip arg0, Trip arg1) { 
        	        	Double change1 = Double.valueOf(arg0.getPrice());
        	        	Double change2 = Double.valueOf(arg1.getPrice());
        	        	return change1.compareTo(change2);
					}
				});
			} else if (param == "duration"){
				adapter.sort(new Comparator<Trip>() {
					public int compare(Trip arg0, Trip arg1) {
						return arg0.getDuration().compareTo(arg1.getDuration());
					}
				});
			}
			
			MainActivity.this.getListView().setAdapter(adapter);
		} else {
			displayError(); 
		}
	}
	
	
    private class DownloadXmlTask extends AsyncTask<String, Void, ArrayList<Trip>> {

        @Override
        protected ArrayList<Trip> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
            	Log.v("Exception: ", e.toString());
            	return null;
            } catch (XmlPullParserException e) {
                Log.v("Exception: ", e.toString());
            	return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Trip> result) {
            if (result != null) {
            	TripsAdapter adapter = new TripsAdapter(MainActivity.this, R.id.programTitle, result);
            	trips = result;	
            	MainActivity.this.getListView().setAdapter(adapter);
                displayList();
            } else {
            	displayError(); 
            }
        }
    }

    /**
     * loadXmlFromNetwork(String urlString) - loads XML from network and parses it
     * 
     * @param urlString - URL to XML file to parse
     * @return trips - ArrayList with flight entries
     * @throws XmlPullParserException
     * @throws IOException
     */
    private ArrayList<Trip> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        CleverPumpkinParser cleverPumpkinParser = new CleverPumpkinParser();
        ArrayList<Trip> trips = new ArrayList<Trip>();
  
        try {
            stream = downloadUrl(urlString);
            trips = cleverPumpkinParser.parse(stream);      
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return trips;
    }

    /**
     * @param urlString - URL to get InputStream
     * @return stream - InputStrim from a given URL
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(20000);
        conn.setConnectTimeout(20000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }
    
}
