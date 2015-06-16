package com.purezhihudaily.ui.widget;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 侧向滑出菜单的ListView
 * 该效果的实现是基于在Item的布局中通过设置PaddingLeft和PaddingRight来隐藏左右菜单的
 */
public class SlideListView extends ListView {

	/** 禁止侧滑模式 */
	public static int MOD_FORBID = 0;
	/** 从左向右滑出菜单模式 */
	public static int MOD_LEFT = 1;
	/** 从右向左滑出菜单模式 */
	public static int MOD_RIGHT = 2;
	/** 左右均可以滑出菜单模式 */
	public static int MOD_BOTH = 3;
	/** 当前的模式 */
	private int mode = MOD_FORBID;
	/** 左侧菜单的长度 */
	private int leftLength = 0;
	/** 右侧菜单的长度 */
	private int rightLength = 0;

	/**
	 * 当前滑动的ListView　position
	 */
	private int slidePosition;
	/**
	 * 手指按下X的坐标
	 */
	private int downY;
	/**
	 * 手指按下Y的坐标
	 */
	private int downX;
	/**
	 * ListView的item
	 */
	private View itemView;
	/**
	 * 滑动类
	 */
	private Scroller scroller;
	/**
	 * 认为是用户滑动的最小距离
	 */
	private int mTouchSlop;

	/**
	 * 判断是否可以侧向滑动
	 */
	private boolean canMove = false;
	/**
	 * 标示是否完成侧滑
	 */
	private boolean isSlided = false;

	private AnimatorSet backAnimatorSet; // 这是显示头尾元素使用的动画
	private AnimatorSet hideAnimatorSet; // 这是隐藏头尾元素使用的动画

	private float lastY = 0f;
	private float currentY = 0f;
	// 下面两个表示滑动的方向，大于0表示向下滑动，小于0表示向上滑动，等于0表示未滑动
	private int lastDirection = 0;
	private int currentDirection = 0;

