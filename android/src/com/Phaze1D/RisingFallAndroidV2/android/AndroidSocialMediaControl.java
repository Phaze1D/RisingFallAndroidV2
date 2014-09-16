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
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl.SocialMediaConnectionDelegate;
import com.Phaze1D.RisingFallAndroidV2.android.MyVKShareDialog.MyVKShareDialogListener;
import com.Phaze1D.RisingFallAndroidV2.android.SocialShareDialog.OnCompleteSharing;
import com.Phaze1D.RisingFallAndroidV2.android.SocialWebLoginDialog.SocialCompleteListener;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.wearable.internal.m;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;

public class AndroidSocialMediaControl implements
		SocialMediaConnectionDelegate, ConnectionCallbacks,
		OnConnectionFailedListener {

	private AndroidLauncher androidLan;

	private final String TWITTER_CONSUMER_KEY = "AbYsTP6OcOSkdHbjuHYateD7S";
	private final String TWITTER_CONSUMER_SECRET = "REc4OaAyAd8JpqvvDvefkBswsiksyNW2FMPEdizhaklwbmTl5Y";
	private String verifier;

	private SocialMediaControl smc;

	private Twitter twitter;
	private RequestToken requestToken;
	private AccessToken accessToken;

	private static final String[] sMyScope = new String[] { VKScope.WALL };

	public GoogleApiClient googleClient;
	private ConnectionResult mConnectionResult;
	public GoogleConnectClass googleConnect;

	public AndroidSocialMediaControl(AndroidLauncher androidLan,
			SocialMediaControl smc) {
		this.androidLan = androidLan;
		this.smc = smc;
	}

	public AndroidSocialMediaControl() {

	}

	@Override
	public void androidFacebookClicked() {

		if (isConnectingToInternet()) {

			Session.StatusCallback statusCallback = new Session.StatusCallback() {

				@Override
				public void call(Session session, SessionState state,
						Exception exception) {

					Log.d("DAVID VILLARREAL", "changing");

					if (session.isOpened()) {
						smcDisable();
						Log.d("DAVID VILLARREAL", "log is open");
						facebookAfterLoggedIn();
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
	private void facebookAfterLoggedIn() {
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
						smcEnable();
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {

								postSuccessUpdate();
								Toast.makeText(androidLan,
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								postErrorMessage();
								Toast.makeText(
										androidLan.getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							postErrorMessage();
							Toast.makeText(androidLan.getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							postErrorMessage();
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
	public void androidContactsClicked() {

	}

	@Override
	public void androidGoogleClicked() {
		if (isConnectingToInternet()) {
			smcDisable();
			googleClient = new GoogleApiClient.Builder(androidLan)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).addApi(Plus.API)
					.addScope(Plus.SCOPE_PLUS_LOGIN).build();

			googleConnect = new GoogleConnectClass();

			googleConnect.execute(AndroidLauncher.GOOGLE_RC_SIGN_IN);

		} else {
			postErrorMessage();
		}

	}



	public void googleDidShare() {
		postSuccessUpdate();
		smcEnable();
		if (googleClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(googleClient);
			googleClient.disconnect();
		}
	}

	public void googleDidNotShare() {
		postErrorMessage();
		smcEnable();
		if (googleClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(googleClient);
			googleClient.disconnect();

		}
	}
	
	public void newGoogleConnect(){
		googleConnect = new GoogleConnectClass();
	}
	
	private void googleCreateShare(){
		Intent shareIntent = new PlusShare.Builder(androidLan)
        .setType("text/plain")
        .setText("Welcome to the Google+ platform.")
        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
        .getIntent();

		androidLan.startActivityForResult(shareIntent, AndroidLauncher.GOOGLE_RC_SHARE);
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d("DAVID", "CONNECTED GOODLE");
		
	}
	

	@Override
	public void onConnectionSuspended(int cause) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d("DAVID", "CONNECTION FALILED GOODLE");
		mConnectionResult = result;
	}

	private void resolveSignInError() {
		Log.d("DAVID", "resolve sign in error");
		if (mConnectionResult.hasResolution()) {
			try {

				mConnectionResult.startResolutionForResult(androidLan,
						AndroidLauncher.GOOGLE_RC_SIGN_IN);
			} catch (SendIntentException e) {
				googleDidNotShare();

			}
		}
	}

	public class GoogleConnectClass extends AsyncTask<Integer, Integer, Integer> {

		ProgressDialog spinner;

		@Override
		protected void onPreExecute() {
			androidLan.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					spinner = new ProgressDialog(androidLan);
					spinner.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							googleDidNotShare();
						}
					});
					spinner.show();

				}
			});

		}

		@Override
		protected Integer doInBackground(Integer... params) {

			googleClient.connect();
			
			while (googleClient.isConnecting()) {
				
			}
			
			return params[0];
		}

		@Override
		protected void onPostExecute(final Integer result) {

			androidLan.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					spinner.dismiss();
					spinner = null;
					if (result == AndroidLauncher.GOOGLE_RC_SIGN_IN) {
						resolveSignInError();
					}else if(result == AndroidLauncher.GOOGLE_RC_SHARE){
						googleCreateShare();
					}
				}
			});

		}

	}

	@Override
	public void androidTwitterClicked() {
		smcDisable();
		if (isConnectingToInternet()) {
			androidLan.appControl.pause();
			loadTwitterLogin();
		} else {
			postErrorMessage();
			smcEnable();
		}
	}

	private void loadTwitterLogin() {

		new TwitterRequestURLClass().execute("null");
	}

	// Post a tweet on twitter
	private void postToTwitter(final String oauth) {
		verifier = oauth;
		androidLan.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				SocialShareDialog test = new SocialShareDialog(androidLan);
				test.setOnCompleteSharing(new OnCompleteSharing() {

					@Override
					public void complete(int status, String post) {

						if (status == SocialShareDialog.CANCELED) {
							postErrorMessage();
							logoutTwitter();
							smcEnable();
							androidLan.appControl.resume();
						} else if (status == SocialShareDialog.SHARED) {
							new TwitterUpdateStatus().execute(post);

						}

					}
				});

				test.show();

			}
		});

	}

	public void logoutTwitter() {
		CookieSyncManager.createInstance(androidLan);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();

		if (twitter != null) {
			twitter.setOAuthAccessToken(null);
		}
	}

	// Request URL async task class
	private class TwitterRequestURLClass extends
			AsyncTask<String, String, String> {

		String url;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {

			if (!androidLan.isFinishing()) {

				androidLan.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						pDialog = new ProgressDialog(androidLan);
						pDialog.setIndeterminate(false);
						pDialog.setOnCancelListener(new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								TwitterRequestURLClass.this.cancel(true);
								logoutTwitter();
								androidLan.appControl.resume();
								smcEnable();
								postErrorMessage();

							}
						});
						pDialog.show();
					}
				});
			}

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
					pDialog.dismiss();
					SocialWebLoginDialog twitterLoginDialog = new SocialWebLoginDialog(
							androidLan, url2);
					twitterLoginDialog
							.setSocialComplete(new SocialCompleteListener() {

								@Override
								public void loginComplete(int status,
										String oauth) {

									if (status == SocialWebLoginDialog.VERIFIED) {
										postToTwitter(oauth);
									} else if (status == SocialWebLoginDialog.NOT_VERIFIED) {
										postErrorMessage();
										androidLan.appControl.resume();
										smcEnable();
									}
								}

							});
					twitterLoginDialog.show();

				}
			});

		}

	}

	private class TwitterUpdateStatus extends AsyncTask<String, String, String> {

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
			try {
				accessToken = twitter.getOAuthAccessToken(requestToken,
						verifier);
				Log.d("DAVID", params[0]);
				twitter.updateStatus(params[0]);
			} catch (TwitterException e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			logoutTwitter();
			androidLan.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					pDialog.dismiss();
					postSuccessUpdate();
					androidLan.appControl.resume();
					smcEnable();

				}
			});
		}

	}

	@Override
	public void androidVKClicked() {
		if (isConnectingToInternet()) {
			smcDisable();
			VKSdk.initialize(new VKListener(), "4486348");
			VKSdk.authorize(sMyScope);
		} else {
			postErrorMessage();
		}

	}

	private class VKListener extends VKSdkListener {

		@Override
		public void onAcceptUserToken(VKAccessToken token) {

			super.onAcceptUserToken(token);

			Log.d("DAVID VILLARREAL", "AcceptUserToken ------ ");
		}

		@Override
		public void onAccessDenied(VKError arg0) {
			Log.d("DAVID VILLARREAL", "Access Denied");
			postErrorMessage();
			smcEnable();
			VKSdk.logout();
		}

		@Override
		public void onCaptchaError(VKError arg0) {
			Log.d("DAVID VILLARREAL", "Captcha Error");

		}

		@Override
		public void onReceiveNewToken(VKAccessToken newToken) {
			super.onReceiveNewToken(newToken);
			Log.d("DAVID VILLARREAL", "On Receive New Token");
			shareToVK();

		}

		@Override
		public void onRenewAccessToken(VKAccessToken token) {
			super.onRenewAccessToken(token);
			Log.d("DAVID VILLARREAL", "On renew access token");
		}

		@Override
		public void onTokenExpired(VKAccessToken arg0) {
			Log.d("DAVID VILLARREAL", "Token expired");

		}

	}

	private void shareToVK() {
		androidLan.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				MyVKShareDialog vkShareD = new MyVKShareDialog(androidLan);
				vkShareD.setText("Testing VK");
				vkShareD.setAttachmentLink("Rising Fall", "vk.com");
				vkShareD.setShareDialogListener(new MyVKShareDialogListener() {

					@Override
					public void onVkShareComplete(int postId) {
						postSuccessUpdate();
						smcEnable();
						VKSdk.logout();

					}

					@Override
					public void onVkShareCancel() {
						postErrorMessage();
						smcEnable();
						VKSdk.logout();

					}
				});

				vkShareD.show();
			}
		});

	}

	@Override
	public void androidWeiboClicked() {

	}

	private void postErrorMessage() {
		smc.delegate.sharedCalledBack(false);
	}

	private void postSuccessUpdate() {
		smc.delegate.sharedCalledBack(true);
	}

	public void smcEnable() {
		smc.delegate.enableOther();
	}

	public void smcDisable() {
		smc.delegate.disableOther();
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
