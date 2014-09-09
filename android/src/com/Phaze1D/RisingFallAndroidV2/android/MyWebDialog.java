package com.Phaze1D.RisingFallAndroidV2.android;



import android.annotation.SuppressLint;
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

public class MyWebDialog extends Dialog {
	
	public static final int CROSS_CLICKED = 1;
	public static final int DIALOG_CANCELED = 2;
	public static final int SPINNER_CANCELED = 3;
	
	private String url;
    private WebView webView;
    private ProgressDialog spinner;
    private ImageView crossImageView;
    private OnCompleteListener onCompleteListener;
    private FrameLayout contentFrameLayout;
    private boolean listenerCalled = false;
    private boolean isDetached = false;
    
    private static final int NO_PADDING_WIDTH = 480;

    private static final int MAX_PADDING_WIDTH = 800;

    private static final int NO_PADDING_HEIGHT = 800;

    private static final int MAX_PADDING_HEIGHT = 1280;
    
    private static final int BACKGROUND_COLOR = 0xCC000000;

	public MyWebDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		
	}

	public MyWebDialog(Context context, String url) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
	
		this.url = url;
	}
	
	
	
	public OnCompleteListener getOnCompleteListener() {
		return onCompleteListener;
	}

	public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
		this.onCompleteListener = onCompleteListener;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onCompleteListener.onComplete(DIALOG_CANCELED);
            }
        });

        spinner = new ProgressDialog(getContext());
        spinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        spinner.setMessage(getContext().getString(R.string.com_facebook_loading));
        spinner.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            	onCompleteListener.onComplete(SPINNER_CANCELED);
                MyWebDialog.this.dismiss();
            }
        });

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        contentFrameLayout = new FrameLayout(getContext());


        calculateSize();
        getWindow().setGravity(Gravity.CENTER);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        createCrossImage();

        int crossWidth = crossImageView.getDrawable().getIntrinsicWidth();

        setUpWebView(crossWidth / 2 + 1);

        contentFrameLayout.addView(crossImageView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(contentFrameLayout);
    }
	
	@SuppressLint("SetJavaScriptEnabled")
	private void setUpWebView(int margin) {
        LinearLayout webViewContainer = new LinearLayout(getContext());
        webView = new WebView(getContext());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);;
        webView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setSaveFormData(false);

        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(webView);
        webViewContainer.setBackgroundColor(BACKGROUND_COLOR);
        contentFrameLayout.addView(webViewContainer);
    }
	
	private void calculateSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // always use the portrait dimensions to do the scaling calculations so we always get a portrait shaped
        // web dialog
        int width = metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
        int height = metrics.widthPixels < metrics.heightPixels ? metrics.heightPixels : metrics.widthPixels;

        int dialogWidth = Math.min(
                getScaledSize(width, metrics.density, NO_PADDING_WIDTH, MAX_PADDING_WIDTH),
                metrics.widthPixels);
        int dialogHeight = Math.min(
                getScaledSize(height, metrics.density, NO_PADDING_HEIGHT, MAX_PADDING_HEIGHT),
                metrics.heightPixels);

        getWindow().setLayout(dialogWidth, dialogHeight);
    }
	
	private int getScaledSize(int screenSize, float density, int noPaddingSize, int maxPaddingSize) {
        int scaledSize = (int) ((float) screenSize / density);
        double scaleFactor;
        if (scaledSize <= noPaddingSize) {
            scaleFactor = 1.0;
        } else if (scaledSize >= maxPaddingSize) {
            scaleFactor = .5;
        } else {
            // between the noPadding and maxPadding widths, we take a linear reduction to go from 100%
            // of screen size down to MIN_SCALE_FACTOR
            scaleFactor = .5 +
                    ((double) (maxPaddingSize - scaledSize))
                            / ((double) (maxPaddingSize - noPaddingSize))
                            * (1.0 - .5);
        }
        return (int) (screenSize * scaleFactor);
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
	
	private void createCrossImage() {
        crossImageView = new ImageView(getContext());
        // Dismiss the dialog when user click on the 'x'
        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCompleteListener.onComplete(CROSS_CLICKED);
                MyWebDialog.this.dismiss();
            }
        });
        Drawable crossDrawable = getContext().getResources().getDrawable(R.drawable.com_facebook_close);
        crossImageView.setImageDrawable(crossDrawable);
        /* 'x' should not be visible while webview is loading
         * make it visible only after webview has fully loaded
         */
        crossImageView.setVisibility(View.INVISIBLE);
    }

	
	
	public interface OnCompleteListener{
		public void onComplete(int status);
	}
	
	
	private class MyWebViewClient extends WebViewClient{
		
		
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			if(url.contains("oauth://RisingFall")){
				Uri uri = Uri.parse( url );
	            // you get the verifier here
	            String oauthVerifier = uri.getQueryParameter( "oauth_verifier" );
	            
	            MyWebDialog.this.dismiss();
	            return true;
			}
			
			
			return false;
		}

		@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {            
            super.onPageStarted(view, url, favicon);
            if (!isDetached) {
                spinner.show();
            }
        }
		
		 @Override
	        public void onPageFinished(WebView view, String url) {
	            super.onPageFinished(view, url);
	            if (!isDetached) {
	                spinner.dismiss();
	            }
	            contentFrameLayout.setBackgroundColor(Color.TRANSPARENT);
	            webView.setVisibility(View.VISIBLE);
	            crossImageView.setVisibility(View.VISIBLE);
	        }
		
	}
}
