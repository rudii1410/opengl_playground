package simplewindow

import core.entities.Camera
import core.entities.Entity
import objloader.OBJLoader
import core.entities.Light
import core.entities.Player
import core.math.Vector3
import core.renderengine.DisplayManager
import core.renderengine.Loader
import core.renderengine.MasterRenderer
import core.terrains.Terrain
import simplewindow.model.TexturedModel
import simplewindow.texture.ModelTexture
import simplewindow.texture.TerrainTexture
import simplewindow.texture.TerrainTexturePack
import kotlin.random.Random

fun main() {
    val displayManager = DisplayManager(title = "Simple Window")
    val loader = Loader()

    val tree = generateTextureModel(loader, "lowPolyTree", "lowPolyTree")
    val fern = generateTextureModel(loader, "fern", "fern").also {
        it.texture.hasTransparency = true
    }
    val grass = generateTextureModel(loader, "grassModel", "grassTexture").also {
        it.texture.hasTransparency = true
        it.texture.useFakeLighting = true
    }

    val entityList = mutableListOf<Entity>()
    val random = Random(1)
    for (i in 0..500) {
        entityList.add(Entity(tree, generateRandomPos(random), Vector3(0f), 1f))
        entityList.add(Entity(grass, generateRandomPos(random), Vector3(0f), 1f))
        entityList.add(Entity(fern, generateRandomPos(random), Vector3(0f), 1f))
    }

    val texturePack = TerrainTexturePack(
        TerrainTexture(loader.loadTexture(getTextureFile("grass"))),
        TerrainTexture(loader.loadTexture(getTextureFile("mud"))),
        TerrainTexture(loader.loadTexture(getTextureFile("grassFlowers"))),
        TerrainTexture(loader.loadTexture(getTextureFile("path")))
    )
    val blendMap = TerrainTexture(loader.loadTexture(getTextureFile("blendMap")))
    val terrain = Terrain(0, 0, loader, texturePack, blendMap)
    val terrain2 = Terrain(1, 0, loader, texturePack, blendMap)

    val player = Player(generateTextureModel(loader, "stall", "stallTexture"), Vector3(100f, 0f, -50f), Vector3(0f), 1f)

    val light = Light(Vector3(0f, 250f, -250f), Vector3(1f, 1f, 1f))
    val camera = Camera(player)
    val renderer = MasterRenderer()

    displayManager.loop {
        camera.move()
        player.move()

        renderer.processEntity(player)
        for(entity in entityList) {
            renderer.processEntity(entity)
        }
        renderer.processTerrain(terrain)
        renderer.processTerrain(terrain2)

        renderer.render(light, camera)
    }

    renderer.cleanUp()
    loader.cleanUp()
    displayManager.closeDisplay()
}

fun generateRandomPos(random: Random): Vector3 {
    return Vector3(
        random.nextFloat() * 800 - 400,
        0f,
        random.nextFloat() * -600,
    )
}

fun getObjFile(filename: String): String {
    return "src/main/resources/$filename.obj"
}

fun getTextureFile(filename: String): String {
    return "src/main/resources/$filename.png"
}

fun generateTextureModel(loader: Loader, objFileName: String, textureFileName: String): TexturedModel {
    val data = OBJLoader.loadObjModel(getObjFile(objFileName))
    return TexturedModel(
        loader.loadToVao(data.vertices, data.textureCoords, data.normals, data.indices),
        ModelTexture(loader.loadTexture(getTextureFile(textureFileName)))
    )
}
