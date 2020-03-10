package com.dosssik.scenefromexamples.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dosssik.scenefromexamples.R
import kotlinx.android.synthetic.main.menu_fragment.boneConnection
import kotlinx.android.synthetic.main.menu_fragment.flingAnimation
import kotlinx.android.synthetic.main.menu_fragment.lights
import kotlinx.android.synthetic.main.menu_fragment.moveCamera
import kotlinx.android.synthetic.main.menu_fragment.predefinedAnimation
import kotlinx.android.synthetic.main.menu_fragment.textureChange

class MenuFragment : Fragment(R.layout.menu_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        predefinedAnimation.setOnClickListener {
            showFragment(PredefinedAnimationFragment())
        }
        flingAnimation.setOnClickListener {
            showFragment(FlingAnimationFragment())
        }
        textureChange.setOnClickListener {
            showFragment(ChangeTextureFragment())
        }
        boneConnection.setOnClickListener {
            showFragment(BoneConnectionFragment())
        }
        moveCamera.setOnClickListener {
            showFragment(MoveCameraFragment())
        }
        lights.setOnClickListener {
            showFragment(LightsFragment())
        }
    }

    private fun showFragment(fragment: Fragment) {
        val activity = activity ?: return
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}