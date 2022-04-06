package simplewindow

import core.renderengine.DisplayManager
import core.renderengine.Loader
import core.renderengine.Renderer
import simplewindow.model.TexturedModel
import simplewindow.shader.StaticShader
import simplewindow.texture.ModelTexture

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
    val textures = floatArrayOf(
        1.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 0.0f,
        0.0f, 1.0f,
    )

    val model = loader.loadToVao(vertices, textures, indices)
    val texture = ModelTexture(loader.loadTexture("src/main/resources/zenitsu.png"))
    val texturedModel = TexturedModel(model, texture)

    displayManager.loop {
        renderer.prepare()
        shader.start()
        renderer.render(texturedModel)
        shader.stop()
    }

    shader.cleanUp()
    loader.cleanUp()
    displayManager.closeDisplay()
}