package com.example.discogs.fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discogs.JSONParser;
import com.example.discogs.Utils;
import com.example.restexample.R;

public class FragmentSearch extends ListFragment {

	
	// Sample url to make API request to
	protected static String baseURL = "http://api.discogs.com/";
	protected static String searchURL =	"database/search?type=release&q=";	
	protected String searchQuery;

	// ArrayList for ListView which stores all separate releases as hashmap entries
	protected ArrayList<HashMap<String, String>> releasesList = new ArrayList<HashMap<String, String>>();
		    
	// JSON Node names of interest
	protected static final String TAG_RELEASES ="results";
	protected static final String TAG_TITLE ="title";

	
	// first method to be called by Android OS in a fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_search, container, false);
		
    	final EditText edittext = (EditText) view.findViewById(R.id.editText1);
		edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
		    	// If the event is a search event:
		        if (actionID == EditorInfo.IME_ACTION_SEARCH) {
		        
//		        	/* hide keyboard */
//		        	((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
//		            .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		        	
		        	searchQuery = edittext.getText().toString();
		        	
		        	String escapedURL = "";
		        	if (Utils.isNetworkAvailable(getActivity())){		        		
						try {
							// remove white space from URL:
							escapedURL = baseURL + searchURL + URLEncoder.encode(searchQuery, "UTF-8");
							// start downloading the info in background thread:
							new DownloadInfoTask().execute(escapedURL);
						} catch (UnsupportedEncodingException e) {
							Log.e(escapedURL,Log.getStackTraceString(e));
						}
		        	} else{
		        		Context context = (Context)getActivity();
		        		CharSequence text = "Internet Connection Lost";
		        		int duration = Toast.LENGTH_SHORT;

		        		Toast toast = Toast.makeText(context, text, duration);
		        		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		        		toast.show();
		        	}
		          return true;
		        }
		        return false;
		    }
		});	
		return view;
	}
	
	// after activity is intialized this method is called by Android OS, and checks whether there is any savedInstanceState data to be loaded
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
//        //show softkeyboard after creating activity
//        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
//	    .toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);

        
        if (savedInstanceState != null) {
        	// Restore state members from saved instance
    	    this.releasesList = (ArrayList<HashMap<String, String>>)savedInstanceState.getSerializable("releases");
    	    populateListView(releasesList);      	
		}        
	}
	
	// this method is called whenever a configuration change takes place (screen rotation) to save specific data 
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    super.onSaveInstanceState(savedInstanceState);
	    savedInstanceState.putSerializable("releases", releasesList);
	}
	
	
	protected void populateListView(ArrayList<HashMap<String, String>> result) {
    	//each list item needs its own xml file (list_item.xml)
    	ListAdapter adapter = new SimpleAdapter(getActivity(), result,
                R.layout.list_item,
                new String[] { TAG_TITLE }, new int[] {
                        R.id.label });
 
        setListAdapter(adapter);      
    }
		
	
	private class DownloadInfoTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
	    
		
		/** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() */
	    protected ArrayList<HashMap<String, String>> doInBackground(String... urls) {
	        
	    	// first empty arraylist 
            releasesList.clear();
	    		        
	        // Creating JSON Parser instance (see JSONParser class)
	        JSONParser jParser = new JSONParser();
	        
	        // getting JSONObject from URL (method of JSONParser)
	        JSONObject json = jParser.getJSONFromUrl(urls[0]);
	        
	    	// contacts JSONArray, used to store the response array from the api call
	        JSONArray releasesResponse = new JSONArray();
	 
	        try {
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
