package com.npe.horse.travel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by pmkjkr on 2017. 3. 16..
 */

public class ProgressDialogController {

    private Context context;
    private ProgressDialog mProgressDialog;


    public ProgressDialogController(Context context) {
        this.context = context;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("로딩 중입니다.");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }
}
