package com.liaopeixin.lib_mvvm;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @author huanghuaqiao
 * @date 2021/11/9
 * 权限申请工具
 * 使用：调用requestPermission方法即可
 */
public class PermissionUtil {

    private static RequestList mRequestList=new RequestList();

    /**
     * 申请权限
     * @param activity
     * @param permissions
     * @param onPermissionListener
     */
    public static void requestPermission(Activity activity, String[] permissions, int requestCode, OnPermissionListener onPermissionListener){
        if (!isOverMarshMallow()&&onPermissionListener!=null&&activity!=null){
            onPermissionListener.onPermissionSuccess();
        }else {
            //获取还没授权的权限列表
            List<String> permissionsNeed = findPermissionsNeed(activity, permissions);
            if (permissionsNeed.size()>0){
                //申请还需要申请的权限
                doRequestPermission(activity,permissionsNeed.toArray(new String[permissionsNeed.size()]),requestCode,onPermissionListener);
            }else if (onPermissionListener!=null){
                onPermissionListener.onPermissionSuccess();
            }

        }

    }

    /**
     * 申请权限系统回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(Activity activity,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (activity!=null&&mRequestList!=null&&grantResults.length>0) {
            RequestList.RequestBean requestBean = mRequestList.get(activity, requestCode);
            if (requestBean==null||requestBean.onPermissionListener==null){
                return;
            }
            //权限是否已全部授权
            boolean isAllPermit = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    isAllPermit = false;
                    break;
                }
            }
            if (isAllPermit) {
                requestBean.onPermissionListener.onPermissionSuccess();
            } else {
                requestBean.onPermissionListener.onPermissionFail();
            }

        }
    }

    /**
     * 申请系统权限
     * @param activity
     * @param permissions
     * @param requestCode
     */
    private static void doRequestPermission(Activity activity, String[] permissions, int requestCode,OnPermissionListener onPermissionListener){
        if (isOverMarshMallow()&&activity!=null&&mRequestList!=null) {
            RequestList.RequestBean requestBean = new RequestList.RequestBean(requestCode,onPermissionListener);
            mRequestList.add(activity,requestCode,requestBean);
            activity.requestPermissions(permissions,requestCode);
        }
    }

    /**
     * 获取还需要申请的权限列表
     * @param permissions
     * @return
     */
    private static List<String> findPermissionsNeed(Activity activity, String[] permissions){
        //需要申请的权限列表
        ArrayList permissionsNeed = new ArrayList();
        if (activity==null||permissions==null||permissions.length==0){
            return permissionsNeed;
        }
        for (String permission : permissions) {
            if (isOverMarshMallow() && activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeed.add(permission);
            }
        }
        return permissionsNeed;
    }

    /**
     * 判断是否所有权限都授权
     * @param activity
     * @param permissions
     * @returns
     */
    public static boolean isAllPermissionsGranted(Activity activity,String[] permissions){
        if (activity==null){
            return false;
        }
        if (!isOverMarshMallow()){
            return true;
        }
        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回拒绝且不再询问的权限
     * 注意：此方法只能在权限请求接口回调中调用
     * @param activity
     * @param permissions
     * @returns
     */
    public static List<String> getPermissionsDenyAlways(Activity activity,String[] permissions){
        if (activity==null||permissions==null||permissions.length==0){
            return null;
        }
        ArrayList<String> denyAlwaysPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (isOverMarshMallow()&&activity.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED&&!activity.shouldShowRequestPermissionRationale(permission)){
                denyAlwaysPermissionList.add(permission);
            }
        }
        return denyAlwaysPermissionList;
    }

    /**
     * 判断是否有权限拒绝申请且不再询问
     * 注意：此方法只能在权限请求接口回调中调用
     * @param activity
     * @param permissions
     * @returns
     */
    public static boolean hasPermissionsDenyAlways(Activity activity,String[] permissions){
        if (activity==null||permissions==null||permissions.length==0||!isOverMarshMallow()){
            return false;
        }
        return getPermissionsDenyAlways(activity,permissions)!=null&&getPermissionsDenyAlways(activity,permissions).size()>0;
    }

    /**
     * 前往系统权限申请页
     * @param activity
     * @param requestCode
     */
    public static void goToPermissionsRequestPage(Activity activity,int requestCode){
        if (activity==null){
            return;
        }
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 系统版本是否安卓6.0(棉花糖)以上，需要申请系统权限
     * @return
     */
    private static boolean isOverMarshMallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 用于保存和取用不同的权限申请操作
     */
    private static class RequestList{
        private Map<String,RequestBean> permissionMap=new HashMap();

        private void add(Activity activity,int requestCode,RequestBean requestBean){
            if (permissionMap==null){
                return;
            }
            permissionMap.put(getRequestKey(activity,requestCode),requestBean);
        }

        private RequestBean get(Activity activity,int requestCode){
            if (permissionMap==null||activity==null){
                return null;
            }
            RequestBean requestBean = permissionMap.remove(getRequestKey(activity,requestCode));
            return requestBean;
        }

        //以当前的activity名和requestCode组合作为key
        private String getRequestKey(Activity activity, int requestCode){
            return String.format("%s#%d",activity.getClass().getName(),requestCode);
        }

        private static class RequestBean{

           private int requestCode;
           private OnPermissionListener onPermissionListener;

            public RequestBean(int requestCode,OnPermissionListener onPermissionListener){
                this.requestCode=requestCode;
                this.onPermissionListener=onPermissionListener;
            }
        }

    }

    public interface OnPermissionListener {
        void onPermissionSuccess();
        void onPermissionFail();
    }

}
