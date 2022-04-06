package simplewindow

import core.entities.Camera
import core.entities.Entity
import core.OBJLoader
import core.entities.Light
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

    val texturedModel = TexturedModel(
        OBJLoader.loadObjModel("src/main/resources/stall.obj", loader),
        ModelTexture(loader.loadTexture("src/main/resources/stallTexture.png"))
    ).also {
        it.texture.shineDamper = 10f
        it.texture.reflectivity = 0f
    }
    val entity = Entity(texturedModel, Vector3(0f, -5f, -25f), Vector3(0f), 1f)
    val light = Light(Vector3(0f, 0f, -20f), Vector3(1f, 1f, 1f))
    val camera = Camera()

    displayManager.loop {
        entity.increaseRotation(0f, 1f, 0f)
        camera.move()
        renderer.prepare()
        shader.start()
        shader.loadLight(light)
        shader.loadViewMatrix(camera)
        renderer.render(entity, shader)
        shader.stop()
    }

    shader.cleanUp()
    loader.cleanUp()
    displayManager.closeDisplay()
}