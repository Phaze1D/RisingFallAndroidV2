package com.Phaze1D.RisingFallAndroidV2.android;




import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MySocialWebLoginDialog extends Dialog {

	public static final int NOT_POSTED = 0;
	public static final int POSTED = 1;
	public static final int NOT_VERIFIED = 2;
	public static final int VERIFIED = 3;

	// width below which there are no extra margins
	private static final int NO_PADDING_SCREEN_WIDTH = 480;
	// width beyond which we're always using the MIN_SCALE_FACTOR
	private static final int MAX_PADDING_SCREEN_WIDTH = 800;
	// height below which there are no extra margins
	private static final int NO_PADDING_SCREEN_HEIGHT = 800;
	// height beyond which we're always using the MIN_SCALE_FACTOR
	private static final int MAX_PADDING_SCREEN_HEIGHT = 1280;

	// the minimum scaling factor for the web dialog (50% of screen size)
	private static final double MIN_SCALE_FACTOR = 0.5;
	// translucent border around the webview
	private static final int BACKGROUND_GRAY = 0xCC000000;

	public static final int DEFAULT_THEME = android.R.style.Theme_Translucent_NoTitleBar;

	private String url;
	private WebView webView;
	private SocialCompleteListener onComplete;
	private ProgressDialog spinner;
	private ImageView crossImageView;
	private FrameLayout contentFrameLayout;
	private boolean isDetached = false;

	/** On Complete listener */
	public interface SocialCompleteListener {

		public void loginComplete(int status, String oauth);

	}

	public MySocialWebLoginDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener, String url) {
		super(context, cancelable, cancelListener);
		this.url = url;

	}

	public MySocialWebLoginDialog(Context context, int theme, String url) {
		super(context, theme);
		this.url = url;

	}

	public MySocialWebLoginDialog(Context context, String url) {
		super(context,DEFAULT_THEME);
		this.url = url;

	}

	public void setSocialComplete(SocialCompleteListener onComplete) {
		this.onComplete = onComplete;
	}

	@Override
	public void dismiss() {
		if (webView != null) {
			webView.stopLoading();
		}
		if (!isDetached) {
			if (spinner.isShowing()) {
				spinner.dismiss();
			}
			super.dismiss();
		}

	}

	@Override
	public void onDetachedFromWindow() {
		isDetached = true;
		super.onDetachedFromWindow();
	}

	@Override
	public void onAttachedToWindow() {
		isDetached = false;
		super.onAttachedToWindow();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				MySocialWebLoginDialog.this.onComplete.loginComplete(NOT_VERIFIED, null);
				MySocialWebLoginDialog.this.dismiss();
			}
		});

		spinner = new ProgressDialog(getContext());
		spinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		spinner.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				MySocialWebLoginDialog.this.onComplete.loginComplete(NOT_VERIFIED, null);
				MySocialWebLoginDialog.this.dismiss();
			}
		});

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		contentFrameLayout = new FrameLayout(getContext());

		calculateSize();
		getWindow().setGravity(Gravity.CENTER);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		createCrossImage();

		int crossWidth = crossImageView.getDrawable().getIntrinsicWidth();

		setUpWebView(crossWidth / 2 + 1);

		contentFrameLayout.addView(crossImageView, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		setContentView(contentFrameLayout);

	}

	private void createCrossImage() {
		crossImageView = new ImageView(getContext());
		// Dismiss the dialog when user click on the 'x'
		crossImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MySocialWebLoginDialog.this.onComplete.loginComplete(NOT_VERIFIED, null);
				MySocialWebLoginDialog.this.dismiss();
			}
		});
		Drawable crossDrawable = getContext().getResources().getDrawable(
				R.drawable.com_facebook_close);
		crossImageView.setImageDrawable(crossDrawable);

		crossImageView.setVisibility(View.INVISIBLE);
	}

    private void setUpWebView(int margin) {
        LinearLayout webViewContainer = new LinearLayout(getContext());
        webView = new WebView(getContext());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new SocialWebClient());
        webView.loadUrl(url);
        webView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setSaveFormData(false);

        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(webView);
        webViewContainer.setBackgroundColor(BACKGROUND_GRAY);
        contentFrameLayout.addView(webViewContainer);
    }

	private void calculateSize() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);

		int width = metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels
				: metrics.heightPixels;
		int height = metrics.widthPixels < metrics.heightPixels ? metrics.heightPixels
				: metrics.widthPixels;

		int dialogWidth = Math.min(
				getScaledSize(width, metrics.density, NO_PADDING_SCREEN_WIDTH,
						MAX_PADDING_SCREEN_WIDTH), metrics.widthPixels);
		int dialogHeight = Math.min(
				getScaledSize(height, metrics.density,
						NO_PADDING_SCREEN_HEIGHT, MAX_PADDING_SCREEN_HEIGHT),
				metrics.heightPixels);

		getWindow().setLayout(dialogWidth, dialogHeight);
	}

	private int getScaledSize(int screenSize, float density, int noPaddingSize,
			int maxPaddingSize) {
		int scaledSize = (int) ((float) screenSize / density);
		double scaleFactor;
		if (scaledSize <= noPaddingSize) {
			scaleFactor = 1.0;
		} else if (scaledSize >= maxPaddingSize) {
			scaleFactor = MIN_SCALE_FACTOR;
		} else {
			scaleFactor = MIN_SCALE_FACTOR
					+ ((double) (maxPaddingSize - scaledSize))
					/ ((double) (maxPaddingSize - noPaddingSize))
					* (1.0 - MIN_SCALE_FACTOR);
		}
		return (int) (screenSize * scaleFactor);
	}

	private class SocialWebClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			if(url.contains("oauth://RisingFall")){
				Uri uri = Uri.parse( url );				
	            String oauthVerifier = uri.getQueryParameter( "oauth_verifier" );
	            if(oauthVerifier != null){
	            	 MySocialWebLoginDialog.this.onComplete.loginComplete(VERIFIED, oauthVerifier);
	            }else{
	            	 MySocialWebLoginDialog.this.onComplete.loginComplete(NOT_VERIFIED, null);
	            }
	            
	            MySocialWebLoginDialog.this.dismiss();
	            return true;
			}
			
			return false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.d("DAVID", "PAGE STARTED");
			if (!isDetached) {
                spinner.show();
            }
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d("DAVID", "PAGE FINISH");
			 if (!isDetached) {	                
				 spinner.dismiss();
	         }
	            contentFrameLayout.setBackgroundColor(Color.TRANSPARENT);
	            webView.setVisibility(View.VISIBLE);
	            crossImageView.setVisibility(View.VISIBLE);
		}

	}

}
