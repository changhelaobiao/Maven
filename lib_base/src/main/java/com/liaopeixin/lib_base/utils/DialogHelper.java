package com.liaopeixin.lib_base.utils;

import android.app.Dialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huanghuaqiao
 * @date 2021/12/31
 */
public class DialogHelper {

    private static List<Dialog> mDialogList=new ArrayList<>();

    public static void addDialog(Dialog dialog){
        mDialogList.add(dialog);
    }

    public static void dismissAllDialog(){
        if (mDialogList==null||mDialogList.size()==0) return;
        for (Dialog dialog: mDialogList){
            if (dialog!=null){
                dialog.dismiss();
            }
        }
        mDialogList.clear();
    }

}
