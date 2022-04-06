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
        if (InputHandler.keyDown(GLFW.GLFW_KEY_W)) position.sub(0f, 0f, -0.1f)
        if (InputHandler.keyDown(GLFW.GLFW_KEY_S)) position.sub(0f, 0f, 0.1f)
        if (InputHandler.keyDown(GLFW.GLFW_KEY_D)) position.sub(0.02f, 0f, 0f)
        if (InputHandler.keyDown(GLFW.GLFW_KEY_A)) position.sub(-0.02f, 0f, 0f)
    }
}