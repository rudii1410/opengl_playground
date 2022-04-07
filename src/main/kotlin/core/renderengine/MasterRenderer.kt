package core.renderengine

import core.entities.Camera
import core.entities.Entity
import core.entities.Light
import core.math.Matrix4
import core.terains.Terrain
import org.lwjgl.opengl.GL11
import simplewindow.model.TexturedModel
import simplewindow.shader.static.StaticShader
import simplewindow.shader.terrain.TerrainShader
import kotlin.math.tan

class MasterRenderer {
    private val shader: StaticShader = StaticShader()
    private val projectionMatrix: Matrix4
    private val entityRenderer: EntityRenderer
    private val entityList = HashMap<TexturedModel, MutableList<Entity>>()
    private val terrainShader = TerrainShader()
    private val terrainRenderer: TerrainRenderer
    private val terrainList = ArrayList<Terrain>()

    constructor() {
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_BACK)
        projectionMatrix = createProjectionMatrix()
        entityRenderer = EntityRenderer(shader, projectionMatrix)
        terrainRenderer = TerrainRenderer(terrainShader, projectionMatrix)
    }

    fun render(sun: Light, camera: Camera) {
        prepare()

        shader.start()
        shader.loadLight(sun)
        shader.loadViewMatrix(camera)
        entityRenderer.render(entityList)
        shader.stop()

        terrainShader.start()
        terrainShader.loadLight(sun)
        terrainShader.loadViewMatrix(camera)
        terrainRenderer.render(terrainList)
        terrainShader.stop()

        entityList.clear()
        terrainList.clear()
    }

    fun processTerrain(terrain: Terrain) {
        terrainList.add(terrain)
    }

    fun processEntity(entity: Entity) {
        val batch = entityList[entity.model]
        if (batch != null) {
            batch.add(entity)
        } else {
            entityList[entity.model] = arrayListOf(entity)
        }
    }

    fun cleanUp() {
        shader.cleanUp()
        terrainShader.cleanUp()
    }

    private fun prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glClearColor(1f, 1f, 0.5f, 1f)
    }

    private fun createProjectionMatrix(): Matrix4 {
        val aspectRatio = 1280f / 720f
        val yScale = (1f / tan(Math.toRadians((FOV / 2f).toDouble()))) * aspectRatio
        val xScale = yScale / aspectRatio
        val frustumLength = FAR_PLANE - NEAR_PLANE

        return Matrix4().also {
            it.put(0, 0, xScale.toFloat())
            it.put(1, 1, yScale.toFloat())
            it.put(2, 2, -((FAR_PLANE + NEAR_PLANE) / frustumLength))
            it.put(2, 3, -1f)
            it.put(3, 2, -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength))
            it.put(3, 3, 0f)
        }
    }

    companion object {
        private const val FOV = 70f
        private const val NEAR_PLANE = 0.1f
        private const val FAR_PLANE = 1000f
    }
}