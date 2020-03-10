package com.dosssik.scenefromexamples.fragment

import com.dosssik.scenefromexamples.R
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.rendering.Texture
import kotlinx.android.synthetic.main.change_texture_fragment.changeTextureButton
import kotlinx.android.synthetic.main.change_texture_fragment.sceneView

private const val TEXTURE_NAME = "baseColorMap"

class ChangeTextureFragment: BaseSceneformFragment(R.layout.change_texture_fragment) {

    override fun getSceneView(): SceneView = sceneView

    override fun onRenderableReady() {
        changeTextureButton.isEnabled = true
        changeTextureButton.setOnClickListener {
            changeTextureButton.isEnabled = false
            changeTexture()
        }
    }

    private fun changeTexture() {
        val textureResource = listOf(
            R.drawable.apples,
            R.drawable.oranges,
            R.drawable.harold
        )
            .random()

        Texture.builder()
            .setSource(activity, textureResource)
            .setUsage(Texture.Usage.COLOR)
            .build()
            .thenAccept { texture ->
                val material = renderable.getMaterial(0)
                material.setTexture(TEXTURE_NAME, texture)
                changeTextureButton.isEnabled = true
            }
    }
}