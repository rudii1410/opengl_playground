package core.renderengine

import core.entities.Camera
import core.entities.Entity
import core.entities.Light
import simplewindow.model.TexturedModel
import simplewindow.shader.StaticShader

class MasterRenderer {
    private val shader = StaticShader()
    private val renderer = Renderer(shader)
    private val entityList = HashMap<TexturedModel, MutableList<Entity>>()

    fun render(sun: Light, camera: Camera) {
        renderer.prepare()
        shader.start()

        shader.loadLight(sun)
        shader.loadViewMatrix(camera)
        renderer.render(entityList)

        shader.stop()
        entityList.clear()
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
    }
}