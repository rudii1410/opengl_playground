package simplewindow

import core.renderengine.DisplayManager
import core.renderengine.Loader
import core.renderengine.Renderer
import simplewindow.shader.StaticShader

fun main() {
    val displayManager = DisplayManager(title = "Simple Window")
    val loader = Loader()
    val renderer = Renderer()
    val shader = StaticShader()

    val vertices = floatArrayOf(
        -0.5f, 0.5f, 0f,
        -0.5f, -0.5f, 0f,
        0.5f, -0.5f, 0f,
        0.5f, 0.5f, 0f,
    )
    val indices = intArrayOf(
        0,1,3,
        3,1,2
    )
    val model = loader.loadToVao(vertices, indices)

    displayManager.loop {
        renderer.prepare()
        shader.start()
        renderer.render(model)
        shader.stop()
    }

    shader.cleanUp()
    loader.cleanUp()
    displayManager.closeDisplay()
}