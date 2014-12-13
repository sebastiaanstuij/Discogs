package com.example.discogs.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.discogs.Constants;
import com.example.discogs.JSONParser;
import com.example.discogs.Utils;
import com.example.discogs.oAuth.PrepareRequestTokenActivity;
import com.example.restexample.R;

public class FragmentProfile extends ListFragment {
	
	// ArrayList for ListView which stores all separate releases as hashmap entries
	protected ArrayList<HashMap<String, String>> releasesList = new ArrayList<HashMap<String, String>>();
	
	// JSON Node names of interest
	protected static final String TAG_RELEASES ="results";
	protected static final String TAG_TITLE ="title";
	
	final String TAG = getClass().getName();
	private SharedPreferences prefs;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_profile, container, false);

		Button launchOauth = (Button) view.findViewById(R.id.btn_launch_oauth);
		Button clearCredentials = (Button) view.findViewById(R.id.btn_clear_credentials);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


		launchOauth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(v.getContext(), PrepareRequestTokenActivity.class));
				//performApiCall();
			}
		});
			
		
		clearCredentials.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				clearCredentials();
				performApiCall();
			}
	    });
	          
		performApiCall();
		
		
		return view;
	}
	
	
	
	private void performApiCall() {

		if (Utils.isNetworkAvailable(getActivity())){		        		
			
			// start downloading the info in background thread:
			new DownloadInfoTask(getConsumer(this.prefs)).execute(Constants.API_REQUEST);
			
    	} else{
    		Context context = (Context)getActivity();
    		CharSequence text = "Internet Connection Lost";
    		int duration = Toast.LENGTH_SHORT;

    		Toast toast = Toast.makeText(context, text, duration);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}
		
	}

	
	
	
	
	
	private OAuthConsumer getConsumer(SharedPreferences prefs) {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		consumer.setTokenWithSecret(token, secret);
		return consumer;
	}
	
	
	
	protected void populateListView(ArrayList<HashMap<String, String>> result) {
    	//each list item needs its own xml file (list_item.xml)
    	ListAdapter adapter = new SimpleAdapter(getActivity(), result,
                R.layout.list_item,
                new String[] { TAG_TITLE }, new int[] {
                        R.id.label });
 
        setListAdapter(adapter);      
    }
	
	
	
	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()); //this
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}
	
	
	private class DownloadInfoTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
	    
		OAuthConsumer consumer;
		
		public DownloadInfoTask(OAuthConsumer consumer){
			this.consumer = consumer;
		}
		
		/** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() 
		 * @throws Exception */
	    protected ArrayList<HashMap<String, String>> doInBackground(String... urls) {
	        
	    	// first empty arraylist 
            releasesList.clear();
	    		        
	        // Creating JSON Parser instance (see JSONParser class)
	        JSONParser jParser = new JSONParser();
	        
	        // declaring JSONObject
	        JSONObject json;
	        
	    	// contacts JSONArray, used to store the response array from the api call
	        JSONArray releasesResponse = new JSONArray();
	 
	        try {
	        	
	        	 // getting JSONObject from URL (method of JSONParser)
		        json = jParser.getJSONFromUrl(urls[0], consumer);
		        		
	        	// Getting Array of all Releases from JSONObject
	            releasesResponse = json.getJSONArray(TAG_RELEASES);
	                   
	            // loop through all releases to get each item
	            for(int i = 0; i < releasesResponse.length(); i++){
	                JSONObject releaseItem = releasesResponse.getJSONObject(i);
	                 
	                // Storing each json item in variable
	                String id = releaseItem.getString(TAG_TITLE);
	                           
	                // creating new HashMap
	                HashMap<String, String> map = new HashMap<String, String>();
	                 
	                // adding each child node to HashMap key => value
	                map.put(TAG_TITLE, id);
	 
	                // adding HashMap to ArrayList
	                releasesList.add(map);
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
				Log.e(TAG, "Error executing request",e);
			}
	        
			   	
	    	return releasesList;
	    }
	    
	        
	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground(), an ArrayList (releasesList) */
	    protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
	        populateListView(result);		        
	    }
	}
	
	
	
	
}
