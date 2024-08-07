package com.kavin.jutils.utils.permission;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/*

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

String premission[] = {
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
};
permissRequest = new PermissionRequest(this);
permissRequest.permissions(premission);
permissRequest.execute();

另一种权限类：
//第三方权限框架，自己GitHub上看具体使用
implementation 'com.github.getActivity:XXPermissions:18.0'

import com.hjq.permissions.XXPermissions;
private String[] permissionArray = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

XXPermissions.with(this).permission(permissionArray).request((permissions, allGranted) -> {
            if (!allGranted) {
                Toast.makeText(Camera2Activity.this, "请务必开启所有权限", Toast.LENGTH_LONG).show();
                return;
            }
            //获取权限成功，执行相关操作
        });
*/

/**
 * 权限请求类
 */
public class PermissionRequest {
    private Activity mActivity;
    private int mRequestCode;
    private String[] mPermissions;
    private boolean isAutoShowTip;
    private OnPermissionListener mListener;

    public PermissionRequest(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 设置权限请求的请求码
     *
     * @param requestCode 权限请求码
     * @return {@link PermissionRequest}
     */
    public PermissionRequest requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    /**
     * 设置需要申请的权限
     *
     * @param permissions 需要申请的权限
     * @return {@link PermissionRequest}
     */
    public PermissionRequest permissions(@NonNull String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    /**
     * 设置权限请求的回调接口
     *
     * @param listener 权限请求的回调接口
     * @return {@link PermissionRequest}
     */
    public PermissionRequest callback(OnPermissionListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * 是否自动显示拒绝授权时的提示
     *
     * @param isAutoShowTip {@code true}: 显示<br>
     *                      {@code false}: 不显示
     * @return {@link PermissionRequest}
     */
    public PermissionRequest autoShowTip(boolean isAutoShowTip) {
        this.isAutoShowTip = isAutoShowTip;
        return this;
    }

    /**
     * 执行权限请求
     */
    public void execute() {
        if (PermissionUtil.isNeedRequest()) {
            String[] deniedPermissions = PermissionUtil.getDeniedPermissions(mActivity, mPermissions);
            if (deniedPermissions.length > 0) {
                ActivityCompat.requestPermissions(mActivity, deniedPermissions, mRequestCode);
            } else {
                if (mListener != null) {
                    mListener.onPermissionGranted(mRequestCode, mPermissions);
                }
            }
        } else {
            if (mListener != null) {
                mListener.onPermissionGranted(mRequestCode, mPermissions);
            }
        }
    }

    /**
     * 获取权限请求码
     *
     * @return 权限请求码
     */
    public int getRequestCode() {
        return mRequestCode;
    }

    /**
     * 获取申请的权限
     *
     * @return 申请的权限
     */
    public String[] getPermissions() {
        return mPermissions;
    }

    /**
     * 获取是否自动显示拒绝授权时的提示
     *
     * @return {@code true}: 显示<br>
     *         {@code false}: 不显示
     */
    public boolean isAutoShowTip() {
        return isAutoShowTip;
    }

    /**
     * 获取权限请求的回调方法
     *
     * @return 权限请求的回调
     */
    public OnPermissionListener getPermissionListener() {
        return mListener;
    }
}
