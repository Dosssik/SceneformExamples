package com.dosssik.scenefromexamples.fragment

import android.graphics.Color
import com.dosssik.scenefromexamples.R
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Light
import kotlinx.android.synthetic.main.lights_fragment.additionalLightButton
import kotlinx.android.synthetic.main.lights_fragment.sceneView
import kotlinx.android.synthetic.main.lights_fragment.sunlightButton

class LightsFragment : BaseSceneformFragment(R.layout.lights_fragment) {

    private val lightNode = Node()

    override fun getSceneView(): SceneView = sceneView

    override fun onRenderableReady() {
        sunlightButton.isEnabled = true
        additionalLightButton.isEnabled = true
        addAdditionalLightSource()
        sunlightButton.setOnClickListener {
            val sunlight = getSceneView().scene.sunlight ?: return@setOnClickListener
            sunlight.isEnabled = sunlight.isEnabled.not()
        }
        additionalLightButton.setOnClickListener {
            lightNode.isEnabled = lightNode.isEnabled.not()
        }
    }

    private fun addAdditionalLightSource() {
        val scene = getSceneView().scene
        lightNode.apply {
            setLookDirection(LightConfig.LOOK_DIRECTION)
            light = Light.builder(LightConfig.TYPE)
                .setShadowCastingEnabled(LightConfig.SHADOW_CASTING_ENABLED)
                .setColorTemperature(LightConfig.TEMPERATURE)
                .setIntensity(LightConfig.INTENSITY)
                .setColor(LightConfig.COLOR)
                .build()
            isEnabled = false
            localPosition = scene.camera?.localPosition
            worldPosition = scene.camera?.worldPosition
            setParent(scene)
        }
    }
}


private object LightConfig {
    const val TEMPERATURE = 50F
    const val INTENSITY = 350F
    const val SHADOW_CASTING_ENABLED = false
    val TYPE = Light.Type.DIRECTIONAL
    val COLOR = com.google.ar.sceneform.rendering.Color(Color.RED)
    val LOOK_DIRECTION = Vector3(-0.2f, -0.2f, -0.7f)
}