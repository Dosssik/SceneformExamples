package com.dosssik.scenefromexamples.fragment

import android.net.Uri
import androidx.core.animation.doOnEnd
import com.dosssik.scenefromexamples.R
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import kotlinx.android.synthetic.main.bone_connection_fragment.sceneView
import kotlinx.android.synthetic.main.bone_connection_fragment.showOrHideConnectedNode
import kotlinx.android.synthetic.main.bone_connection_fragment.startAnimation
import java.io.File

class BoneConnectionFragment: BaseSceneformFragment(R.layout.bone_connection_fragment) {

    val hatNode = Node()

    override fun getSceneView(): SceneView = sceneView

    override fun onRenderableReady() {
        loadHat()
    }

    private fun loadHat() {
        val uri = Uri.fromFile(File("//android_asset/baseball-cap.sfb"))
        ModelRenderable.builder()
            .setSource(activity, uri)
            .build()
            .thenAccept { renderable ->
                connectHatToRobot(renderable)
                enableButtons()
            }
            .exceptionally { throwable ->
                throwable.printStackTrace()
                null
            }
    }

    private fun connectHatToRobot(renderable: ModelRenderable) {
        val boneNode = Node()
        boneNode.setParent(robotNode)
        robotNode.setBoneAttachment("joint12", boneNode)
        hatNode.setParent(boneNode)
        hatNode.renderable = renderable
        hatNode.worldScale = Vector3(0.25F, 0.25F, 0.25F)
        val position = hatNode.worldPosition
        position.y -= 0.08F
        hatNode.worldPosition = position
    }

    private fun enableButtons() {
        startAnimation.isEnabled = true
        startAnimation.setOnClickListener {
            startAnimation()
        }
        showOrHideConnectedNode.isEnabled = true
        showOrHideConnectedNode.setOnClickListener {
            hatNode.isEnabled = hatNode.isEnabled.not()
        }
    }

    private fun startAnimation() {
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