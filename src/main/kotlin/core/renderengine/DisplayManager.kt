package core.renderengine

import core.InputHandler
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL


class DisplayManager {
    private var window = 0L
    constructor(width: Int = 1280, height: Int = 720, title: String?) {
        GLFWErrorCallback.createPrint(System.err).set()

        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }

        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE) // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE) // the window will be resizable

        window = GLFW.glfwCreateWindow(width, height, title ?: "", NULL, NULL)
        if (window === NULL) throw RuntimeException("Failed to create the GLFW window")

        InputHandler.init(window)

        // Get the thread stack and push a new frame
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()) ?: throw Exception("")

            // Center the window
            GLFW.glfwSetWindowPos(
                window,
                (vidMode.width() - pWidth[0]) / 2,
                (vidMode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window)
        // Enable v-sync
        GLFW.glfwSwapInterval(1)
        GL.createCapabilities()
        GL11.glViewport(0,0,width,height)

        // Make the window visible
        GLFW.glfwShowWindow(window)
    }

    fun loop(cb: () -> Unit) {
        while (!GLFW.glfwWindowShouldClose(window)) {
            InputHandler.update()
            GLFW.glfwPollEvents()

            cb()

            GLFW.glfwSwapBuffers(window)
        }
    }

    fun closeDisplay() {
        glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null)?.free();
    }
}