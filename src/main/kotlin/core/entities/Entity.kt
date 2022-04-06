package core.entities

import core.math.Vector3
import simplewindow.model.TexturedModel

class Entity(
    val model: TexturedModel,
    val position: Vector3,
    val rotation: Vector3,
    val scale: Float
) {
    fun increasePosition(dx: Float, dy: Float, dz: Float) {
        this.position.add(dx, dy, dz)
    }

    fun increaseRotation(dx: Float, dy: Float, dz: Float) {
        this.rotation.add(dx, dy, dz)
    }
}