package com.fenchtose.touch2focus;


import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;

public class TouchActivity extends Activity {
	
	private PreviewSurfaceView camView;
	private CameraPreview cameraPreview;
	private DrawingView drawingView;
	
	private int previewWidth = 1280;
	private int previewHeight = 720;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.activity_touch);
		
		camView = (PreviewSurfaceView) findViewById(R.id.preview_surface);
		SurfaceHolder camHolder = camView.getHolder();

		cameraPreview = new CameraPreview(previewWidth, previewHeight);
		camHolder.addCallback(cameraPreview);
		camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		camView.setListener(cameraPreview);
		//cameraPreview.changeExposureComp(-currentAlphaAngle);
		drawingView = (DrawingView) findViewById(R.id.drawing_surface);
		camView.setDrawingView(drawingView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.touch, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
