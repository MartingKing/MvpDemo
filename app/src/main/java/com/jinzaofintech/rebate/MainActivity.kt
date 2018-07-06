package com.jinzaofintech.rebate

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.githang.statusbar.StatusBarCompat
import com.jinzaofintech.commonlib.app.BaseApplication
import com.jinzaofintech.commonlib.http.utils.NetWorkState
import com.jinzaofintech.commonlib.ui.base.BaseActivity
import com.jinzaofintech.rebate.adapter.BaseViewPagerAdapter
import com.jinzaofintech.rebate.app.AppActivityManager
import com.jinzaofintech.rebate.bean.MyConstants
import com.jinzaofintech.rebate.presenter.MainPresenter
import com.jinzaofintech.rebate.presenter.impl.MainPresenterImpl
import com.jinzaofintech.rebate.ui.activity.LoginActivity
import com.jinzaofintech.rebate.ui.fragment.AccountFragment
import com.jinzaofintech.rebate.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class MainActivity : BaseActivity<MainPresenterImpl>(), MainPresenter.View {

    private var homeFragment: HomeFragment? = null
    private var accountFragment: AccountFragment? = null
    private var fragmentList = ArrayList<Fragment>()
    private var mAdapter: BaseViewPagerAdapter? = null;
    private var menuItem: MenuItem? = null

    override fun initPresenter() {
        mPresenter = MainPresenterImpl()
    }

    override fun initData() {

    }

    override fun loadData() {
        state = NetWorkState.STATE_SUCCESS
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white))
        //自动登录
        mPresenter.autoLogin()
        bottom_navigation.setOnNavigationItemSelectedListener(mBottomNavigationView)
        homeFragment = HomeFragment()
        accountFragment = AccountFragment()
        fragmentList.add(homeFragment!!)
        fragmentList.add(accountFragment!!)
        mAdapter = BaseViewPagerAdapter(supportFragmentManager, fragmentList)
        viewpager.adapter = mAdapter
        viewpager.offscreenPageLimit = fragmentList.size
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (menuItem != null) {
                    menuItem!!.isChecked = false
                } else {
                    bottom_navigation.getMenu().getItem(0).isChecked = false
                }
                menuItem = bottom_navigation.getMenu().getItem(position)
                menuItem!!.isChecked = true
            }
        })
    }

    private var mBottomNavigationView = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                viewpager.currentItem = 0
                StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white))
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                if (BaseApplication.mInstance.user == null) {
                    startActivity(Intent(this, LoginActivity::class.java)
                            .putExtra(MyConstants.HOME2LOGIN, true))
                    return@OnNavigationItemSelectedListener false
                }
                viewpager.currentItem = 1
                StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorFEC043))
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun messageEventBus(msg: String) {
        AppActivityManager.finishAllActivity()
        if (msg == MyConstants.OTHERS2HOME) {
            viewpager.currentItem = 0
            StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white))
            mBottomNavigationView.onNavigationItemSelected(menuItem!!.setActionView(viewpager.currentItem))
        }
        if (msg == MyConstants.LOGIN2ACCOUNT) {
            viewpager.currentItem = 1
            mBottomNavigationView.onNavigationItemSelected(menuItem!!.setActionView(viewpager.currentItem))
        }
    }
}
