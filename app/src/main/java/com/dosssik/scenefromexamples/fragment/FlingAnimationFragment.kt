package com.dosssik.scenefromexamples.fragment

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import com.dosssik.scenefromexamples.R
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import kotlinx.android.synthetic.main.fling_fragment.sceneView
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


private const val SWIPE_THRESHOLD_VELOCITY: Float = 10F
private const val ROTATION_FRICTION: Float = 3F

class FlingAnimationFragment: BaseSceneformFragment(R.layout.fling_fragment) {

    override val needToRotate = false
    private var lastDeltaYAxisAngle: Float = 0F
    private val quaternion = Quaternion()
    private val rotateVector = Vector3.up()
    private val gestureDetector = FlingGestureDetector()

    private lateinit var mDetector: GestureDetectorCompat

    private val rotationProperty = object : FloatPropertyCompat<Node>("rotation") {

        override fun setValue(node: Node, value: Float) {
            node.localRotation = getRotationQuaternion(value)
        }

        override fun getValue(card: Node): Float = card.localRotation.y
    }

    private val animation: FlingAnimation by lazy {
        FlingAnimation(robotNode, rotationProperty).apply {
            minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_ROTATION_DEGREES
        }
    }

    override fun getSceneView(): SceneView = sceneView

    override fun onRenderableReady() {
        mDetector = GestureDetectorCompat(activity, gestureDetector)
        sceneView.setOnTouchListener { _, event ->
            mDetector.onTouchEvent(event)
        }
    }

    private fun getRotationQuaternion(deltaYAxisAngle: Float): Quaternion {
        lastDeltaYAxisAngle = deltaYAxisAngle
        return quaternion.apply {
            val arc = Math.toRadians(deltaYAxisAngle.toDouble())
            val axis = sin(arc / 2.0)
            x = (rotateVector.x * axis).toFloat()
            y = (rotateVector.y * axis).toFloat()
            z = (rotateVector.z * axis).toFloat()
            w = (cos(arc / 2.0)).toFloat()
            normalize()
        }
    }

    inner class FlingGestureDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val deltaX = -(distanceX / resources.displayMetrics.density) / ROTATION_FRICTION
            robotNode.localRotation = getRotationQuaternion(lastDeltaYAxisAngle + deltaX)
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                val deltaVelocity =
                    (velocityX / resources.displayMetrics.density) / ROTATION_FRICTION
                startAnimation(deltaVelocity)
            }
            return true
        }
    }

    private fun startAnimation(velocity: Float) {
        if (!animation.isRunning) {
            animation.setStartVelocity(velocity)
            animation.setStartValue(lastDeltaYAxisAngle)
            animation.start()
        }
    }
}