package com.liaopeixin.lib_mvvm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * @author huanghuaqiao
 * @date 2021/12/22
 * 网络加载弹窗
 */
public class SpectrumLoadingDialog extends Dialog {

    private TextView dialog_show_text;
    private Context mContext;

    public SpectrumLoadingDialog(@NonNull Context context) {
        this(context, R.style.spectrum_loading_dialog);
    }

    public SpectrumLoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_view);
        dialog_show_text = findViewById(R.id.dialog_show_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = this.getWindow();
        if (window!=null){
            window.setWindowAnimations(R.style.dialog_enter_exit_anim);
        }
    }

    public void setLoadingTitle(String title){
        if (dialog_show_text!=null){
            dialog_show_text.setText(title);
        }
    }

    public void isCancelable(boolean cancelAble){
        this.setCancelable(cancelAble);
        this.setCanceledOnTouchOutside(cancelAble);
    }

}
