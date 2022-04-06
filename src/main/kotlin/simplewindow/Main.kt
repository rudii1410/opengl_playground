package simplewindow

import core.Camera
import core.Entity
import core.OBJLoader
import core.math.Vector3
import core.renderengine.DisplayManager
import core.renderengine.Loader
import core.renderengine.Renderer
import simplewindow.model.TexturedModel
import simplewindow.shader.StaticShader
import simplewindow.texture.ModelTexture

fun main() {
    val displayManager = DisplayManager(title = "Simple Window")
    val loader = Loader()
    val shader = StaticShader()
    val renderer = Renderer(shader)

    val vertices = floatArrayOf(
        -0.5f,0.5f,-0.5f,
        -0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        0.5f,0.5f,-0.5f,

        -0.5f,0.5f,0.5f,
        -0.5f,-0.5f,0.5f,
        0.5f,-0.5f,0.5f,
        0.5f,0.5f,0.5f,

        0.5f,0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,0.5f,
        0.5f,0.5f,0.5f,

        -0.5f,0.5f,-0.5f,
        -0.5f,-0.5f,-0.5f,
        -0.5f,-0.5f,0.5f,
        -0.5f,0.5f,0.5f,

        -0.5f,0.5f,0.5f,
        -0.5f,0.5f,-0.5f,
        0.5f,0.5f,-0.5f,
        0.5f,0.5f,0.5f,

        -0.5f,-0.5f,0.5f,
        -0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,0.5f
    )
    val indices = intArrayOf(
        0,1,3,
        3,1,2,
        4,5,7,
        7,5,6,
        8,9,11,
        11,9,10,
        12,13,15,
        15,13,14,
        16,17,19,
        19,17,18,
        20,21,23,
        23,21,22
    )
    val textures = floatArrayOf(
        0f,0f,
        0f,1f,
        1f,1f,
        1f,0f,
        0f,0f,
        0f,1f,
        1f,1f,
        1f,0f,
        0f,0f,
        0f,1f,
        1f,1f,
        1f,0f,
        0f,0f,
        0f,1f,
        1f,1f,
        1f,0f,
        0f,0f,
        0f,1f,
        1f,1f,
        1f,0f,
        0f,0f,
        0f,1f,
        1f,1f,
        1f,0f
    )

    val model = OBJLoader.loadObjModel("src/main/resources/stall.obj", loader)
    val texture = ModelTexture(loader.loadTexture("src/main/resources/stallTexture.png"))
    val texturedModel = TexturedModel(model, texture)
    val entity = Entity(texturedModel, Vector3(0f, 0f, -50f), Vector3(0f), 1f)
    val camera = Camera()

    displayManager.loop {
        entity.increaseRotation(0f, 1f, 0f)
        camera.move()
        renderer.prepare()
        shader.start()
        shader.loadViewMatrix(camera)
        renderer.render(entity, shader)
        shader.stop()
    }

    shader.cleanUp()
    loader.cleanUp()
    displayManager.closeDisplay()
}