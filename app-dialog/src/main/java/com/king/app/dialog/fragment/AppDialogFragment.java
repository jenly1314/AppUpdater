package com.king.app.dialog.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.king.app.dialog.AppDialogConfig;
import com.king.app.dialog.R;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogFragment extends BaseDialogFragment {

    private AppDialogConfig config;

    public static AppDialogFragment newInstance(AppDialogConfig config) {

        Bundle args = new Bundle();
        AppDialogFragment fragment = new AppDialogFragment();
        fragment.config = config;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootLayoutId() {
        if(config == null){
            config = new AppDialogConfig();
        }
        return config.getLayoutId();
    }

    public void init(View rootView){
        if(config!=null){
            TextView tvDialogTitle = rootView.findViewById(config.getTitleId());
            setText(tvDialogTitle,config.getTitle());
            tvDialogTitle.setVisibility(config.isHideTitle() ? View.GONE : View.VISIBLE);

            TextView tvDialogContent = rootView.findViewById(config.getContentId());
            setText(tvDialogContent,config.getContent());

            Button btnDialogCancel = rootView.findViewById(config.getCancelId());
            setText(btnDialogCancel,config.getCancel());
            btnDialogCancel.setOnClickListener(config.getOnClickCancel() != null ? config.getOnClickCancel() : getOnClickDismiss());
            btnDialogCancel.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);

            try{
                //不强制要求要有中间的线
                View line = rootView.findViewById(R.id.line);
                line.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);
            }catch (Exception e){

            }

            Button btnDialogOK = rootView.findViewById(config.getOkId());
            setText(btnDialogOK,config.getOk());
            btnDialogOK.setOnClickListener(config.getOnClickOk() != null ? config.getOnClickOk() : getOnClickDismiss());

        }
    }
}
