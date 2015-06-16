package com.purezhihudaily.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.purezhihudaily.R;
import com.purezhihudaily.framework.ApplicationHelper;

/**
 * Activity基类
 * 
 */
public class GenericActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationHelper.addActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ApplicationHelper.removeActivity(this);
	}

	/**
	 * 控制返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
