package simplewindow

import core.entities.Camera
import core.entities.Entity
import core.OBJLoader
import core.entities.Light
import core.math.Vector3
import core.renderengine.DisplayManager
import core.renderengine.Loader
import core.renderengine.MasterRenderer
import core.terains.Terrain
import simplewindow.model.TexturedModel
import simplewindow.texture.ModelTexture
import kotlin.random.Random

fun main() {
    val displayManager = DisplayManager(title = "Simple Window")
    val loader = Loader()

    val texturedModel = TexturedModel(
        OBJLoader.loadObjModel("src/main/resources/stall.obj", loader),
        ModelTexture(loader.loadTexture("src/main/resources/stallTexture.png"))
    ).also {
        it.texture.shineDamper = 10f
        it.texture.reflectivity = 0f
    }
    val entityList = mutableListOf<Entity>()
    val random = Random(1)
    for (i in 0..1) {
        val pos = Vector3(
            random.nextFloat() * 500,
            random.nextFloat() * 500,
            random.nextFloat() * -1000,
        )
        val rot = Vector3(
            random.nextFloat() * 180f,
            random.nextFloat() * 180f,
            0f,
        )
        entityList.add(Entity(texturedModel, pos, rot, 1f))
    }

    val terrain = Terrain(0, 0, loader, ModelTexture(loader.loadTexture("src/main/resources/grass.png")))
    val terrain1 = Terrain(1, 0, loader, ModelTexture(loader.loadTexture("src/main/resources/grass.png")))

    val light = Light(Vector3(0f, 0f, -20f), Vector3(1f, 1f, 1f))
    val camera = Camera().also {
        it.position = Vector3(0f, 0.5f, 0f)
        it.yaw = Math.toRadians(180.0).toFloat()
    }
    val renderer = MasterRenderer()

    displayManager.loop {
        camera.move()

        for(entity in entityList) {
            renderer.processEntity(entity)
        }
        renderer.processTerrain(terrain)
        renderer.processTerrain(terrain1)

        renderer.render(light, camera)
    }

    renderer.cleanUp()
    loader.cleanUp()
    displayManager.closeDisplay()
}