package com.dosssik.scenefromexamples.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.SkeletonNode
import com.google.ar.sceneform.collision.Ray
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import java.io.File

private const val MODEL_SCALE = 8F
private const val CAMERA_FOV = 60F

abstract class BaseSceneformFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {

    open val needToRotate = true
    val robotNode = SkeletonNode()

    lateinit var renderable: ModelRenderable

    abstract fun onRenderableReady()
    abstract fun getSceneView(): SceneView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRenderable()
    }

    override fun onResume() {
        super.onResume()
        getSceneView().resume()
    }

    override fun onPause() {
        getSceneView().pause()
        super.onPause()
    }

    override fun onDestroyView() {
        getSceneView().destroy()
        super.onDestroyView()
    }

    private fun loadRenderable() {
        val uri = Uri.fromFile(File("//android_asset/model.sfb"))
        ModelRenderable.builder()
            .setSource(activity, uri)
            .build()
            .thenAccept { renderable ->
                this.renderable = renderable
                showModel()
                onRenderableReady()
            }
            .exceptionally { throwable ->
                throwable.printStackTrace()
                null
            }
    }

    private fun showModel() {
        val camera = getSceneView().scene.camera
        camera.verticalFovDegrees = CAMERA_FOV
        val ray = Ray(camera.worldPosition, camera.forward)
        val position = ray.getPoint(1.0f)
        position.y -= 0.5f

        robotNode.setParent(getSceneView().scene)
        robotNode.worldPosition = position
        robotNode.renderable = renderable
        robotNode.worldScale = Vector3(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE)

        if (needToRotate) {
            val vectorY = Vector3(0f, 1f, 0f)
            val rotationY = Quaternion.axisAngle(vectorY, 180f)
            robotNode.localRotation = rotationY
        }
    }
}