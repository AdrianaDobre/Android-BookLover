package com.example.booklover

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Property
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.example.booklover.databinding.FragmentSplashScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                View.VISIBLE
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, BooksFragment::class.java, null).commit()
        }, 10000)

        binding.animateButton.setOnClickListener {
            animate(binding.booksPic, View.TRANSLATION_Y, binding.booksPic.translationY,binding.booksPic.translationY + 100f, 500, DecelerateInterpolator())
            animate(binding.booksPic, View.ALPHA, 1.0f, 0.0f,500, LinearInterpolator())
        }
    }

    fun animate(target: ImageView, property: Property<View, Float>, from: Float, to: Float, duration: Long, interpolar: TimeInterpolator) {
        val tY = ObjectAnimator.ofFloat(target, property, from, to)
        tY.duration = duration
        tY.interpolator = interpolar
        tY.start()
    }

}