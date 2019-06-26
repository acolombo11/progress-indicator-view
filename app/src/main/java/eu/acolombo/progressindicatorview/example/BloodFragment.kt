package eu.acolombo.progressindicatorview.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rd.animation.type.AnimationType
import kotlinx.android.synthetic.main.fragmnent_flight.*

class BloodFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragmnent_blood, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressIndicatorView.setAnimationType(AnimationType.SLIDE)
        progressIndicatorView.count = 8
        progressIndicatorView.min = 0
        progressIndicatorView.max = 100
        progressIndicatorView.animationDuration = 200L

    }
}