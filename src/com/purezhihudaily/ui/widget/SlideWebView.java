package com.purezhihudaily.ui.widget;

import java.util.ArrayList;

import com.purezhihudaily.R;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class SlideWebView extends WebView {

	private AnimatorSet backAnimatorSet; // 这是显示头尾元素使用的动画
	private AnimatorSet hideAnimatorSet; // 这是隐藏头尾元素使用的动画

	private float lastY = 0f;
	private float currentY = 0f;
	// 下面两个表示滑动的方向，大于0表示向下滑动，小于0表示向上滑动，等于0表示未滑动
	private int lastDirection = 0;
	private int currentDirection = 0;

	private Toolbar toolbar;

	private View headerView;
	private Matrix matrix = new Matrix();
	private Rect clipBounds = new Rect();

	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
	}

	public SlideWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setHeaderView(View v) {
		if (headerView == v)
			return;
		if (headerView != null) {
			removeView(headerView);
		}
		if (v != null) {
			addView(v);
			setInitialScale(100);
		}
		headerView = v;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();

		if (headerView != null) {
			final int sy = getScrollY();
			final int sx = getScrollX();
			clipBounds.top = sy;
			clipBounds.left = sx;
			clipBounds.right = clipBounds.left + getWidth();
			clipBounds.bottom = clipBounds.top + getHeight();
			canvas.clipRect(clipBounds);
			matrix.set(canvas.getMatrix());
			int titleBarOffs = headerView.getHeight() - sy;
			if (titleBarOffs < 0)
				titleBarOffs = 0;
			matrix.postTranslate(0, titleBarOffs);
			canvas.setMatrix(matrix);
		}

		super.onDraw(canvas);
		canvas.restore();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		if (child == headerView) {
			clipBounds.top = 0;
			clipBounds.left = 0;
			clipBounds.right = clipBounds.left + child.getWidth();
			clipBounds.bottom = child.getHeight();
			canvas.save();
			child.setDrawingCacheEnabled(true);
			matrix.set(canvas.getMatrix());
			matrix.postTranslate(getScrollX(), -getScrollY());
			canvas.setMatrix(matrix);
			canvas.clipRect(clipBounds);
			child.draw(canvas);
			canvas.restore();

			return false;
		}

		return super.drawChild(canvas, child, drawingTime);
	}

	/**
	 * 处理我们拖动ListView item的逻辑
	 */
	@SuppressLint({ "ClickableViewAccessibility", "Recycle" })
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = ev.getY();
			currentY = ev.getY();
			currentDirection = 0;
			lastDirection = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			float tmpCurrentY = ev.getY();
			if (Math.abs(tmpCurrentY - lastY) > (int) getResources().getDimension(
					R.dimen.abc_action_bar_default_height_material)) {
				currentY = tmpCurrentY;
				currentDirection = (int) (currentY - lastY);
				if (lastDirection != currentDirection) {
					// 如果与上次方向不同，则执行显/隐动画
					if (currentDirection < 0) {
						animateHide();
					}
					else {
						animateBack();
					}
				}
				lastY = currentY;
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		}

		return super.onTouchEvent(ev);
	}

	public void animateBack() {
		// 先清除其他动画
		if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
			hideAnimatorSet.cancel();
		}
		if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
			
		}
		else {
			backAnimatorSet = new AnimatorSet();
			// 下面两句是将头尾元素放回初始位置。
			ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(toolbar, "translationY",
					toolbar.getTranslationY(), 0f);
			ArrayList<Animator> animators = new ArrayList<Animator>();
			animators.add(headerAnimator);
			backAnimatorSet.setDuration(300);
			backAnimatorSet.playTogether(animators);
			backAnimatorSet.start();
		}
	}

	public void animateHide() {
		// 先清除其他动画
		if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
			backAnimatorSet.cancel();
		}
		if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
			
		}
		else {
			hideAnimatorSet = new AnimatorSet();
			ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(toolbar, "translationY",
					toolbar.getTranslationY(), -toolbar.getHeight());// 将ToolBar隐藏到上面
			ArrayList<Animator> animators = new ArrayList<Animator>();
			animators.add(headerAnimator);
			hideAnimatorSet.setDuration(300);
			hideAnimatorSet.playTogether(animators);
			hideAnimatorSet.start();
		}
	}

}
