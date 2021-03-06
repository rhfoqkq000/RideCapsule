package com.npe.horse.travel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * Created by JRokH on 2017-10-30.
 */

public class SearchViewCustom extends MaterialSearchView {

    private Context context;

    public SearchViewCustom(Context context) {
        super(context);
        this.context = context;
    }

    public SearchViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SearchViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            KeyEvent.DispatcherState state = getKeyDispatcherState();
            if (state != null) {
                InputMethodManager mgr = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mgr != null) {
                    mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
                }
                // TODO: Hide your view as you do it in your activity
                if (isSearchOpen()) {
                    closeSearch();
                }
                return true;
            }
        }

        return super.dispatchKeyEventPreIme(event);
    }


    //    @Override
//    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            // User has pressed Back key. So hide the keyboard
//            Log.d("onkeyPrelme","back");
//            InputMethodManager mgr = (InputMethodManager)
//                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (mgr != null) {
//                mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
//            }
//            // TODO: Hide your view as you do it in your activity
//            if (isSearchOpen()) {
//                closeSearch();
//            }
//            return true;
//        }
//        return false;
//    }
}