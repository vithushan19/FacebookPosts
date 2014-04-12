package com.example.facebookposts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	// Your Facebook APP ID
    private static String APP_ID = "653966461323456";
    private static String TAG = "VITHUSHAN";
    
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	
	TextView nameTextView;
	Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		
		nameTextView = (TextView) findViewById(R.id.textView_name);
		loginButton = (Button) findViewById(R.id.button_login);
		loginButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				login();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void login() {
		Log.i(TAG, "LOGIN");
		loginButton.setText(R.string.logout);
	    mPrefs = getPreferences(MODE_PRIVATE);
	    String access_token = mPrefs.getString("access_token", null);
	    long expires = mPrefs.getLong("access_expires", 0);
	 
	    if (access_token != null) {
	        facebook.setAccessToken(access_token);
	    }
	 
	    if (expires != 0) {
	        facebook.setAccessExpires(expires);
	    }
	 
	    if (!facebook.isSessionValid()) {
	        facebook.authorize(this,
	                new String[] { "email", "publish_stream" },
	                new DialogListener() {
	 
	                    @Override
	                    public void onCancel() {
	                        // Function to handle cancel event
	                    }
	 
	                    @Override
	                    public void onComplete(Bundle values) {
	                        // Function to handle complete event
	                        // Edit Preferences and update facebook acess_token
	                        SharedPreferences.Editor editor = mPrefs.edit();
	                        editor.putString("access_token",
	                                facebook.getAccessToken());
	                        editor.putLong("access_expires",
	                                facebook.getAccessExpires());
	                        editor.commit();
	                        getGraphData();
	                		loginButton.setOnClickListener( new OnClickListener() {

	                			@Override
	                			public void onClick(View v) {
	                				logout();
	                			}
	                			
	                		});
	                    }

						@Override
	                    public void onError(DialogError error) {
	                        // Function to handle error
	 
	                    }
	 
	                    @Override
	                    public void onFacebookError(FacebookError fberror) {
	                        // Function to handle Facebook errors
	 
	                    }
	 
	                });
	    }

	}
	
	@SuppressWarnings("deprecation")
	public void logout() {
		loginButton.setText(R.string.login);
	    mAsyncRunner.logout(this, new RequestListener() {
	        @Override
	        public void onComplete(String response, Object state) {
	            Log.d(TAG, "Logout from Facebook" + response);
	            if (Boolean.parseBoolean(response) == true) {
	                // User successfully Logged out
	            }
	    		loginButton.setOnClickListener( new OnClickListener() {

	    			@Override
	    			public void onClick(View v) {
	    				login();
	    			}
	    			
	    		});
	        }
	 
	        @Override
	        public void onIOException(IOException e, Object state) {
	        }
	 
	        @Override
	        public void onFileNotFoundException(FileNotFoundException e,
	                Object state) {
	        }
	 
	        @Override
	        public void onMalformedURLException(MalformedURLException e,
	                Object state) {
	        }
	 
	        @Override
	        public void onFacebookError(FacebookError e, Object state) {
	        }
	    });
	    

	}
	
    private void getGraphData() {
		// TODO Auto-generated method stub
		
	}

}
