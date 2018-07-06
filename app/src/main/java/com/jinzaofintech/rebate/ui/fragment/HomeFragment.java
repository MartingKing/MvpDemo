package com.jinzaofintech.rebate.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.jinzaofintech.commonlib.app.BaseApplication;
import com.jinzaofintech.commonlib.http.utils.NetWorkState;
import com.jinzaofintech.commonlib.ui.base.BaseFragment;
import com.jinzaofintech.commonlib.utils.TextUtils;
import com.jinzaofintech.commonlib.view.TextViewIcon;
import com.jinzaofintech.rebate.R;
import com.jinzaofintech.rebate.adapter.HotRecomendAdapter;
import com.jinzaofintech.rebate.bean.Banner;
import com.jinzaofintech.rebate.bean.HomeDataResponse;
import com.jinzaofintech.rebate.bean.JpGood;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.bean.Notice;
import com.jinzaofintech.rebate.bean.RebateCash;
import com.jinzaofintech.rebate.presenter.HomePresenter;
import com.jinzaofintech.rebate.presenter.impl.HomePresenterImpl;
import com.jinzaofintech.rebate.ui.activity.ChangeZFBAccountActivity;
import com.jinzaofintech.rebate.ui.activity.LoginActivity;
import com.jinzaofintech.rebate.ui.activity.RebateWebViewActivity;
import com.jinzaofintech.rebate.ui.activity.WidthdrawActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/4.
 */

public class HomeFragment extends BaseFragment<HomePresenterImpl> implements HomePresenter.View, Animation.AnimationListener {

    @BindView(R.id.ll_withdraw)
    LinearLayout mLlWithdraw;
    @BindView(R.id.convenient_banner)
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.viewflipper)
    ViewFlipper mViewFlipper;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.rl_hot)
    RelativeLayout mRlHot;
    @BindView(R.id.tv_restamount)
    TextView mTvRestamount;
    @BindView(R.id.iv_feedback)
    TextView mIvFeedback;
    @BindView(R.id.iv_vivid_right)
    TextView mIvVividRight;
    @BindView(R.id.iv_vivid_left)
    TextView mIvVividLeft;
    @BindView(R.id.tv_icon_right)
    TextViewIcon mTvIconRight;
    @BindView(R.id.iv_hot)
    ImageView mIvHot;

    private Boolean isFirstLoad = true;
    private List<JpGood> mDatas;

    private HotRecomendAdapter mHotRecomendAdapter;

    private HomeDataResponse mData;
    private Handler mHandler = new Handler();

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenterImpl();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
        mPresenter.getHomeData();
        setState(NetWorkState.STATE_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_home;
    }


    @Override
    protected void initView() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatas = new ArrayList<>();
        mHotRecomendAdapter = new HotRecomendAdapter(R.layout.item_home_recomend, mDatas);
        mRecyclerview.setAdapter(mHotRecomendAdapter);
        initAds();
        initEvent();
    }

    private void initEvent() {
        mRlHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getCash("1");
            }
        });
        mLlWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getInstance().getUser() != null) {
                    if (mData.getStatus() == 1) {
                        startActivity(new Intent(getContext(), WidthdrawActivity.class)
                                .putExtra(MyConstants.FROM_WHERE, "home_frag")
                                .putExtra(MyConstants.ALI_ACCOUNT, mData.getAlipay())
                                .putExtra(MyConstants.ALI_REALNAME, mData.getAliname())
                                .putExtra(MyConstants.REST_AMOUNT, mData.getBanlance()));
                    } else {
                        startActivity(new Intent(getContext(), ChangeZFBAccountActivity.class)
                                .putExtra(MyConstants.FROM_WHERE, "home_widthdraw")
                                .putExtra(MyConstants.REST_AMOUNT, mData.getBanlance()));
                    }

                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });
    }

    private void initAds() {
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(3000);
        mViewFlipper.setInAnimation(getContext(), R.anim.home_notic_bottom_in);
        mViewFlipper.setOutAnimation(getContext(), R.anim.home_notic_top_out);
    }

    @Override
    public void requestHomeData(HomeDataResponse homeDataResponse) {
        isFirstLoad = false;
        mData = homeDataResponse;
        mPresenter.downloadHotPic(homeDataResponse.getHotgood().getImg(), picName);
        setData(homeDataResponse);
    }

    private String picName = "hot.png";

    @Override
    public void downloadHotPicSuccess(String msg) {
        setData(mData);
        File file = new File(MyConstants.APP_IMAGE_DIR + picName);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(MyConstants.APP_IMAGE_DIR + picName);
            mRlHot.setBackground(new BitmapDrawable(bitmap));
        } else {
            mRlHot.setBackgroundResource(R.mipmap.bg_hot);
        }
    }

    //跳转到爆品详情
    @Override
    public void requestCash(RebateCash cash) {
        cash.setProductId(String.valueOf(mData.getHotgood().getId()));
        RebateWebViewActivity.Companion.loadUrl(getContext(), "https://passport.tuandai.com/2login", "", cash);
    }

    public void setData(HomeDataResponse data) {
        initAnimation();
        initBanner(data);
//        Glide.with(getContext()).load(data.getHotgood().getImg()).into(mIvHot);
        mTvRestamount.setText(TextUtils.getTwoStrAppendText("￥", TextUtils.getDefaultText(data.getBanlance(), "0")));
        mHotRecomendAdapter.setNewData(data.getJp_good());
        if (mViewFlipper.isFlipping()) mViewFlipper.stopFlipping();
        mViewFlipper.removeAllViews();
        if (data.getNotice().size() == 0) {
            mViewFlipper.setVisibility(View.GONE);
            return;
        }
        for (Notice bean : data.getNotice()) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_ads, null);
            TextView tvNotic = view.findViewById(R.id.tv_notic_msg);
            tvNotic.setText(bean.getNotice());
            mViewFlipper.addView(view);
        }
        mViewFlipper.startFlipping();
    }

    private Animation animationFeedback, animationRight, animationLeft;

    private void initAnimation() {
        animationFeedback = AnimationUtils.loadAnimation(getContext(), R.anim.home_vivid_feedback_anim);
        animationRight = AnimationUtils.loadAnimation(getContext(), R.anim.home_vivid_right_anim);
        animationLeft = AnimationUtils.loadAnimation(getContext(), R.anim.home_vivid_left_anim);
        animationFeedback.setAnimationListener(this);
        animationRight.setAnimationListener(this);
        animationLeft.setAnimationListener(this);
        mIvFeedback.startAnimation(animationFeedback);
        mIvVividRight.startAnimation(animationRight);
        mIvVividLeft.startAnimation(animationLeft);
    }

    private void initBanner(HomeDataResponse data) {

        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, data.getBanner())
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.banner_point_normal, R.drawable.banner_point_select})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(2000)
                .setCanLoop(true);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIvFeedback.startAnimation(animationFeedback);
                mIvVividRight.startAnimation(animationRight);
                mIvVividLeft.startAnimation(animationLeft);
            }
        }, 200);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public class LocalImageHolderView implements Holder<Banner> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Banner data) {
            Glide.with(getContext()).load(data.getUrl()).into(imageView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewFlipper != null)
            mViewFlipper.stopFlipping();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoad) {
            mPresenter.getHomeData();
        }
        if (mViewFlipper != null && mViewFlipper.getChildCount() > 1) {
            mViewFlipper.startFlipping();
        }
    }
}
