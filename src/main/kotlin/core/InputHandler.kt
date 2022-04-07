package core

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorPosCallbackI
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.glfw.GLFWScrollCallbackI


object InputHandler {
    private var window: Long = 0
    private const val KEYBOARD_SIZE = 512
    private const val MOUSE_SIZE = 16
    private val keyStates = IntArray(KEYBOARD_SIZE)
    private val activeKeys = BooleanArray(KEYBOARD_SIZE)
    private val mouseButtonStates = IntArray(MOUSE_SIZE)
    private val activeMouseButtons = BooleanArray(MOUSE_SIZE)
    private var lastMouseNS: Long = 0
    private const val mouseDoubleClickPeriodNS = (1000000000 / 5).toLong()
    private const val NO_STATE = -1
    private var onMouseScroll: ((offset: Float) -> Unit)? = null
    private var onMouseMove: ((dx: Float, dy: Float) -> Unit)? = null
    private var keyboard: GLFWKeyCallback = object : GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            activeKeys[key] = action != GLFW_RELEASE
            keyStates[key] = action
        }
    }
    private var mouse: GLFWMouseButtonCallback = object : GLFWMouseButtonCallback() {
        override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
            activeMouseButtons[button] = action != GLFW_RELEASE
            mouseButtonStates[button] = action
        }
    }
    private var mouseScroll: GLFWScrollCallbackI = GLFWScrollCallbackI { _, xoffset, yoffset ->
        onMouseScroll?.invoke(yoffset.toFloat())
    }

    private var oldPosX: Double = 0.0
    private var oldPosY: Double = 0.0
    private var newPosX: Double = 0.0
    private var newPosY: Double = 0.0
    private var mouseMove: GLFWCursorPosCallbackI = GLFWCursorPosCallbackI { _, xpos, ypos ->
        oldPosX = newPosX
        oldPosY = newPosY

        onMouseMove?.invoke(
            (xpos - oldPosX).toFloat(),
            (ypos - oldPosY).toFloat()
        )

        newPosX = xpos
        newPosY = ypos
    }

    internal fun init(window: Long) {
        InputHandler.window = window

        glfwSetKeyCallback(window, keyboard)
        glfwSetMouseButtonCallback(window, mouse)
        glfwSetScrollCallback(window, mouseScroll)
        glfwSetCursorPosCallback(window, mouseMove)

        resetKeyboard()
        resetMouse()
    }

    internal fun update() {
        resetKeyboard()
        resetMouse()
    }

    private fun resetKeyboard() {
        for (i in keyStates.indices) {
            keyStates[i] = NO_STATE
        }
    }

    private fun resetMouse() {
        for (i in mouseButtonStates.indices) {
            mouseButtonStates[i] = NO_STATE
        }
        val now = System.nanoTime()
        if (now - lastMouseNS > mouseDoubleClickPeriodNS) lastMouseNS = 0
    }

    fun keyDown(key: Int): Boolean {
        return activeKeys[key]
    }

    fun keyPressed(key: Int): Boolean {
        return keyStates[key] == GLFW_PRESS
    }

    fun keyReleased(key: Int): Boolean {
        return keyStates[key] == GLFW_RELEASE
    }

    fun mouseButtonDown(button: Int): Boolean {
        return activeMouseButtons[button]
    }

    fun mouseButtonPressed(button: Int): Boolean {
        return mouseButtonStates[button] == GLFW_RELEASE
    }

    fun mouseButtonReleased(button: Int): Boolean {
        val flag = mouseButtonStates[button] == GLFW_RELEASE
        if (flag) lastMouseNS = System.nanoTime()
        return flag
    }

    fun mouseButtonDoubleClicked(button: Int): Boolean {
        val last = lastMouseNS
        val flag = mouseButtonReleased(button)
        val now = System.nanoTime()
        if (flag && now - last < mouseDoubleClickPeriodNS) {
            lastMouseNS = 0
            return true
        }
        return false
    }

    fun mouseOnScroll(cb: (offset: Float)->Unit) {
        this.onMouseScroll = cb
    }

    fun mouseDeltaMove(cb: (dx: Float, dy: Float) -> Unit) {
        this.onMouseMove = cb
    }
}