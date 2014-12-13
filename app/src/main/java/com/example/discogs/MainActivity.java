package com.example.discogs;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.discogs.fragments.FragmentProfile;
import com.example.discogs.fragments.FragmentSearch;
import com.example.restexample.R;


// this Activity extends ListActivity so it indicates that it contains a listview object in its layout
// and can be populated with data with the setListAdapter() method 
public class MainActivity extends FragmentActivity {
	
	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

	private String[] menuTitles;
	private CharSequence mDrawerTitle;
    private CharSequence mTitle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
        menuTitles = getResources().getStringArray(R.array.menuTitles);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        // Set the adapter for the menu drawer list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_list_item, menuTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
            	selectItem(position);
            }
        });
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// Whenever invalidateOptionsMenu() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        Fragment fragment = null;
        
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_search:
        	fragment = new FragmentSearch();	
        	FragmentManager fragmentManager = getSupportFragmentManager();
        	fragmentManager.beginTransaction()
        			       .replace(R.id.content_frame, fragment)
        			       .commit();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
              
    }
    
	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Handle action buttons  
    	FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = null; 
		 		
		switch (position) {
        case 0:  
        	fragment = new FragmentSearch();
        	break;
		case 1:  
			fragment = new FragmentSearch();
			break;
		case 2:  
			fragment = new FragmentSearch();
			break;
		case 3:  
			fragment = new FragmentSearch();
			break;
		case 4:  
			fragment = new FragmentProfile();
			break;
		case 5:  
			fragment = new FragmentSearch();
			break;
    	}
		
    	// Insert the fragment by replacing any existing fragment
    	fragmentManager.beginTransaction()
    			       .replace(R.id.content_frame, fragment)
    			       .commit();

	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(menuTitles[position]);
	    mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	
	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    getActionBar().setTitle(mTitle);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
	
	
}
