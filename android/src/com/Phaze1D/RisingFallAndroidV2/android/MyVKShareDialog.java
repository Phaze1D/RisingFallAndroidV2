package com.Phaze1D.RisingFallAndroidV2.android;

import java.util.ArrayList;

import com.vk.sdk.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.httpClient.VKHttpClient;
import com.vk.sdk.api.httpClient.VKImageOperation;
import com.vk.sdk.api.model.VKApiLink;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.api.photo.VKUploadWallPhotoRequest;
import com.vk.sdk.util.VKStringJoiner;
import com.vk.sdk.util.VKUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyVKShareDialog extends Dialog {

	static private final String SHARE_TEXT_KEY = "ShareText";
	static private final String SHARE_LINK_KEY = "ShareLink";
	static private final String SHARE_IMAGES_KEY = "ShareImages";
	static private final String SHARE_UPLOADED_IMAGES_KEY = "ShareUploadedImages";

	static private final int SHARE_PHOTO_HEIGHT = 100;
	static private final int SHARE_PHOTO_CORNER_RADIUS = 3;
	static private final int SHARE_PHOTO_MARGIN_LEFT = 10;

	private EditText mShareTextField;
	private Button mSendButton;
	private ProgressBar mSendProgress;
	private LinearLayout mPhotoLayout;
	private HorizontalScrollView mPhotoScroll;

	private UploadingLink mAttachmentLink;
	private VKUploadImage[] mAttachmentImages;
	private VKPhotoArray mExistingPhotos;
	private CharSequence mAttachmentText;

	private MyVKShareDialogListener mListener;

	public MyVKShareDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public MyVKShareDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public MyVKShareDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sets images that will be uploaded with post
	 * 
	 * @param images
	 *            array of VKUploadImage objects with image data and upload
	 *            parameters
	 * @return Returns this dialog for chaining
	 */
	public MyVKShareDialog setAttachmentImages(VKUploadImage[] images) {
		mAttachmentImages = images;
		return this;
	}

	/**
	 * Sets this dialog post text. User can change that text
	 * 
	 * @param textToPost
	 *            Text for post
	 * @return Returns this dialog for chaining
	 */
	public MyVKShareDialog setText(CharSequence textToPost) {
		mAttachmentText = textToPost;
		return this;
	}

	/**
	 * Sets dialog link with link name
	 * 
	 * @param linkTitle
	 *            A small description for your link
	 * @param linkUrl
	 *            Url that link follows
	 * @return Returns this dialog for chaining
	 */
	public MyVKShareDialog setAttachmentLink(String linkTitle, String linkUrl) {
		mAttachmentLink = new UploadingLink(linkTitle, linkUrl);
		return this;
	}

	/**
	 * Sets array of already uploaded photos from VK, that will be attached to
	 * post
	 * 
	 * @param photos
	 *            Prepared array of {@link VKApiPhoto} objects
	 * @return Returns this dialog for chaining
	 */
	public MyVKShareDialog setUploadedPhotos(VKPhotoArray photos) {
		mExistingPhotos = photos;
		return this;
	}

	/**
	 * Sets this dialog listener
	 * 
	 * @param listener
	 *            {@link VKShareDialogListener} object
	 * @return Returns this dialog for chaining
	 */
	public MyVKShareDialog setShareDialogListener(
			MyVKShareDialogListener listener) {
		mListener = listener;
		return this;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		Context context = getContext();
		View mInternalView = LayoutInflater.from(context).inflate(
				R.layout.vk_share_dialog, null);

		assert mInternalView != null;

		mSendButton = (Button) mInternalView.findViewById(R.id.sendButton);
		mSendProgress = (ProgressBar) mInternalView
				.findViewById(R.id.sendProgress);
		mPhotoLayout = (LinearLayout) mInternalView
				.findViewById(R.id.imagesContainer);
		mShareTextField = (EditText) mInternalView.findViewById(R.id.shareText);
		mPhotoScroll = (HorizontalScrollView) mInternalView
				.findViewById(R.id.imagesScrollView);

		LinearLayout mAttachmentLinkLayout = (LinearLayout) mInternalView
				.findViewById(R.id.attachmentLinkLayout);

		mSendButton.setOnClickListener(sendButtonPress);

		mShareTextField.setText(mAttachmentText);


		// Attachment photos
		mPhotoLayout.removeAllViews();
		if (mAttachmentImages != null) {
			for (VKUploadImage mAttachmentImage : mAttachmentImages) {
				addBitmapToPreview(mAttachmentImage.mImageData);
			}
			mPhotoLayout.setVisibility(View.VISIBLE);
		}

		if (mExistingPhotos != null) {
			processExistingPhotos();
		}
		if (mExistingPhotos == null && mAttachmentImages == null) {
			mPhotoLayout.setVisibility(View.GONE);
		}

		// Attachment link
		if (mAttachmentLink != null) {
			TextView linkTitle = (TextView) mAttachmentLinkLayout
					.findViewById(R.id.linkTitle), linkHost = (TextView) mAttachmentLinkLayout
					.findViewById(R.id.linkHost);

			linkTitle.setText(mAttachmentLink.linkTitle);
			linkHost.setText(VKUtil.getHost(mAttachmentLink.linkUrl));
			mAttachmentLinkLayout.setVisibility(View.VISIBLE);
		} else {
			mAttachmentLinkLayout.setVisibility(View.GONE);
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(mInternalView);
		setCancelable(true);
		setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				if (mListener != null) {
					mListener.onVkShareCancel();
				}
				MyVKShareDialog.this.dismiss();
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(getWindow().getAttributes());
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(lp);
	}

	// @Override
	// public void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// outState.putString(SHARE_TEXT_KEY, mShareTextField.getText().toString());
	// if (mAttachmentLink != null)
	// outState.putParcelable(SHARE_LINK_KEY, mAttachmentLink);
	// if (mAttachmentImages != null)
	// outState.putParcelableArray(SHARE_IMAGES_KEY, mAttachmentImages);
	// if (mExistingPhotos != null)
	// outState.putParcelable(SHARE_UPLOADED_IMAGES_KEY, mExistingPhotos);
	// }

	private void setIsLoading(boolean loading) {
		if (loading) {
			mSendButton.setVisibility(View.GONE);
			mSendProgress.setVisibility(View.VISIBLE);
			mShareTextField.setEnabled(false);
			mPhotoLayout.setEnabled(false);
		} else {
			mSendButton.setVisibility(View.VISIBLE);
			mSendProgress.setVisibility(View.GONE);
			mShareTextField.setEnabled(true);
			mPhotoLayout.setEnabled(true);
		}
	}

	private void processExistingPhotos() {
		ArrayList<String> photosToLoad = new ArrayList<String>(
				mExistingPhotos.size());
		for (VKApiPhoto photo : mExistingPhotos) {
			photosToLoad.add("" + photo.owner_id + '_' + photo.id);
		}
		VKRequest photosById = new VKRequest("photos.getById",
				VKParameters.from(VKApiConst.PHOTO_SIZES, 1, VKApiConst.PHOTOS,
						VKStringJoiner.join(photosToLoad, ",")),
				VKRequest.HttpMethod.GET, VKPhotoArray.class);
		photosById.executeWithListener(new VKRequest.VKRequestListener() {
			@Override
			public void onComplete(VKResponse response) {
				VKPhotoArray photos = (VKPhotoArray) response.parsedModel;
				for (VKApiPhoto photo : photos) {
					if (photo.src.getByType('q') != null) {
						loadAndAddPhoto(photo.src.getByType('q'));
					} else if (photo.src.getByType('p') != null) {
						loadAndAddPhoto(photo.src.getByType('p'));
					} else if (photo.src.getByType('m') != null) {
						loadAndAddPhoto(photo.src.getByType('m'));
					}
					// else ignore that strange photo
				}
			}

			@Override
			public void onError(VKError error) {
				if (VKSdk.DEBUG) {
					Log.w(VKSdk.SDK_TAG, "Cannot load photos for share: "
							+ error.toString());
				}
			}
		});
	}

	private void loadAndAddPhoto(String photoUrl) {
		VKImageOperation op = new VKImageOperation(photoUrl);
		op.setImageOperationListener(new VKImageOperation.VKImageOperationListener() {
			@Override
			public void onComplete(VKImageOperation operation, Bitmap image) {
				addBitmapToPreview(image);
			}
		});
		VKHttpClient.enqueueOperation(op);
	}

	private void addBitmapToPreview(Bitmap sourceBitmap) {
		if (getContext() == null)
			return;
		Bitmap b = VKUIHelper.getRoundedCornerBitmap(sourceBitmap,
				SHARE_PHOTO_HEIGHT, SHARE_PHOTO_CORNER_RADIUS);
		ImageView iv = new ImageView(getContext());
		iv.setImageBitmap(b);
		iv.setAdjustViewBounds(true);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(
				mPhotoLayout.getChildCount() > 0 ? SHARE_PHOTO_MARGIN_LEFT : 0,
				0, 0, 0);

		mPhotoLayout.addView(iv, params);
		mPhotoLayout.invalidate();
		mPhotoScroll.invalidate();
	}

	private void makePostWithAttachments(VKAttachments attachments) {

		if (attachments == null) {
			attachments = new VKAttachments();
		}
		if (mExistingPhotos != null) {
			attachments.addAll(mExistingPhotos);
		}
		if (mAttachmentLink != null) {
			attachments.add(new VKApiLink(mAttachmentLink.linkUrl));
		}
		String message = mShareTextField.getText().toString();

		final Long userId = Long.parseLong(VKSdk.getAccessToken().userId);
		VKRequest wallPost = VKApi.wall().post(
				VKParameters.from(VKApiConst.OWNER_ID, userId,
						VKApiConst.MESSAGE, message, VKApiConst.ATTACHMENTS,
						attachments.toAttachmentsString()));
		wallPost.executeWithListener(new VKRequest.VKRequestListener() {
			@Override
			public void onError(VKError error) {
				setIsLoading(false);
			}

			@Override
			public void onComplete(VKResponse response) {
				setIsLoading(false);
				VKWallPostResult res = (VKWallPostResult) response.parsedModel;
				if (mListener != null) {
					mListener.onVkShareComplete(res.post_id);
				}
				dismiss();
			}
		});
	}

	View.OnClickListener sendButtonPress = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			setIsLoading(true);
			if (mAttachmentImages != null) {
				final Long userId = Long
						.parseLong(VKSdk.getAccessToken().userId);
				VKUploadWallPhotoRequest photoRequest = new VKUploadWallPhotoRequest(
						mAttachmentImages, userId, 0);
				photoRequest
						.executeWithListener(new VKRequest.VKRequestListener() {
							@Override
							public void onComplete(VKResponse response) {
								VKPhotoArray photos = (VKPhotoArray) response.parsedModel;
								VKAttachments attachments = new VKAttachments(
										photos);
								makePostWithAttachments(attachments);
							}

							@Override
							public void onError(VKError error) {
								setIsLoading(false);
							}
						});
			} else {
				makePostWithAttachments(null);
			}
		}
	};

	@Override
	public void cancel() {
		super.cancel();
		if (mListener != null) {
			mListener.onVkShareCancel();
		}
	}

	static private class UploadingLink implements Parcelable {
		public String linkTitle, linkUrl;

		public UploadingLink(String title, String url) {
			linkTitle = title;
			linkUrl = url;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.linkTitle);
			dest.writeString(this.linkUrl);
		}

		private UploadingLink(Parcel in) {
			this.linkTitle = in.readString();
			this.linkUrl = in.readString();
		}

		public static final Parcelable.Creator<UploadingLink> CREATOR = new Parcelable.Creator<UploadingLink>() {
			public UploadingLink createFromParcel(Parcel source) {
				return new UploadingLink(source);
			}

			public UploadingLink[] newArray(int size) {
				return new UploadingLink[size];
			}
		};
	}

	public static interface MyVKShareDialogListener {
		public void onVkShareComplete(int postId);

		public void onVkShareCancel();
	}

}
