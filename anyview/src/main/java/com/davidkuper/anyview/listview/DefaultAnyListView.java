package com.davidkuper.anyview.listview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.davidkuper.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by malingyi on 2017/3/2.
 */

public class DefaultAnyListView extends ListView implements AbsListView.OnScrollListener {
    private static final int DONE = 0;
    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;
    private static final int RATIO = 3;
    private LinearLayout headerView;
    private int headerViewHeight;
    private float startY;
    private float offsetY;
    private TextView tvPullToRefresh;
    private OnMeiTuanRefreshListener mOnRefreshListener;
    private OnMeiTuanLoadingListener mOnLoadingListener;
    private int state;
    private int mFirstVisibleItemIndex;
    private int mLastVisibleItemIndex;
    private boolean isLoadingMore;
    private boolean isRecord;
    private boolean isEnd;
    private boolean isRefreable;
    private FrameLayout mAnimContainer;
    private Animation animation;
    private SimpleDateFormat format;
    private FirstHeadView mFirstView;
    private SecondHeadView mSecondView;
    private AnimationDrawable secondAnim;
    private ThirdHeadView mThirdView;
    private AnimationDrawable thirdAnim;
    private ExecutorService delayRun;
    private boolean isDelayRun = false;
    private static final int SLEEP_TIME = 4000;

    private BottomLoadingView mBottomLoadingView;

    public DefaultAnyListView(Context context) {
        super(context);
        init(context);
    }

    public DefaultAnyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DefaultAnyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnMeiTuanRefreshListener{
        void onRefresh();
    }

    public interface OnMeiTuanLoadingListener{
        void onLoadMore();
    }

    /**
     * 回调接口，想实现下拉刷新的listview实现此接口
     * @param onRefreshListener
     */
    public void setOnMeiTuanRefreshListener(OnMeiTuanRefreshListener onRefreshListener){
        mOnRefreshListener = onRefreshListener;
        isRefreable = true;
    }

    public void setOnMeituanLoadingListener(OnMeiTuanLoadingListener onMeituanLoadingListener){
        this.mOnLoadingListener = onMeituanLoadingListener;
    }

    /**
     * 刷新完毕，从主线程发送过来，并且改变headerView的状态和文字动画信息
     */
    public void setOnRefreshComplete(){
        //一定要将isEnd设置为true，以便于下次的下拉刷新
        isEnd = true;
        state = DONE;

        changeHeaderByState(state);
    }

    public void setOnLoadMoreComplete() {
        mBottomLoadingView.stopAnim();
        mBottomLoadingView.setVisibility(GONE);

        isLoadingMore = false;
    }

    private void init(Context context) {
        setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        setOnScrollListener(this);


        mBottomLoadingView = new BottomLoadingView(getContext());
        mBottomLoadingView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        headerView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.meituan_item, null, false);
        mFirstView = (FirstHeadView) headerView.findViewById(R.id.first_view);
        tvPullToRefresh = (TextView) headerView.findViewById(R.id.tv_pull_to_refresh);
        mSecondView = (SecondHeadView) headerView.findViewById(R.id.second_view);
        mSecondView.setBackgroundResource(R.drawable.pull_to_refresh_second_anim);
        secondAnim = (AnimationDrawable) mSecondView.getBackground();
        mThirdView = (ThirdHeadView) headerView.findViewById(R.id.third_view);
        mThirdView.setBackgroundResource(R.drawable.pull_to_refresh_third_anim);
        thirdAnim = (AnimationDrawable) mThirdView.getBackground();

        measureView(headerView);
        addHeaderView(headerView);
        addFooterView(mBottomLoadingView);
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        Log.e("RoadMapListView","headerViewHeight="+headerViewHeight);

