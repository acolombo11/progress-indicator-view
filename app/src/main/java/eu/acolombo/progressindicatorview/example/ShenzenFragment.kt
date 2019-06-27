package eu.acolombo.progressindicatorview.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rd.animation.type.AnimationType
import kotlinx.android.synthetic.main.fragment_shenzen.*

class ShenzenFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_shenzen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressIndicatorView.setAnimationType(AnimationType.THIN_WORM)
        progressIndicatorView.count = 5
        progressIndicatorView.min = 0
        progressIndicatorView.max = 100
        progressIndicatorView.animationDuration = 300L

    }
}