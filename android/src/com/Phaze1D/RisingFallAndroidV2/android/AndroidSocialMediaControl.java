package com.Phaze1D.RisingFallAndroidV2.android;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl.SocialMediaConnectionDelegate;
import com.Phaze1D.RisingFallAndroidV2.android.SocialWebDialog.SocialCompleteListener;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class AndroidSocialMediaControl implements SocialMediaConnectionDelegate {

	private AndroidLauncher androidLan;

	private final String TWITTER_CONSUMER_KEY = "AbYsTP6OcOSkdHbjuHYateD7S";
	private final String TWITTER_CONSUMER_SECRET = "REc4OaAyAd8JpqvvDvefkBswsiksyNW2FMPEdizhaklwbmTl5Y";

	private Twitter twitter;
	private RequestToken requestToken;
	private AccessToken accessToken;

	public AndroidSocialMediaControl(AndroidLauncher androidLan) {
		this.androidLan = androidLan;
	}

	public AndroidSocialMediaControl() {

	}

	@Override
	public void androidFacebookClicked(final SocialMediaControl smc) {

		if (isConnectingToInternet()) {

			Session.StatusCallback statusCallback = new Session.StatusCallback() {

				@Override
				public void call(Session session, SessionState state,
						Exception exception) {

					Log.d("DAVID VILLARREAL", "changing");

					if (session.isOpened()) {
						smc.delegate.disableOther();
						Log.d("DAVID VILLARREAL", "log is open");
						facebookAfterLoggedIn(smc);
					} else if (session.isClosed()) {
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
		} else {
			postErrorMessage();
		}

	}

	// Handles the facebook sharing after logging in
	private void facebookAfterLoggedIn(final SocialMediaControl smc) {
		Bundle params = new Bundle();
		params.putString("name", "Facebook SDK for Android");
		params.putString("caption",
				"Build great social apps and get more installs.");
		params.putString(
				"description",
				"The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
		params.putString("link", "https://developers.facebook.com/android");
		params.putString("picture",
				"https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

		final WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
				androidLan, Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {

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
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								smc.delegate.sharedCalledBack(false);
								Toast.makeText(
										androidLan.getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							smc.delegate.sharedCalledBack(false);
							Toast.makeText(androidLan.getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							smc.delegate.sharedCalledBack(false);
							Toast.makeText(androidLan.getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}

						Session.getActiveSession()
								.closeAndClearTokenInformation();
					}
				}).build();

		if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
			Log.d("DAVID VILLARREAL", "RUNNING ON MAIN THREAD");
			feedDialog.show();
		} else {
			Log.d("DAVID VILLARREAL", " NOT RUNNING ON MAIN THREAD");
			androidLan.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					feedDialog.show();
				}
			});
		}

	}

	@Override
	public void androidContactsClicked(final SocialMediaControl smc) {

	}

	@Override
	public void androidGoogleClicked(final SocialMediaControl smc) {

	}

	@Override
	public void androidTwitterClicked(final SocialMediaControl smc) {
		if (isConnectingToInternet()) {
			androidLan.appControl.pause();
			loadTwitterLogin();
		} else {
			postErrorMessage();
		}
	}

	private void loadTwitterLogin() {

		new RequestURLClass().execute("null");
	}

	// Post a tweet on twitter
	private void postToTwitter(final String oauth) {
		Thread accessThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					accessToken = twitter.getOAuthAccessToken(requestToken,
							oauth);

					// Update status
					twitter4j.Status response = twitter
							.updateStatus("Testing my tweet2");

				} catch (TwitterException e) {
					e.printStackTrace();

				}
			}
		});
		accessThread.start();

	}

	private void logoutTwitter() {
		CookieSyncManager.createInstance(androidLan);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();

		twitter.setOAuthAccessToken(null);

	}

	// Request URL async task class
	private class RequestURLClass extends AsyncTask<String, String, String> {

		String url;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {

			androidLan.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					pDialog = new ProgressDialog(androidLan);
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(false);
					pDialog.show();
				}
			});

		}

		@Override
		protected String doInBackground(String... params) {

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter
						.getOAuthRequestToken("oauth://RisingFall");

			} catch (TwitterException e) {

				e.printStackTrace();
			}

			url = requestToken.getAuthenticationURL();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			final String url2 = url;

			androidLan.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					SocialWebDialog twitterLoginDialog = new SocialWebDialog(
							androidLan, url2);
					twitterLoginDialog
							.setSocialComplete(new SocialCompleteListener() {

								@Override
								public void loginComplete(int status,
										String oauth) {

									if (status == SocialWebDialog.VERIFIED) {
										postToTwitter(oauth);
									} else if (status == SocialWebDialog.NOT_VERIFIED) {
										postErrorMessage();
										androidLan.appControl.resume();
									}
								}

							});
					twitterLoginDialog.show();
					pDialog.dismiss();
				}
			});

		}

	}

	private class UpdatingTwitter extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			return null;
		}

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected void onPostExecute(String result) {
			
		}

	}

	@Override
	public void androidVKClicked(final SocialMediaControl smc) {

	}

	@Override
	public void androidWeiboClicked(final SocialMediaControl smc) {

	}

	private void postErrorMessage() {

	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) androidLan
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
}
