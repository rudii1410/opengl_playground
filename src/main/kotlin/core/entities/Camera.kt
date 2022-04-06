package core.entities

import core.InputHandler
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
    private var speedMultiplier = 1f

    fun move() {
        if (InputHandler.keyDown(GLFW.GLFW_KEY_W)) position.z(position.z() - getSpeed())
        if (InputHandler.keyDown(GLFW.GLFW_KEY_S)) position.z(position.z() + getSpeed())
        if (InputHandler.keyDown(GLFW.GLFW_KEY_A)) position.x(position.x() - getSpeed())
        if (InputHandler.keyDown(GLFW.GLFW_KEY_D)) position.x(position.x() + getSpeed())
        if (InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) position.y(position.y() - getSpeed())
        if (InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) position.y(position.y() + getSpeed())
        if (InputHandler.keyDown(GLFW.GLFW_KEY_Q)) yaw += getSpeed()
        if (InputHandler.keyDown(GLFW.GLFW_KEY_E)) yaw -= getSpeed()
        if (InputHandler.keyPressed(GLFW.GLFW_KEY_SPACE)) speedMultiplier = 5f
        if (InputHandler.keyReleased(GLFW.GLFW_KEY_SPACE)) speedMultiplier = 1f
    }

    private fun getSpeed(): Float {
        return SPEED * speedMultiplier
    }

    companion object {
        private const val SPEED = 0.2f
    }
}