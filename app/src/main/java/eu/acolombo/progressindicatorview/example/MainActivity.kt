package eu.acolombo.progressindicatorview.example

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
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
                0 -> HackerFragment()
                1 -> FlightFragment()
                2 -> BloodFragment()
                else -> Fragment()
            }

            override fun getCount() = 3

        }
        viewPager.offscreenPageLimit = 0

    }

}
