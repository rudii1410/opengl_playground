package core.entities

import core.InputHandler
import core.math.Vector3
import org.lwjgl.glfw.GLFW
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class Camera(
    val player: Player,
    val position: Vector3 = Vector3(0f, 0f, 0f),
    var pitch: Float = 25f,
    var yaw: Float = 0f,
    var roll: Float = 0f
) {
    private var distanceFromPlayer = 50f
    private var angleAroundPlayer = 0f

    init {
        InputHandler.mouseOnScroll {
            distanceFromPlayer -= (it * 1f)
        }
        InputHandler.mouseDeltaMove { dx, dy ->
            if (InputHandler.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
                pitch += (dy * 0.3f)
                if (pitch <= 1f) pitch = 1f
                if (pitch >= 90f) pitch = 90f
                angleAroundPlayer -= (dx * 0.3f)
            }
        }
    }

    fun move() {
        calculateCameraPosition(
            max(calculateHorizontalDistance(), 12f),
            max(calculateVerticalDistance(), 8f)
        )
        yaw = 180 - (player.rotation.y() + angleAroundPlayer)
    }

    private fun calculateCameraPosition(horizontal: Float, vertical: Float) {
        val theta = (player.rotation.y() + angleAroundPlayer).toDouble()
        val offsetX = (horizontal * sin(Math.toRadians(theta))).toFloat()
        val offsetZ = (horizontal * cos(Math.toRadians(theta))).toFloat()
        position.x(player.position.x() - offsetX)
        position.y(player.position.y() + vertical)
        position.z(player.position.z() - offsetZ)
    }

    private fun calculateHorizontalDistance(): Float {
        return distanceFromPlayer * cos(Math.toRadians(pitch.toDouble())).toFloat()
    }

    private fun calculateVerticalDistance(): Float {
        return distanceFromPlayer * sin(Math.toRadians(pitch.toDouble())).toFloat()
    }
}