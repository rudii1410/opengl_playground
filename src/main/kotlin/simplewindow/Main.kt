package simplewindow

import core.renderengine.DisplayManager
import core.renderengine.Loader
import core.renderengine.Renderer

fun main() {
    val displayManager = DisplayManager(title = "Simple Window")

    val loader = Loader()
    val renderer = Renderer()

    val vertices = floatArrayOf(
        -0.5f, 0.5f, 0f,
        -0.5f, -0.5f, 0f,
        0.5f, -0.5f, 0f,
        0.5f, -0.5f, 0f,
        0.5f, 0.5f, 0f,
        -0.5f, 0.5f, 0f
    )
    val model = loader.loadToVao(vertices);

    displayManager.loop {
        renderer.prepare()
        renderer.render(model)
    }

    loader.cleanUp()
    displayManager.closeDisplay()
}