        delayRun = Executors.newFixedThreadPool(1);
        state = DONE;
        isEnd = true;
        isRefreable = false;
    }




    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItemIndex = firstVisibleItem;
        mLastVisibleItemIndex = firstVisibleItem + visibleItemCount;
        if (mLastVisibleItemIndex >= totalItemCount&&totalItemCount > visibleItemCount){
            if (mBottomLoadingView == null){
                mBottomLoadingView = new BottomLoadingView(getContext());
            }
            mBottomLoadingView.startAnim();
            mBottomLoadingView.setVisibility(VISIBLE);
            //如果当前没有正在加载数据
            if (mOnLoadingListener != null && !isLoadingMore){
                isLoadingMore = true;
                mOnLoadingListener.onLoadMore();
            }
        }else {
            if (mBottomLoadingView != null){
                mBottomLoadingView.setVisibility(GONE);
            }
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }
    private static final int FIRST_VISIBLE_INDEX = 0;
    private static final int OFF_DIVIDE = 3;
    private static final int PROGRESS_CONSTANT = 1;
    private static final int OFFSET_Y = 0;
    private static final int ZERAO = 0;
    private static final int DURATION = 500;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnd) {//如果现在时结束的状态，即刷新完毕了，可以再次刷新了，在onRefreshComplete中设置
            if (isRefreable) {//如果现在是可刷新状态   在setOnMeiTuanListener中设置为true
                switch (ev.getAction()){
                    //用户按下
                    case MotionEvent.ACTION_DOWN:
                        //如果当前是在listview顶部并且没有记录y坐标
                        if (mFirstVisibleItemIndex == FIRST_VISIBLE_INDEX && !isRecord) {
                            //将isRecord置为true，说明现在已记录y坐标
                            isRecord = true;
                            //将当前y坐标赋值给startY起始y坐标
                            startY = ev.getY();
                        }
                        break;
                    //用户滑动
                    case MotionEvent.ACTION_MOVE:
                        //再次得到y坐标，用来和startY相减来计算offsetY位移值
                        float tempY = ev.getY();
                        //再起判断一下是否为listview顶部并且没有记录y坐标
                        if (mFirstVisibleItemIndex == FIRST_VISIBLE_INDEX && !isRecord) {
                            isRecord = true;
                            startY = tempY;
                        }
                        //如果当前状态不是正在刷新的状态，并且已经记录了y坐标
                        if (state!=REFRESHING && isRecord ) {
                            //计算y的偏移量
                            offsetY = tempY - startY;
                            //计算当前滑动的高度
                            float currentHeight = (-headerViewHeight+offsetY/OFF_DIVIDE);
                            //用当前滑动的高度和头部headerView的总高度进行比 计算出当前滑动的百分比 0到1
                            float currentProgress = PROGRESS_CONSTANT+currentHeight/headerViewHeight;
                            //如果当前百分比大于1了，将其设置为1，目的是让第一个状态的椭圆不再继续变大
                            if (currentProgress>=PROGRESS_CONSTANT) {
                                currentProgress = PROGRESS_CONSTANT;
                            }
                            //如果当前的状态是放开刷新，并且已经记录y坐标
                            if (state == RELEASE_TO_REFRESH && isRecord) {
                                setSelection(FIRST_VISIBLE_INDEX);
                                //如果当前滑动的距离小于headerView的总高度
                                if (-headerViewHeight+offsetY/RATIO<OFFSET_Y) {
                                    //将状态置为下拉刷新状态
                                    state = PULL_TO_REFRESH;
                                    //根据状态改变headerView，主要是更新动画和文字等信息
                                    changeHeaderByState(state);
                                    //如果当前y的位移值小于0，即为headerView隐藏了
                                }else if (offsetY<=OFFSET_Y) {
                                    //将状态变为done
                                    state = DONE;
                                    //根据状态改变headerView，主要是更新动画和文字等信息
                                    changeHeaderByState(state);
                                }
                            }
                            //如果当前状态为下拉刷新并且已经记录y坐标
                            if (state == PULL_TO_REFRESH && isRecord) {
                                setSelection(FIRST_VISIBLE_INDEX);
                                //如果下拉距离大于等于headerView的总高度
                                if (-headerViewHeight+offsetY/RATIO>=OFFSET_Y) {
                                    //将状态变为放开刷新
                                    state = RELEASE_TO_REFRESH;
                                    //根据状态改变headerView，主要是更新动画和文字等信息
                                    changeHeaderByState(state);
                                    //如果当前y的位移值小于0，即为headerView隐藏了
                                }else if (offsetY<=OFFSET_Y) {
                                    //将状态变为done
                                    state = DONE;
                                    //根据状态改变headerView，主要是更新动画和文字等信息
                                    changeHeaderByState(state);
                                }
                            }
                            //如果当前状态为done并且已经记录y坐标
                            if (state == DONE && isRecord) {
                                //如果位移值大于0
                                if (offsetY>=OFFSET_Y) {
                                    //将状态改为下拉刷新状态
                                    state = PULL_TO_REFRESH;
                                }
                            }
                            //如果为下拉刷新状态
                            if (state == PULL_TO_REFRESH) {
                                //则改变headerView的padding来实现下拉的效果
                                headerView.setPadding(ZERAO,(int)(-headerViewHeight+offsetY/RATIO) ,ZERAO,ZERAO);
                                //给第一个状态的View设置当前进度值
                                mFirstView.setCurrentProgress(currentProgress);
                                //重画
                                mFirstView.postInvalidate();
                            }
                            //如果为放开刷新状态
                            if (state == RELEASE_TO_REFRESH) {
                                //改变headerView的padding值
                                headerView.setPadding(ZERAO,(int)(-headerViewHeight+offsetY/RATIO) ,ZERAO, ZERAO);
                                //给第一个状态的View设置当前进度值
                                mFirstView.setCurrentProgress(currentProgress);
                                //重画
                                mFirstView.postInvalidate();
                            }
                        }
                        break;
                    //当用户手指抬起时
                    case MotionEvent.ACTION_UP:
                        //如果当前状态为下拉刷新状态
                        if (state == PULL_TO_REFRESH) {
                            //平滑的隐藏headerView
                            this.smoothScrollBy((int)(-headerViewHeight+offsetY/RATIO)+headerViewHeight, DURATION);
                            //根据状态改变headerView
                            changeHeaderByState(state);
                        }
                        //如果当前状态为放开刷新
                        if (state == RELEASE_TO_REFRESH) {
                            //平滑的滑到正好显示headerView
                            this.smoothScrollBy((int)(-headerViewHeight+offsetY/RATIO), DURATION);
                            //将当前状态设置为正在刷新
                            state = REFRESHING;
                            //回调接口的onRefresh方法
                            mOnRefreshListener.onRefresh();
                            //根据状态改变headerView
                            changeHeaderByState(state);
                        }
                        //这一套手势执行完，一定别忘了将记录y坐标的isRecord改为false，以便于下一次手势的执行
                        isRecord = false;
                        break;
                    default:
                }

            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据状态改变headerView的动画和文字显示
     * @param state
     */
    private void changeHeaderByState(int state){
        switch (state) {
            case DONE://如果的隐藏的状态
                //设置headerView的padding为隐藏
                headerView.setPadding(ZERAO, -headerViewHeight, ZERAO, ZERAO);
                //第一状态的view显示出来
                mFirstView.setVisibility(View.VISIBLE);
                //第二状态的view隐藏起来
                mSecondView.setVisibility(View.GONE);
                //停止第二状态的动画
                secondAnim.stop();
                //第三状态的view隐藏起来
                mThirdView.setVisibility(View.GONE);
                //停止第三状态的动画
                thirdAnim.stop();
                break;
            case RELEASE_TO_REFRESH://当前状态为放开刷新
                //文字显示为放开刷新
                tvPullToRefresh.setText("放开刷新");
                //第一状态view隐藏起来
                mFirstView.setVisibility(View.GONE);
                //第二状态view显示出来
                mSecondView.setVisibility(View.VISIBLE);
                //播放第二状态的动画
                secondAnim.start();
                //第三状态view隐藏起来
                mThirdView.setVisibility(View.GONE);
                //停止第三状态的动画
                thirdAnim.stop();
                break;
            case PULL_TO_REFRESH://当前状态为下拉刷新
                //设置文字为下拉刷新
                tvPullToRefresh.setText("下拉刷新");
                //第一状态view显示出来
                mFirstView.setVisibility(View.VISIBLE);
                //第二状态view隐藏起来
                mSecondView.setVisibility(View.GONE);
                //第二状态动画停止
                secondAnim.stop();
                //第三状态view隐藏起来
                mThirdView.setVisibility(View.GONE);
                //第三状态动画停止
                thirdAnim.stop();
                break;
            case REFRESHING://当前状态为正在刷新
                //文字设置为正在刷新
                tvPullToRefresh.setText("正在刷新");
                //第一状态view隐藏起来
                mFirstView.setVisibility(View.GONE);
                //第三状态view显示出来
                mThirdView.setVisibility(View.VISIBLE);
                //第二状态view隐藏起来
                mSecondView.setVisibility(View.GONE);
                //停止第二状态动画
                secondAnim.stop();
                //启动第三状态view
                thirdAnim.start();
                //启动监控,4s后自动恢复初始状态
                delayRun.submit(new Runnable() {
                    @Override public void run() {
                        if (!isDelayRun) {
                            isDelayRun = true;
                            try {
                                //休眠4s
                                Thread.sleep(SLEEP_TIME);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //4s结束，恢复初始状态
                            if (DefaultAnyListView.this.state == REFRESHING) {
                                DefaultAnyListView.this.state = DONE;
                                isDelayRun = false;
                                post(new Runnable() {
                                    @Override public void run() {
                                        changeHeaderByState(DefaultAnyListView.this.state);
                                    }
                                });
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }


    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(ZERAO, ZERAO + ZERAO, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > ZERAO) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(ZERAO, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }



}