	private Toolbar toolbar;

	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
	}

	/**
	 * 获取滑动的position
	 * 
	 * @return
	 */
	public int getSlidePosition() {
		return slidePosition;
	}

	public SlideListView(Context context) {
		this(context, null);
	}

	public SlideListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	/**
	 * 初始化菜单的滑出模式
	 * 
	 * @param mode
	 */
	public void initSlideMode(int mode) {
		this.mode = mode;
	}

	/**
	 * 处理我们拖动ListView item的逻辑
	 */
	@SuppressLint({ "ClickableViewAccessibility", "Recycle" })
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		int lastX = (int) ev.getX();

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				lastY = ev.getY();
				currentY = ev.getY();
				currentDirection = 0;
				lastDirection = 0;

				/* 当前模式不允许滑动，则直接返回，交给ListView自身去处理 */
				if (this.mode == MOD_FORBID) {
					return super.onTouchEvent(ev);
				}

				// 如果处于侧滑完成状态，侧滑回去，并直接返回
				if (isSlided) {
					scrollBack();
					return false;
				}

				// 假如scroller滚动还没有结束，我们直接返回
				if (!scroller.isFinished()) {
					return false;
				}
				downX = (int) ev.getX();
				downY = (int) ev.getY();

				slidePosition = pointToPosition(downX, downY);

				// 无效的position, 不做任何处理
				if (slidePosition == AdapterView.INVALID_POSITION) {
					return super.onTouchEvent(ev);
				}

				// 获取我们点击的item view
				itemView = getChildAt(slidePosition - getFirstVisiblePosition());

				/* 此处根据设置的滑动模式，自动获取左侧或右侧菜单的长度 */
				if (this.mode == MOD_BOTH) {
					this.leftLength = -itemView.getPaddingLeft();
					this.rightLength = -itemView.getPaddingRight();
				}
				else if (this.mode == MOD_LEFT) {
					this.leftLength = -itemView.getPaddingLeft();
				}
				else if (this.mode == MOD_RIGHT) {
					this.rightLength = -itemView.getPaddingRight();
				}

				break;
			case MotionEvent.ACTION_MOVE:

				/* 防止一滚动就将第一行显示出来 */
				if (getFirstVisiblePosition() > 0) {
					float tmpCurrentY = ev.getY();
					if (Math.abs(tmpCurrentY - lastY) > 0) {
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
				}

				if (!canMove && slidePosition != AdapterView.INVALID_POSITION && (Math.abs(ev.getX() - downX) > mTouchSlop && Math.abs(ev.getY() - downY) < mTouchSlop)) {
					int offsetX = downX - lastX;
					if (offsetX > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)) {
						/* 从右向左滑 */
						canMove = true;
					}
					else if (offsetX < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT)) {
						/* 从左向右滑 */
						canMove = true;
					}
					else {
						canMove = false;
					}
					/* 此段代码是为了避免在侧向滑动时同时触发ListView的OnItemClickListener时间 */
					MotionEvent cancelEvent = MotionEvent.obtain(ev);
					cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
					onTouchEvent(cancelEvent);
				}
				if (canMove) {
					/* 设置此属性，可以在侧向滑动时，保持ListView不会上下滚动 */
					requestDisallowInterceptTouchEvent(true);

					// 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
					int deltaX = downX - lastX;
					if (deltaX < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT)) {
						/* 向左滑 */
						itemView.scrollTo(deltaX, 0);
					}
					else if (deltaX > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)) {
						/* 向右滑 */
						itemView.scrollTo(deltaX, 0);
					}
					else {
						itemView.scrollTo(0, 0);
					}
					return true; // 拖动的时候ListView不滚动
				}
			case MotionEvent.ACTION_UP:
				if (canMove) {
					canMove = false;
					scrollByDistanceX();
					/* 此段代码是为了避免我们在侧向滑动时同时出发ListView的OnItemClickListener时间 */
					MotionEvent cancelEvent = MotionEvent.obtain(ev);
					cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
					onTouchEvent(cancelEvent);
				}
				break;
		}

		// 否则直接交给ListView来处理onTouchEvent事件
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
	 */
	private void scrollByDistanceX() {
		/* 当前模式不允许滑动，则直接返回 */
		if (this.mode == MOD_FORBID) {
			return;
		}
		if (itemView.getScrollX() > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)) {
			/* 从右向左滑 */
			if (itemView.getScrollX() >= 0) {
				scrollLeft();
			}
			else {
				// 滚回到原始位置
				scrollBack();
			}
		}
		else if (itemView.getScrollX() < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT)) {
			/* 从左向右滑 */
			if (itemView.getScrollX() <= 0) {
				scrollRight();
			}
			else {
				// 滚回到原始位置
				scrollBack();
			}
		}
		else {
			// 滚回到原始位置
			scrollBack();
		}

	}

	/**
	 * 往右滑动，getScrollX()返回的是左边缘的距离，就是以View左边缘为原点到开始滑动的距离，所以向右边滑动为负值
	 */
	private void scrollRight() {
		isSlided = true;
		final int delta = (leftLength + itemView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0, Math.abs(delta));
		postInvalidate(); // 刷新itemView
	}

	/**
	 * 向左滑动，根据上面我们知道向左滑动为正值
	 */
	private void scrollLeft() {
		isSlided = true;
		final int delta = (rightLength - itemView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0, Math.abs(delta));
		postInvalidate(); // 刷新itemView
	}

	/**
	 * 滑动会原来的位置
	 */
	private void scrollBack() {
		isSlided = false;
		scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(), 0, Math.abs(itemView.getScrollX()));
		postInvalidate(); // 刷新itemView
	}

	@Override
	public void computeScroll() {
		// 调用startScroll的时候scroller.computeScrollOffset()返回true，
		if (scroller.computeScrollOffset()) {
			// 让ListView item根据当前的滚动偏移量进行滚动
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

			postInvalidate();
		}
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
			ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), 0f);
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
			ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), -toolbar.getHeight());// 将ToolBar隐藏到上面
			ArrayList<Animator> animators = new ArrayList<Animator>();
			animators.add(headerAnimator);
			hideAnimatorSet.setDuration(300);
			hideAnimatorSet.playTogether(animators);
			hideAnimatorSet.start();
		}
	}

	// 这个Listener其实是用来对付当用户的手离开列表后列表仍然在滑动的情况，也就是SCROLL_STATE_FLING
	public AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

		int lastPosition = 0; // 上次滚动到的第一个可见元素在listview里的位置——firstVisibleItem
		int state = SCROLL_STATE_IDLE;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 记录当前列表状态
			state = scrollState;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem == 0) {
				animateBack();
			}
			if (firstVisibleItem > 0) {
				// 如果上次的位置小于当前位置，那么隐藏头尾元素
				if (firstVisibleItem > lastPosition && state == SCROLL_STATE_FLING) {
					animateHide();
				}

				if (firstVisibleItem < lastPosition && state == SCROLL_STATE_FLING) {
					animateBack();
				}
				// 还得考虑(firstVisibleItem == lastPosition && state ==
				// SCROLL_STATE_FLING)的情况

			}
			lastPosition = firstVisibleItem;
		}
	};

	/**
	 * 提供给外部调用，用以将侧滑出来的滑回去
	 */
	public void slideBack() {
		this.scrollBack();
	}

}
