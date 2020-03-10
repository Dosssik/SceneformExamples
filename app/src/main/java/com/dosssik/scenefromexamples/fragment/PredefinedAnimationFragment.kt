package com.dosssik.scenefromexamples.fragment

import androidx.core.animation.doOnEnd
import com.dosssik.scenefromexamples.R
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.animation.ModelAnimator
import kotlinx.android.synthetic.main.predefined_animation_fragment_layout.sceneView
import kotlinx.android.synthetic.main.predefined_animation_fragment_layout.startAnimation

class PredefinedAnimationFragment: BaseSceneformFragment(R.layout.predefined_animation_fragment_layout) {

    override fun getSceneView(): SceneView = sceneView

    override fun onRenderableReady() {
        startAnimation.isEnabled = renderable.animationDataCount != 0
        startAnimation.setOnClickListener {
            onStartAnimationClicked()
        }
    }

    private fun onStartAnimationClicked() {
        startAnimation.isEnabled = false
        val animationNumber = (0 until renderable.animationDataCount).random()

        val animationData = renderable.getAnimationData(animationNumber)
        val animator = ModelAnimator(animationData, renderable)
        animator.start()

        animator.doOnEnd {
            startAnimation?.isEnabled = true
        }
    }
}