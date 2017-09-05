package com.wyu.iwork.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wyu.iwork.R;


/**
 * 包含上拉加载更多功能的ListView
 * @author jxh
 *
 */
public class LoadMoreListView extends ListView implements OnScrollListener{
	
	private float downY; // 按下的y坐标
	private float moveY; // 移动后的y坐标
	private ProgressBar pb;			// 进度指示器
	private OnRefreshListener mListener; // 刷新监听
	private View mFooterView;		// 脚布局
	private int mFooterViewHeight;	// 脚布局高度
	private boolean isLoadingMore; // 是否正在加载更多

	public LoadMoreListView(Context context) {
		super(context);
		init();
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化脚布局
	 * 滚动监听
	 */
	private void init() {
		initFooterView();
		
		setOnScrollListener(this);
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.layout_footer_list, null);
		
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		
		// 隐藏脚布局
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		
		addFooterView(mFooterView);
	}

	
	/**
	 * 刷新结束, 恢复界面效果
	 */
	public void onRefreshComplete() {
		if(isLoadingMore){
			// 加载更多
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadingMore = false;
		}
		
	}
	

	public interface OnRefreshListener{
		
		void onLoadMore();// 加载更多
	}
	
	public void setRefreshListener(OnRefreshListener mListener) {
		this.mListener = mListener;
	}

//    public static int SCROLL_STATE_IDLE = 0; // 空闲
//    public static int SCROLL_STATE_TOUCH_SCROLL = 1; // 触摸滑动
//    public static int SCROLL_STATE_FLING = 2; // 滑翔
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 状态更新的时候
		System.out.println("scrollState: " + scrollState);
		if(isLoadingMore){
			return; // 已经在加载更多.返回
		}
		
		// 最新状态是空闲状态, 并且当前界面显示了所有数据的最后一条. 加载更多
		if(scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition() >= (getCount() - 1)){
			isLoadingMore = true;
			System.out.println("scrollState: 开始加载更多");
			mFooterView.setPadding(0, 0, 0, 0);
			
			setSelection(getCount()); // 跳转到最后一条, 使其显示出加载更多.

			if(mListener != null){
				mListener.onLoadMore();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 滑动过程
	}


	
	

}



