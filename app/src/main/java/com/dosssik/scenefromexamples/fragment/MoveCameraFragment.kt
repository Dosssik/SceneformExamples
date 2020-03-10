package com.dosssik.scenefromexamples.fragment

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.dosssik.scenefromexamples.R
import com.google.ar.sceneform.SceneView
import kotlinx.android.synthetic.main.move_camera_fragment.moveCamera
import kotlinx.android.synthetic.main.move_camera_fragment.sceneView

class MoveCameraFragment: BaseSceneformFragment(R.layout.move_camera_fragment) {

    private var animator: Animator? = null

    override fun getSceneView(): SceneView = sceneView

    override fun onRenderableReady() {
        moveCamera.isEnabled = true
        moveCamera.setOnClickListener {
            startCameraAnimation()
        }
    }

    override fun onDestroyView() {
        animator?.cancel()
        super.onDestroyView()
    }

    private fun startCameraAnimation() {
        val camera = getSceneView().scene.camera
        val initialCameraPositionZ = camera.worldPosition.z
        animator = ValueAnimator.ofFloat(initialCameraPositionZ, 1F, initialCameraPositionZ).apply {
            duration = 3_000L
            interpolator = AnticipateOvershootInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                val cameraPosition = camera.worldPosition
                cameraPosition.z = value
                camera.worldPosition = cameraPosition
            }
            doOnStart { moveCamera.isEnabled = false }
            doOnEnd { moveCamera.isEnabled = true }
        }
        animator?.start()
    }
}