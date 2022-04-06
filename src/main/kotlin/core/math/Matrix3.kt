package core.math

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils

// Source: https://github.com/ra4king/LWJGL-OpenGL-Utils/tree/master/src/com/ra4king/opengl/util/math
class Matrix3() {
    private val matrix = FloatArray(LENGTH)

    constructor(m: FloatArray) : this() {
        set(m)
    }

    constructor(m: Matrix3) : this() {
        set(m)
    }

    fun clear(): Matrix3 {
        for (a in 0 until LENGTH) {
            matrix[a] = 0f
        }
        return this
    }

    fun clearToIdentity(): Matrix3 {
        return clear().put(0, 1f).put(4, 1f).put(8, 1f)
    }

    operator fun get(index: Int): Float {
        return matrix[index]
    }

    operator fun get(col: Int, row: Int): Float {
        return matrix[col * 3 + row]
    }

    fun put(index: Int, f: Float): Matrix3 {
        matrix[index] = f
        return this
    }

    fun put(col: Int, row: Int, f: Float): Matrix3 {
        matrix[col * 3 + row] = f
        return this
    }

    fun putColumn(index: Int, v: Vector3): Matrix3 {
        put(index, 0, v.x())
        put(index, 1, v.y())
        put(index, 2, v.z())
        return this
    }

    fun set(m: FloatArray): Matrix3 {
        require(m.size >= LENGTH) { "float array must have at least $LENGTH values." }
        var a = 0
        while (a < m.size && a < LENGTH) {
            matrix[a] = m[a]
            a++
        }
        return this
    }

    fun set(m: Matrix3): Matrix3 {
        set(m.matrix)
        return this
    }

    fun set4x4(m: Matrix4): Matrix3 {
        for (a in 0..2) {
            put(a, 0, m[a, 0])
            put(a, 1, m[a, 1])
            put(a, 2, m[a, 2])
        }
        return this
    }

    fun mult(f: Float): Matrix3 {
        for (a in 0 until LENGTH) {
            put(a, get(a) * f)
        }
        return this
    }

    fun mult(m: FloatArray): Matrix3 {
        require(m.size >= LENGTH) { "float array must have at least $LENGTH values." }
        return mult(Matrix3(m))
    }

    fun mult(m: Matrix3): Matrix3 {
        val temp = Matrix3()
        for (a in 0..2) {
            temp.put(a, 0, get(0) * m[a, 0] + get(3) * m[a, 1] + get(6) * m[a, 2])
            temp.put(a, 1, get(1) * m[a, 0] + get(4) * m[a, 1] + get(7) * m[a, 2])
            temp.put(a, 2, get(2) * m[a, 0] + get(5) * m[a, 1] + get(8) * m[a, 2])
        }
        set(temp)
        return this
    }

    fun mult3(vec: Vector3, result: Vector3): Vector3 {
        return result.set(
            get(0) * vec.x() + get(3) * vec.y() + get(6) * vec.z(),
            get(1) * vec.x() + get(4) * vec.y() + get(7) * vec.z(),
            get(2) * vec.x() + get(5) * vec.y() + get(8) * vec.z()
        )
    }

    fun transpose(): Matrix3 {
        var old = get(1)
        put(1, get(3))
        put(3, old)
        old = get(2)
        put(2, get(6))
        put(6, old)
        old = get(5)
        put(5, get(7))
        put(7, old)
        return this
    }

    fun determinant(): Float {
        return +get(0) * get(4) * get(8) + get(3) * get(7) * get(2) + get(6) * get(1) * get(5) - get(2) * get(4) * get(6) - get(
            5
        ) * get(7) * get(0) - get(8) * get(1) * get(3)
    }

    fun inverse(): Matrix3 {
        val inv = Matrix3()
        inv.put(0, +(get(4) * get(8) - get(5) * get(7)))
        inv.put(1, -(get(3) * get(8) - get(5) * get(6)))
        inv.put(2, +(get(3) * get(7) - get(4) * get(6)))
        inv.put(3, -(get(1) * get(8) - get(2) * get(7)))
        inv.put(4, +(get(0) * get(8) - get(2) * get(6)))
        inv.put(5, -(get(0) * get(7) - get(1) * get(6)))
        inv.put(6, +(get(1) * get(5) - get(2) * get(4)))
        inv.put(7, -(get(0) * get(5) - get(2) * get(3)))
        inv.put(8, +(get(0) * get(4) - get(1) * get(3)))
        return set(inv.transpose().mult(1 / determinant()))
    }

    init {
        clearToIdentity()
    }

    fun toBuffer(): FloatBuffer {
        direct.clear()
        for (a in 0 until LENGTH) {
            direct.put(matrix[a])
        }
        direct.flip()
        return direct
    }

    companion object {
        const val LENGTH = 9
        private val direct = BufferUtils.createFloatBuffer(LENGTH)
    }
}