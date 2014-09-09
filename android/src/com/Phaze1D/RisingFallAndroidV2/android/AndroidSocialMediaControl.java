package com.Phaze1D.RisingFallAndroidV2.android;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
	
	public AndroidSocialMediaControl(AndroidLauncher androidLan){
		this.androidLan = androidLan;
	}
	
	public AndroidSocialMediaControl(){
		
	}

	@Override
	public void androidFacebookClicked() {
		

		Session.StatusCallback statusCallback = new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {
				
				Log.d("DAVID VILLARREAL", "changing");
				
				if(session.isOpened()){
					Log.d("DAVID VILLARREAL", "log is open");
					facebookAfterLoggedIn();
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
	
	private void facebookAfterLoggedIn(){
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
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(androidLan,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(androidLan.getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(androidLan.getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(androidLan.getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }
	        })
	        .build();
	    
	    feedDialog.show();
		
	}

	@Override
	public void androidContactsClicked() {
		
		
	}

	@Override
	public void androidGoogleClicked() {
		
		
	}

	@Override
	public void androidTwitterClicked() {
		
		
	}

	@Override
	public void androidVKClicked() {
		
		
	}

	@Override
	public void androidWeiboClicked() {
		
		
	}
	

}
