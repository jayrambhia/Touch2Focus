package com.fenchtose.touch2focus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.util.Log;
import android.view.SurfaceHolder;

public class CameraPreview
		implements
			SurfaceHolder.Callback {
	
	private Camera mCamera = null;
	public Camera.Parameters params;
	private SurfaceHolder sHolder;

	public List<Camera.Size> supportedSizes;

	public int isCamOpen = 0;
	public boolean isSizeSupported = false;
	private int previewWidth, previewHeight;

	private final static String TAG = "CameraPreview";

	public CameraPreview(int width, int height) {
		Log.i("campreview", "Width = " + String.valueOf(width));
		Log.i("campreview", "Height = " + String.valueOf(height));
		previewWidth = width;
		previewHeight = height;
	}

	private int openCamera() {
		if (isCamOpen == 1) {
			releaseCamera();
		}

		mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

		if (mCamera == null) {
			return -1;
		}

		params = mCamera.getParameters();
		params.setPreviewSize(previewWidth, previewHeight);

		try {
			mCamera.setParameters(params);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		mCamera.startPreview();
		try {
			mCamera.setPreviewDisplay(sHolder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
			return -1;
		}
		isCamOpen = 1;
		return isCamOpen;
	}
	public int isCamOpen() {
		return isCamOpen;
	}

	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
		isCamOpen = 0;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		sHolder = holder;
		
		isCamOpen = openCamera();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		releaseCamera();

	}

	/**
	 * Called from PreviewSurfaceView to set touch focus.
	 * 
	 * @param - Rect - new area for auto focus
	 */
	public void doTouchFocus(final Rect tfocusRect) {
		Log.i(TAG, "TouchFocus");
		try {
			final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
			Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
			focusList.add(focusArea);
		  
			Camera.Parameters para = mCamera.getParameters();
			para.setFocusAreas(focusList);
			para.setMeteringAreas(focusList);
			mCamera.setParameters(para);
		  
			mCamera.autoFocus(myAutoFocusCallback);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "Unable to autofocus");
		}

	}
	
	/**
	 * AutoFocus callback
	 */
	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

		  @Override
		  public void onAutoFocus(boolean arg0, Camera arg1) {
		   if (arg0){
		    mCamera.cancelAutoFocus();      
		   }
	    }
	};
}