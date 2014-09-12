package com.Phaze1D.RisingFallAndroidV2.android;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SocialShareDialog extends Dialog {

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

	public static final int CANCELED = 12;
	public static final int SHARED = 13;

	private OnCompleteSharing onCompleteSharing;
	private Button tweetButton;
	private EditText textArea;
	private LinearLayout frameLayout;
	private TextView titleTV;

	public SocialShareDialog(Context context, int theme) {
		super(context, theme);

	}

	public SocialShareDialog(Context context) {
		super(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
	}

	@Override
	public void dismiss() {

		super.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				onCompleteSharing.complete(CANCELED, null);

			}
		});
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		frameLayout = new LinearLayout(getContext());
		frameLayout.setOrientation(LinearLayout.VERTICAL);
		getWindow().setGravity(Gravity.CENTER);

		calculateSize();

		createTitle();
		createTextArea();
		createTweetButton();

		setContentView(frameLayout);

	}

	public void setOnCompleteSharing(OnCompleteSharing onCompleteSharing) {
		this.onCompleteSharing = onCompleteSharing;
	}

	private void createTitle() {

		LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layout.setMargins(10, 10, 10, 5);

		titleTV = new TextView(getContext());
		titleTV.setTextAppearance(getContext(),
				android.R.style.TextAppearance_Large);
		titleTV.setText("Twitter");
		titleTV.setLayoutParams(layout);
		titleTV.setTextColor(getContext().getResources().getColor(
				R.color.twitter_color));
		frameLayout.addView(titleTV);
	}

	private void createTextArea() {
		LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		textArea = new EditText(getContext());
		textArea.setLayoutParams(layout);
		textArea.setLines(4);
		textArea.setMaxLines(4);
		textArea.setText(getContext().getResources().getString(R.string.share_text));
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(160);
		textArea.setFilters(FilterArray);
		frameLayout.addView(textArea);

	}

	private void createTweetButton() {
		tweetButton = new Button(getContext());
		LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layout.setMargins(5, 5, 5, 5);
		layout.gravity = Gravity.RIGHT;
		tweetButton.setLayoutParams(layout);
		tweetButton.setText("Tweet");
		tweetButton.setTextSize(titleTV.getTextSize() - 5);
		tweetButton.setBackgroundResource(R.drawable.round_tweet_button);
		tweetButton.setPadding(10, 10, 10, 10);
		
		tweetButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				onCompleteSharing.complete(SHARED, textArea.getText().toString());
				SocialShareDialog.this.dismiss();
			} 
			
		});
		tweetButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					tweetButton.setAlpha(.5f);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					tweetButton.setAlpha(1f);
					v.performClick();
				}
				
				return true;
			}
		});
		
		frameLayout.addView(tweetButton);
		

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

		int dialogHeight = Math.min(
				getScaledSize(width, metrics.density, NO_PADDING_SCREEN_WIDTH,
						MAX_PADDING_SCREEN_WIDTH), metrics.widthPixels) / 2;
		int dialogWidth = (int) (Math.min(
				getScaledSize(height, metrics.density,
						NO_PADDING_SCREEN_HEIGHT, MAX_PADDING_SCREEN_HEIGHT),
				metrics.heightPixels) / 1.5);

		getWindow().setLayout(dialogWidth, dialogHeight);
		frameLayout.setMinimumWidth(dialogWidth);
		frameLayout.setMinimumHeight(dialogHeight);

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

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}

	public interface OnCompleteSharing {
		public void complete(int status, String post);
	}
}
