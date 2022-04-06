package core.util

import core.Camera
import core.math.Matrix4
import core.math.Vector3

object MathUtil {
    fun createTransformationMatrix(translation: Vector3, rotation: Vector3, scale: Float): Matrix4 {
        val matrix = Matrix4()
        matrix.clearToIdentity()
        matrix.translate(translation)
        matrix.rotate(
            Math.toRadians(rotation.x().toDouble()).toFloat(),
            Vector3(1f, 0f, 0f)
        )
        matrix.rotate(
            Math.toRadians(rotation.y().toDouble()).toFloat(),
            Vector3(0f, 1f, 0f)
        )
        matrix.rotate(
            Math.toRadians(rotation.z().toDouble()).toFloat(),
            Vector3(0f, 0f, 1f)
        )
        matrix.scale(Vector3(scale))
        return matrix
    }

    fun createViewMatrix(camera: Camera): Matrix4 {
        return Matrix4().also {
            it.clearToIdentity()
            it.rotate(
                Math.toRadians(camera.pitch.toDouble()).toFloat(),
                Vector3(1f, 0f, 0f)
            )
            it.rotate(
                Math.toRadians(camera.yaw.toDouble()).toFloat(),
                Vector3(0f, 1f, 0f)
            )

            it.translate(Vector3(-camera.position.x(), -camera.position.y(), -camera.position.z()))
        }
    }
}