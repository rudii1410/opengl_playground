package core.entities

import core.InputHandler
import core.math.Vector3
import core.renderengine.DisplayManager
import org.lwjgl.glfw.GLFW
import simplewindow.model.TexturedModel
import kotlin.math.cos
import kotlin.math.sin

class Player(model: TexturedModel, position: Vector3, rotation: Vector3, scale: Float) :
    Entity(model, position, rotation, scale) {

    private var currentSpeed = 0f
    private var turnSpeed = 0f
    private var upwardSpeed = 0f
    private var isJumping = false

    fun move() {
        checkInputs()
        increaseRotation(0f, turnSpeed * DisplayManager.getFrameTimeSeconds(), 0f)
        val distance = currentSpeed * DisplayManager.getFrameTimeSeconds()
        val dx = distance * sin(Math.toRadians(rotation.y().toDouble()))
        val dz = distance * cos(Math.toRadians(rotation.y().toDouble()))
        increasePosition(dx.toFloat(), 0f, dz.toFloat())
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds()
        increasePosition(0f, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0f)
        if (position.y() <= TERRAIN_HEIGHT) {
            upwardSpeed = 0f
            position.y(TERRAIN_HEIGHT)
            isJumping = false
        }
    }

    private fun checkInputs() {
        currentSpeed = if (InputHandler.keyDown(GLFW.GLFW_KEY_W)) {
            RUN_SPEED
        } else if (InputHandler.keyDown(GLFW.GLFW_KEY_S)) {
            -RUN_SPEED
        } else {
            0f
        }
        turnSpeed = if (InputHandler.keyDown(GLFW.GLFW_KEY_A)) {
            TURN_SPEED
        } else if (InputHandler.keyDown(GLFW.GLFW_KEY_D)) {
            -TURN_SPEED
        } else {
            0f
        }

        if (InputHandler.keyDown(GLFW.GLFW_KEY_SPACE)) {
            jump()
        }

        currentSpeed *= if (InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            5f
        } else {
            1f
        }
    }

    private fun jump() {
        if (isJumping) return
        upwardSpeed = JUMP_POWER
        isJumping = true
    }

    companion object {
        private const val RUN_SPEED = 20f
        private const val TURN_SPEED = 120f
        private const val GRAVITY = -50f
        private const val JUMP_POWER = 30f
        private const val TERRAIN_HEIGHT = 0f
    }
}