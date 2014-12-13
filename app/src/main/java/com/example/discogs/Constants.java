package com.example.discogs;

public class Constants {

	public static final String CONSUMER_KEY 	= "gFFpqXDGTMvlFTRRtRMB";
	public static final String CONSUMER_SECRET 	= "uRUqFOoYyjpEAocYNKMIdmyXSQueFpkp";

	//public static final String SCOPE 			= "https://www.google.com/m8/feeds/";
	public static final String REQUEST_URL 		= "http://api.discogs.com/oauth/request_token";
	public static final String ACCESS_URL 		= "http://api.discogs.com/oauth/access_token";  
	public static final String AUTHORIZE_URL 	= "http://www.discogs.com/oauth/authorize";

	public static final String API_REQUEST 		= "http://api.discogs.com/users/sebasstuy/collection/folders/1/releases";

	public static final String ENCODING 		= "UTF-8";

	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
		
}
