package eu.acolombo.progressindicatorview.example

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStatusBarColor(Color.TRANSPARENT)

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager){

            override fun getItem(position: Int) = when (position) {
                0 -> ShenzenFragment()
                1 -> ShenyangFragment()
                2 -> ShanghaiFragment()
                else -> Fragment()
            }

            override fun getCount() = 3

        }

    }

    fun setStatusBarColor(@ColorInt color: Int = Color.TRANSPARENT) {

        val isColorDark = ColorUtils.calculateLuminance(color) > 0.5

        window.apply {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = color
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

            decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isColorDark) {
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }

        }

    }

}
