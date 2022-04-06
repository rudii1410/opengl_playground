package core

import core.math.Vector3
import org.lwjgl.glfw.GLFW

class Camera {
    var position = Vector3()
        private set
    var pitch = 0f
        private set
    var yaw = 0f
        private set
    var roll = 0f
        private set

    fun move() {
        if (InputHandler.keyDown(GLFW.GLFW_KEY_W)) position.z(position.z() + SPEED)
        if (InputHandler.keyDown(GLFW.GLFW_KEY_S)) position.z(position.z() - SPEED)
        if (InputHandler.keyDown(GLFW.GLFW_KEY_A)) position.x(position.x() + SPEED)
        if (InputHandler.keyDown(GLFW.GLFW_KEY_D)) position.x(position.x() - SPEED)
    }

    companion object {
        private const val SPEED = 0.2f
    }
}