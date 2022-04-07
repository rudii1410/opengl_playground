package objloader

import core.math.Vector3

class Vertex(val index: Int, val position: Vector3) {
    var textureIndex = NO_INDEX
    var normalIndex = NO_INDEX
    var duplicateVertex: Vertex? = null
    val length: Float

    init {
        length = position.length()
    }

    val isSet: Boolean
        get() = textureIndex != NO_INDEX && normalIndex != NO_INDEX

    fun hasSameTextureAndNormal(textureIndexOther: Int, normalIndexOther: Int): Boolean {
        return textureIndexOther == textureIndex && normalIndexOther == normalIndex
    }

    companion object {
        private const val NO_INDEX = -1
    }
}