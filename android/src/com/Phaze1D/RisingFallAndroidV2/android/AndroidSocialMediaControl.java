package com.Phaze1D.RisingFallAndroidV2.android;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl.SocialMediaConnectionDelegate;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Session.Builder;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;


public class AndroidSocialMediaControl implements SocialMediaConnectionDelegate {
	
	private AndroidLauncher androidLan;
	
	private final static String TWITTER_CONSUMER_KEY = "AbYsTP6OcOSkdHbjuHYateD7S"; // place your cosumer key here
    private final static String TWITTER_CONSUMER_SECRET = "REc4OaAyAd8JpqvvDvefkBswsiksyNW2FMPEdizhaklwbmTl5Y"; 
    private final static String TWITTER_ACCESS_TOKEN = "2800201136-f5p7J42sMXXrxU6taIyUAL5Cz3Yu8GH1ifC4Dty";
    private final static String TWITTER_ACCESS_TOKEN_SECRET = "CjizXSP2TvGWRbFxNzOTKZT9NXTnmBdjmntPNJy7HrVwe";
    
    private WebView twitterWebView;
	
	public AndroidSocialMediaControl(AndroidLauncher androidLan){
		this.androidLan = androidLan;
	}
	
	public AndroidSocialMediaControl(){
		
	}

	@Override
	public void androidFacebookClicked(final SocialMediaControl smc) {
		

		Session.StatusCallback statusCallback = new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {
				
				Log.d("DAVID VILLARREAL", "changing");
				
				if(session.isOpened()){
					smc.delegate.disableOther();
					Log.d("DAVID VILLARREAL", "log is open");
					facebookAfterLoggedIn(smc);
				}else if (session.isClosed()){
					Log.d("DAVID VILLARREAL", "log is closed");
				}
			}
		};
		
		Session.OpenRequest opR = new Session.OpenRequest(androidLan);
		opR.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		opR.setCallback(statusCallback);
				
		Session session = new Builder(androidLan).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || true) {
			Session.setActiveSession(session);			
			session.openForRead(opR);				
		}
		
	}
	
	//Handles the facebook sharing after logging in
	private void facebookAfterLoggedIn(final SocialMediaControl smc){
		Bundle params = new Bundle();
	    params.putString("name", "Facebook SDK for Android");
	    params.putString("caption", "Build great social apps and get more installs.");
	    params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	    params.putString("link", "https://developers.facebook.com/android");
	    params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	    WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(androidLan,Session.getActiveSession(),params)).setOnCompleteListener(new OnCompleteListener() {

	            @Override
	           public void onComplete(Bundle values,
	                FacebookException error) {
	            	smc.delegate.enableOther();
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                    	smc.delegate.sharedCalledBack(true);
	                        Toast.makeText(androidLan,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                    	smc.delegate.sharedCalledBack(false);
	                        Toast.makeText(androidLan.getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                	smc.delegate.sharedCalledBack(false);
	                    Toast.makeText(androidLan.getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                	smc.delegate.sharedCalledBack(false);
	                    Toast.makeText(androidLan.getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	                
	                Session.getActiveSession().closeAndClearTokenInformation();
	            }
	        })
	        .build();
	    
	    feedDialog.show();
		
	}

	@Override
	public void androidContactsClicked(final SocialMediaControl smc) {
		
		
	}

	@Override
	public void androidGoogleClicked(final SocialMediaControl smc) {
		
		
	}
	
	@Override
	public void androidTwitterClicked(final SocialMediaControl smc) {
		loadTwitterWebView();
	}
	
	private static Twitter twitter;
    private static RequestToken requestToken;
    private AccessToken accessToken;
	
	private void loadTwitterWebView() {
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//        builder.setOAuthAccessToken(TWITTER_ACCESS_TOKEN);
//        builder.setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();
		
        try {
			requestToken = twitter.getOAuthRequestToken("oauth://RisingFall");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		androidLan.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				MyWebDialog webD = new MyWebDialog(androidLan, requestToken.getAuthenticationURL());
				webD.setOnCompleteListener(new MyWebDialog.OnCompleteListener() {
					
					@Override
					public void onComplete(int status) {
						
						
					}

				});
				webD.show();
				
			}
		});
		
		
		
		
	}

	@Override
	public void androidVKClicked(final SocialMediaControl smc) {
		
		
	}

	@Override
	public void androidWeiboClicked(final SocialMediaControl smc) {
		
		
	}
	

}
