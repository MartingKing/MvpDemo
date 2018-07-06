package com.jinzaofintech.rebate.presenter.impl;

import com.jinzaofintech.commonlib.bean.ApiRes;
import com.jinzaofintech.commonlib.http.Callback;
import com.jinzaofintech.commonlib.http.utils.HttpOnNextListener;
import com.jinzaofintech.commonlib.presenter.base.BasePresenter;
import com.jinzaofintech.commonlib.utils.LogUtils;
import com.jinzaofintech.commonlib.utils.ToastUtils;
import com.jinzaofintech.rebate.bean.HomeDataResponse;
import com.jinzaofintech.rebate.bean.MyConstants;
import com.jinzaofintech.rebate.bean.RebateCash;
import com.jinzaofintech.rebate.http.AppApiManager;
import com.jinzaofintech.rebate.presenter.HomePresenter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 首页
 */

public class HomePresenterImpl extends BasePresenter<HomePresenter.View> implements HomePresenter.Presenter {


    public static void writeResponseBodyToDisk(String imageName, ResponseBody body) {
        if (body == null) {
            ToastUtils.showShortToast("图片源错误");
        }
        try {
            InputStream is = body.byteStream();
            File fileDr = new File(MyConstants.APP_IMAGE_DIR);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File file = new File(MyConstants.APP_IMAGE_DIR, imageName);
            if (file.exists()) {
                file.delete();
                file = new File(MyConstants.APP_IMAGE_DIR, imageName);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            LogUtils.e("图片保存 IOException:" + e);
            e.printStackTrace();
        }
    }

    @Override
    public void getHomeData() {
        HttpOnNextListener<HomeDataResponse> listener = new HttpOnNextListener<HomeDataResponse>() {
            @Override
            public void onNext(HomeDataResponse homeinfo) {
                mView.requestHomeData(homeinfo);
            }
        };
        listener.setShowToast(true).setErrorPage(false);
        invoke(AppApiManager.getInstence().getService().getHomeFragData(), new Callback<ApiRes<HomeDataResponse>>(listener));
    }

    @Override
    public void getCash(String id) {
        HttpOnNextListener<RebateCash> listener = new HttpOnNextListener<RebateCash>() {
            @Override
            public void onNext(RebateCash cash) {
                mView.requestCash(cash);
            }
        };
        listener.setShowToast(true).setErrorPage(false).setLoadingDialog(true);
        invoke(AppApiManager.getInstence().getService().getCash(id), new Callback<ApiRes<RebateCash>>(listener));
    }

    @Override
    public void downloadHotPic(String path, final String imageName) {
        Call<ResponseBody> resultCall = AppApiManager.getInstence().getService().downloadLatestFeature(path);
        resultCall.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writeResponseBodyToDisk(imageName, response.body());
                    }
                }).start();
                mView.downloadHotPicSuccess(response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
