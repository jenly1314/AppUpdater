package com.king.app.dialog.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class BaseDialogFragment extends DialogFragment {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mRootView = inflater.inflate(getRootLayoutId(), container, false);
        init(mRootView);
        return mRootView;
    }

    protected View getRootView(){
        return mRootView;
    }

    protected void setText(TextView tv, CharSequence text){
        if(!TextUtils.isEmpty(text)){
            tv.setText(text);
        }
    }

    protected View.OnClickListener getOnClickDismiss(){
        return mOnClickDismissDialog;
    }

    private View.OnClickListener mOnClickDismissDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public abstract int getRootLayoutId();

    public abstract void init(View rootView);

}
