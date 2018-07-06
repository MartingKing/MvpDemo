package com.jinzaofintech.rebate.ui.activity


import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebSettings
import android.webkit.WebView
import com.github.lzyzsd.jsbridge.BridgeWebViewClient
import com.jinzaofintech.commonlib.http.utils.NetWorkState
import com.jinzaofintech.commonlib.ui.base.BaseActivity
import com.jinzaofintech.commonlib.utils.Utils
import com.jinzaofintech.rebate.R
import com.jinzaofintech.rebate.bean.RebateCash
import com.jinzaofintech.rebate.bean.RebateResult
import com.jinzaofintech.rebate.presenter.RebateWebViewPresenter
import com.jinzaofintech.rebate.presenter.impl.RebateWebViewPresenterImpl
import com.jinzaofintech.rebate.utils.AppWebChromeClient
import com.jinzaofintech.rebate.utils.IWebPageView
import com.jinzaofintech.rebate.view.RebateResultDialog
import com.jinzaofintech.rebate.view.RebateSuccessDialog
import kotlinx.android.synthetic.main.activity_rebate_web.*
import java.util.regex.Pattern


/**
 *
 * @author zengwendi
 * @date 2018/6/5
 * 跳转到第三方平台webview
 */

class RebateWebViewActivity : BaseActivity<RebateWebViewPresenterImpl>(), RebateWebViewPresenter.View, IWebPageView {

    override fun onSuccess(rebateResult: RebateResult) {
        when (rebateResult.status) {
            1 -> {
                RebateSuccessDialog(this, rebateResult.content) {}.show()
            }
            2 -> {
                RebateResultDialog(this, rebateResult.content, 2) {}.show()
            }
            3 -> {
                RebateResultDialog(this, rebateResult.content, 2) {}.show()
            }
            4 -> {
                RebateResultDialog(this, rebateResult.content, 1) {}.show()
            }
        }
//        EventBusUtil.sendEvent(Event<RebateResult>(Event.EventCode.A, rebateResult))
//        if (rebateResult.is_close == 1) {
//            finish()
//        }
    }

    override fun progressChanged(newProgress: Int) {//进度条
        if (newProgress >= 100) {
            progressBar.visibility = View.GONE
        } else {
            progressBar.visibility = View.VISIBLE
            progressBar.progress = newProgress
        }
    }

    private val mRebateCash by lazy {
        intent.getSerializableExtra(REBATE_CASH) as RebateCash
    }
    /**
     * 提供webView缓存数据
     */
    private var mCacheData = ""

    /**
     * 默认title
     */
    private val mTitle by lazy {
        intent.getStringExtra(TITLE)
    }
    /**
     * url
     */
    private val mUrl by lazy {
        intent.getStringExtra(URL)
    }

    private val mWebChromeClient: AppWebChromeClient by lazy {
        AppWebChromeClient(this)
    }


    companion object {
        const val URL = "url"
        const val TITLE = "title"
        const val REBATE_CASH = "rebate_cash"

        /**
         * 打开网页:
         *
         * @param mContext 上下文
         * @param mUrl     要加载的网页url
         * @param mTitle   title 加载默认title
         */
        fun loadUrl(mContext: Context, mUrl: String, mTitle: String, rebateCash: RebateCash) {
            val intent = Intent(mContext, RebateWebViewActivity::class.java)
            intent.putExtra(URL, mUrl)
            intent.putExtra(TITLE, mTitle)
            intent.putExtra(REBATE_CASH, rebateCash)
            mContext.startActivity(intent)
        }
    }

    override fun initData() {
    }

    override fun initPresenter() {
        mPresenter = RebateWebViewPresenterImpl()
    }

    override fun loadData() {
        state = NetWorkState.STATE_SUCCESS
    }

    override fun getLayoutId(): Int = R.layout.activity_rebate_web

    override fun onLeftClick() {
        onBackPressed()
    }

    override fun initView() {
        webClose.setOnClickListener {
            finish()
        }
        webTitle.text = if (!TextUtils.isEmpty(mTitle)) {
            mTitle
        } else {
            getString(R.string.loading)
        }
        initWebView()
        if (mUrl != null) {
            webView.loadUrl(mUrl)
        }
        webView.webViewClient = object : BridgeWebViewClient(webView) {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view != null && TextUtils.isEmpty(mTitle)) {
                    webTitle.text = view.title
                }
                mRebateCash.urls.forEach {
                    //正则判断是否匹配
                    if (Pattern.matches(it.pattern, url)) {
                        //是否显示页面
                        llDisposePage.visibility = if (it.is_show == 2) {
                            webTitle.text = "处理中"
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                        view?.loadUrl("javascript:${it.js}")
                        return@forEach
                    }
                }
            }
        }

        webView.registerHandler("getCacheData") { _, function ->
            function.onCallBack(mCacheData)
        }
        webView.registerHandler("setCacheData") { data, function ->
            mCacheData = data
            function.onCallBack("true")
        }
        webView.registerHandler("postData") { data, function ->
            mPresenter.postMoney(data, mRebateCash.productId)
            function.onCallBack("true")
        }
    }

    private fun initWebView() {
        clearCookie()
        val ws = webView.settings
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.loadWithOverviewMode = false
        // 启动应用缓存
        ws.setAppCacheEnabled(true)
        // 设置缓存模式
        ws.cacheMode = WebSettings.LOAD_DEFAULT
        // 设置此属性，可任意比例缩放。
        ws.useWideViewPort = true
        //  页面加载好以后，再放开图片
        ws.blockNetworkImage = false
        // 使用localStorage则必须打开
        ws.domStorageEnabled = true
        // 排版适应屏幕
        ws.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows(true)
        //设置agent
        ws.userAgentString = "${webView.settings.userAgentString}/rebateuseragen_android&/${Utils.getAppUserAgent()}"
        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.webChromeClient = mWebChromeClient
        webView.isSetTrustURL = false
    }


    //退出网页
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            //退出网页
        } else {
            webView.loadUrl("about:blank")
            super.onBackPressed()
        }
    }

    override fun onDestroy() {//释放内存
        super.onDestroy()
        if (webView != null) {
            var parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.settings.javaScriptEnabled = false
            webView.clearHistory()
            webView.clearView()
            webView.removeAllViews()
            try {
                webView.destroy();
            } catch (ex: Throwable) {
            }
        }
    }

    /**
     * 清除cookie
     */
    private fun clearCookie() {
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true)
        var cookieSyncManager = CookieSyncManager.createInstance(webView.context)
        var cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.removeSessionCookie()
        cookieManager.removeAllCookie()
    }
}
