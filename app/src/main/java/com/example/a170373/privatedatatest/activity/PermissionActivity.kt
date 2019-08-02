package com.example.a170373.privatedatatest.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.KeyEvent
import android.view.ViewStub
import android.view.WindowManager


/**
 * 默认启动全部检测
 * 单独申请
 * 1.设置回调iSource?.shouldShowRational,,onPermissonReject,onPermissionGranted
 * 2.通过 ARouter.getInstance().build(Paths.PERMISSION_PAGE).withBoolean("isFirst",false)
 * .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
 * 启动权限页面
 */
class PermissionActivity : Activity() {

    var defaultPermissionArray = arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)

    var onPermissionGranted: (() -> Unit)? = null


    val requestPermissionFunction: (() -> Unit)? = {
        val permissions = defaultPermissionArray
        var chcekPermission = chcekPermission(permissions)
        if (chcekPermission?.size ?: 0 > 0) {
            requestPermissions(chcekPermission!!)
        } else {
            onPermissionGrantedInvoke()
        }
    }
    val notAgreeFunction: (() -> Unit)? = {
        finish()
    }
    private var isFirst: Boolean = true
    val RC_REQUEST_PERMISSION = 100
    var stringStringHashMap: HashMap<String, String>? = null
    var isGoSettingBack: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
        isFirst = intent?.getBooleanExtra("isFirst", true) ?: true
        //防止从应用市场启动时从点击home后再打开应用重启的问题
        if (!isTaskRoot) {
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == intent.action) {
                finish()
                return
            }
        }
        if (isFirst) {
            requestPermissionFunction?.invoke()
            return
        }
        requestPermissions(defaultPermissionArray)
        
    }

    fun chcekPermission(per:Array<String>?):Array<String>? {
        val requestPermission = per?:defaultPermissionArray
        var needRequestPermission :ArrayList<String>? = null
        requestPermission.forEachIndexed { index, s ->
            if(ContextCompat.checkSelfPermission(this,s)
                == PackageManager.PERMISSION_DENIED){
                if(needRequestPermission == null){
                    needRequestPermission = ArrayList()
                }
                needRequestPermission?.add(s)
            }
            Log.e("permession",s)
        }

        if(needRequestPermission?.size?:0>0){
            defaultPermissionArray  = needRequestPermission?.toTypedArray()?:defaultPermissionArray
//            ARouter.getInstance().build(Paths.PERMISSION_PAGE).withBoolean("isFirst",false).withFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
            return defaultPermissionArray
        }
        return null
    }

    /**
     * 重构requestPermission,方法冗余,重复调用多次检查
     * 申请只做申请操作
     */
    fun requestPermissions(permissons: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissons, RC_REQUEST_PERMISSION)
            return
        }
        onPermissionGrantedInvoke()
    }

    override fun onResume() {
        super.onResume()

        if (isGoSettingBack) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = defaultPermissionArray ?: return
                Log.e("permissions", permissions.toString())
                val notPermissions = chcekPermission(permissions)
                if (notPermissions?.size ?: 0 > 0) {
                    showMissingPermissionDialog(notPermissions)
                } else {
                    onPermissionGrantedInvoke()
                }
            }
        }
    }

    /**
     * 权限通过并关闭权限页面
     */
    private fun onPermissionGrantedInvoke() {
        onPermissionGranted?.invoke()
        if (isFirst) {
//            ARouter.getInstance().build(Paths.HOME_PAGE).navigationSplashAnima(this)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != RC_REQUEST_PERMISSION) {
            return
        }
        // 处理申请结果
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (i in permissions.indices) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
        }
        this.onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }


    @TargetApi(Build.VERSION_CODES.M)
    internal fun onRequestPermissionsResult(
        permissions: Array<String>,
        grantResults: IntArray,
        shouldShowRequestPermissionRationale: BooleanArray
    ) {
        val length = permissions.size
        val notPermissionList: ArrayList<String> = ArrayList()
        var granted = 0
        for (i in 0 until length) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                onPermissionRefuse?.invoke()
                if (shouldShowRequestPermissionRationale[i]) {
                } else {
                }
            } else {
                notPermissionList.add(permissions[i])
                granted++
            }
        }
        if (granted == length) {
            onPermissionGrantedInvoke()
        } else {
            val notPermissions = notPermissionList.toTypedArray()
            showMissingPermissionDialog(notPermissions)
        }
        //        finish();
    }


    private fun showMissingPermissionDialog(notPermissions: Array<String>?) {
//        var pbMap = LinkedHashMap<String,String>()
//        notPermissions?.forEachIndexed { index, s ->
//            when(s){
//                Manifest.permission.ACCESS_COARSE_LOCATION->{pbMap.put("获取定位权限","精准推荐")}
//                Manifest.permission.ACCESS_FINE_LOCATION->{pbMap.put("获取定位权限","精准推荐")}
//                Manifest.permission.READ_PHONE_STATE->{pbMap.put("获取手机号码权限","校验IMEI码,保证账号安全")}
//                Manifest.permission.READ_EXTERNAL_STORAGE->{pbMap.put("存储权限","下载书籍文件降低流量消耗")}
//                Manifest.permission.WRITE_EXTERNAL_STORAGE->{pbMap.put("存储权限","下载书籍文件降低流量消耗")}
//            }
//        }
//        isGoSettingBack = true
//
//
//        pbMap.entries.forEach {
//            print("" + it.key + "\t" + it.value + "\n")
//        }
        isGoSettingBack = true
//        if("com.lianzainovel" != Config.getContext().packageName && !Config.spUtils.getBoolean(C.SP_AGREEPRIVACYTIP,false)) {
        AlertDialog.Builder(this)
            .setTitle("开启鸿雁传书完整体验")
            .setMessage("为了您能享受鸿雁传书完整体验，需要定位、存储、通话权限来提供服务")
            .setPositiveButton(
                "设置"
            ) { dialog, which ->
                startAppSettings()
            }
            .setCancelable(false)
            .show()
//            return
//        }
//        cl_root.setBackgroundResource(R.color.black_transtion_40)
//        initGoSetting()
    }

    /**
     * 启动应用权限的设置
     *
     * @since 2.5.0
     */
    private fun startAppSettings() {
        val localIntent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(localIntent)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent) = (keyCode == KeyEvent.KEYCODE_BACK)
}
