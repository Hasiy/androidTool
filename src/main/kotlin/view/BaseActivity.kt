
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import top.hasiy.spinkitprogressbar.dialog.SpinkitProgressBarDialog
import top.hasiy.spinkitprogressbar.dialog.SpinkitProgressBarDialogConfig
import top.hasiy.spinkitprogressbar.dialog.SpinkitProgressBarDialogManager

/**
 * @Author: Hasiy
 * @Date: 2019/1/12 - 17 : 06
 * @LastEditors: Hasiy
 * @LastEditTime: 2019/1/12 - 17 : 06
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 * Activity基类
 */

abstract class BaseActivity : AppCompatActivity(), SpinkitProgressBarDialogManager {

    var permissionHelper: PermissionHelper? = null
    protected var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getView())
        @SuppressLint("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ActivityCollector.addActivity(this)
        StatusBarUtil.setStatusBarIsTranslucent(this)
        if (Config.USERID == "" && this !is WelcomeActivity) {
            LogUtil.toastError("App数据异常请重新启动！")
            this.finish()
        }
        initToolbar()
        initUI()
        initEvent()
        permissionHelper = PermissionHelper(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        LogUtil.w("onSaveInstanceState1")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        LogUtil.w("onSaveInstanceState2")
    }

    override var loadingIsShow: Boolean = false
    override lateinit var spinkitProgressBarDialog: SpinkitProgressBarDialog

    fun showBaseProgressBar() {
        SpinkitProgressBarDialogConfig.instance.messageShow(true)
            .spinKitColor(Color.parseColor("#438BF9")).spinKitStatus("Wave").apply()
        spinkitProgressBarDialog = SpinkitProgressBarDialog.instance("正在处理中,请稍等~")
        showSpinkitProgressBarDialog(supportFragmentManager)
    }

    fun dismissBaseProgressBar() {
        if (!this.isDestroyed && !this.isFinishing) {
            dismissSpinkitProgressBarDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home && toolbar != null) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar) as? Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowTitleEnabled(false)
            }
        }
    }

    abstract fun getView(): Int

    abstract fun initUI()

    open fun initEvent() {}

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        ActivityCollector.removeActivity(this)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionHelper!!.handleResult(requestCode, grantResults)
    }
}
