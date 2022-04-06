package core.math

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils

// Source: https://github.com/ra4king/LWJGL-OpenGL-Utils/tree/master/src/com/ra4king/opengl/util/math
class Matrix4() {
    private val matrix = FloatArray(LENGTH)

    constructor(m: FloatArray) : this() {
        set(m)
    }

    constructor(m: Matrix4) : this() {
        set(m)
    }

    fun clear(): Matrix4 {
        for (a in 0 until LENGTH) {
            matrix[a] = 0f
        }
        return this
    }

    fun clearToIdentity(): Matrix4 {
        return clear().put(0, 1f).put(5, 1f).put(10, 1f).put(15, 1f)
    }

    fun clearToOrtho(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4 {
        return clear().put(0, 2 / (right - left))
            .put(5, 2 / (top - bottom))
            .put(10, -2 / (far - near))
            .put(12, -(right + left) / (right - left))
            .put(13, -(top + bottom) / (top - bottom))
            .put(14, -(far + near) / (far - near))
            .put(15, 1f)
    }

    fun clearToPerspective(fovRad: Float, width: Float, height: Float, near: Float, far: Float): Matrix4 {
        val fov = 1 / Math.tan((fovRad / 2).toDouble()).toFloat()
        return clear().put(0, fov * (height / width))
            .put(5, fov)
            .put(10, (far + near) / (near - far))
            .put(14, 2 * far * near / (near - far))
            .put(11, -1f)
    }

    fun clearToPerspectiveDeg(fov: Float, width: Float, height: Float, near: Float, far: Float): Matrix4 {
        return clearToPerspective(Math.toRadians(fov.toDouble()).toFloat(), width, height, near, far)
    }

    operator fun get(index: Int): Float {
        return matrix[index]
    }

    operator fun get(col: Int, row: Int): Float {
        return matrix[col * 4 + row]
    }

    fun getColumn(index: Int, result: Vector4): Vector4 {
        return result.set(get(index, 0), get(index, 1), get(index, 2), get(index, 3))
    }

    fun put(index: Int, f: Float): Matrix4 {
        matrix[index] = f
        return this
    }

    fun put(col: Int, row: Int, f: Float): Matrix4 {
        matrix[col * 4 + row] = f
        return this
    }

    fun putColumn(index: Int, v: Vector4): Matrix4 {
        put(index, 0, v.x())
        put(index, 1, v.y())
        put(index, 2, v.z())
        put(index, 3, v.z())
        return this
    }

    fun putColumn3(index: Int, v: Vector3): Matrix4 {
        put(index, 0, v.x())
        put(index, 1, v.y())
        put(index, 2, v.z())
        return this
    }

    fun putColumn(index: Int, v: Vector3, w: Float): Matrix4 {
        put(index, 0, v.x())
        put(index, 1, v.y())
        put(index, 2, v.z())
        put(index, 3, w)
        return this
    }

    fun set(m: FloatArray): Matrix4 {
        require(m.size >= LENGTH) { "float array must have at least $LENGTH values." }
        var a = 0
        while (a < m.size && a < LENGTH) {
            matrix[a] = m[a]
            a++
        }
        return this
    }

    fun set(m: Matrix4): Matrix4 {
        set(m.matrix)
        return this
    }

    fun set3x3(m: Matrix3): Matrix4 {
        for (a in 0..2) {
            put(a, 0, m.get(a, 0))
            put(a, 1, m.get(a, 1))
            put(a, 2, m.get(a, 2))
        }
        return this
    }

    fun mult(f: Float): Matrix4 {
        for (a in 0 until LENGTH) {
            put(a, get(a) * f)
        }
        return this
    }

    fun mult(m: FloatArray): Matrix4 {
        require(m.size >= LENGTH) { "float array must have at least $LENGTH values." }
        return mult(Matrix4(m))
    }

    fun mult(m: Matrix4): Matrix4 {
        val temp = Matrix4()
        for (a in 0..3) {
            temp.put(a, 0, get(0) * m[a, 0] + get(4) * m[a, 1] + get(8) * m[a, 2] + get(12) * m[a, 3])
            temp.put(a, 1, get(1) * m[a, 0] + get(5) * m[a, 1] + get(9) * m[a, 2] + get(13) * m[a, 3])
            temp.put(a, 2, get(2) * m[a, 0] + get(6) * m[a, 1] + get(10) * m[a, 2] + get(14) * m[a, 3])
            temp.put(a, 3, get(3) * m[a, 0] + get(7) * m[a, 1] + get(11) * m[a, 2] + get(15) * m[a, 3])
        }
        return set(temp)
    }

    fun mult3(vec: Vector3?, w: Float, result: Vector3): Vector3 {
        return result.set4(mult4(Vector4(vec!!, w), Vector4()))
    }

    fun mult4(vec: Vector4, result: Vector4): Vector4 {
        return result.set(
            matrix[0] * vec.x() + matrix[4] * vec.y() + matrix[8] * vec.z() + matrix[12] * vec.w(),
            matrix[1] * vec.x() + matrix[5] * vec.y() + matrix[9] * vec.z() + matrix[13] * vec.w(),
            matrix[2] * vec.x() + matrix[6] * vec.y() + matrix[10] * vec.z() + matrix[14] * vec.w(),
            matrix[3] * vec.x() + matrix[7] * vec.y() + matrix[11] * vec.z() + matrix[15] * vec.w()
        )
    }

    fun transpose(): Matrix4 {
        var old = get(1)
        put(1, get(4))
        put(4, old)
        old = get(2)
        put(2, get(8))
        put(8, old)
        old = get(3)
        put(3, get(12))
        put(12, old)
        old = get(7)
        put(7, get(13))
        put(13, old)
        old = get(11)
        put(11, get(14))
        put(14, old)
        old = get(6)
        put(6, get(9))
        put(9, old)
        return this
    }

    fun translate(x: Float, y: Float, z: Float): Matrix4 {
        val temp = Matrix4()
        temp.put(0, 1f)
        temp.put(5, 1f)
        temp.put(10, 1f)
        temp.put(15, 1f)
        temp.put(12, x)
        temp.put(13, y)
        temp.put(14, z)
        return mult(temp)
    }

    fun translate(vec: Vector3): Matrix4 {
        return translate(vec.x(), vec.y(), vec.z())
    }

    fun scale(f: Float): Matrix4 {
        return scale(f, f, f)
    }

    fun scale(x: Float, y: Float, z: Float): Matrix4 {
        val temp = Matrix4()
        temp.put(0, x)
        temp.put(5, y)
        temp.put(10, z)
        temp.put(15, 1f)
        return mult(temp)
    }

    fun scale(vec: Vector3): Matrix4 {
        return scale(vec.x(), vec.y(), vec.z())
    }

    fun rotate(angle: Float, x: Float, y: Float, z: Float): Matrix4 {
        var x = x
        var y = y
        var z = z
        val cos = Math.cos(angle.toDouble()).toFloat()
        val sin = Math.sin(angle.toDouble()).toFloat()
        val oneMinusCos = 1 - cos
        val len = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        x /= len
        y /= len
        z /= len
        val temp = Matrix4()
        temp.put(0, x * x * oneMinusCos + cos)
        temp.put(4, x * y * oneMinusCos - z * sin)
        temp.put(8, x * z * oneMinusCos + y * sin)
        temp.put(1, y * x * oneMinusCos + z * sin)
        temp.put(5, y * y * oneMinusCos + cos)
        temp.put(9, y * z * oneMinusCos - x * sin)
        temp.put(2, z * x * oneMinusCos - y * sin)
        temp.put(6, z * y * oneMinusCos + x * sin)
        temp.put(10, z * z * oneMinusCos + cos)
        temp.put(15, 1f)
        return mult(temp)
    }

    fun rotate(angle: Float, vec: Vector3): Matrix4 {
        return rotate(angle, vec.x(), vec.y(), vec.z())
    }

    fun rotateDeg(angle: Float, x: Float, y: Float, z: Float): Matrix4 {
        return rotate(Math.toRadians(angle.toDouble()).toFloat(), x, y, z)
    }

    fun rotateDeg(angle: Float, vec: Vector3): Matrix4 {
        return rotate(Math.toRadians(angle.toDouble()).toFloat(), vec)
    }

    fun determinant(): Float {
        val a =
            get(5) * get(10) * get(15) + get(9) * get(14) * get(7) + get(13) * get(6) * get(11) - get(7) * get(10) * get(
                13
            ) - get(11) * get(14) * get(5) - get(15) * get(6) * get(9)
        val b =
            get(1) * get(10) * get(15) + get(9) * get(14) * get(3) + get(13) * get(2) * get(11) - get(3) * get(10) * get(
                13
            ) - get(11) * get(14) * get(1) - get(15) * get(2) * get(9)
        val c =
            get(1) * get(6) * get(15) + get(5) * get(14) * get(3) + get(13) * get(2) * get(7) - get(3) * get(6) * get(13) - get(
                7
            ) * get(14) * get(1) - get(15) * get(2) * get(5)
        val d =
            get(1) * get(6) * get(11) + get(5) * get(10) * get(3) + get(9) * get(2) * get(7) - get(3) * get(6) * get(9) - get(
                7
            ) * get(10) * get(1) - get(11) * get(2) * get(5)
        return get(0) * a - get(4) * b + get(8) * c - get(12) * d
    }

    fun inverse(): Matrix4 {
        val inv = Matrix4()
        inv.put(
            0,
            +(get(5) * get(10) * get(15) + get(9) * get(14) * get(7) + get(13) * get(6) * get(11) - get(7) * get(10) * get(
                13
            ) - get(11) * get(14) * get(5) - get(15) * get(6) * get(9))
        )
        inv.put(
            1,
            -(get(4) * get(10) * get(15) + get(8) * get(14) * get(7) + get(12) * get(6) * get(11) - get(7) * get(10) * get(
                12
            ) - get(11) * get(14) * get(4) - get(15) * get(6) * get(8))
        )
        inv.put(
            2,
            +(get(4) * get(9) * get(15) + get(8) * get(13) * get(7) + get(12) * get(5) * get(11) - get(7) * get(9) * get(
                12
            ) - get(11) * get(13) * get(4) - get(15) * get(5) * get(8))
        )
        inv.put(
            3,
            -(get(4) * get(9) * get(14) + get(8) * get(13) * get(6) + get(12) * get(5) * get(10) - get(6) * get(9) * get(
                12
            ) - get(10) * get(13) * get(4) - get(14) * get(5) * get(8))
        )
        inv.put(
            4,
            -(get(1) * get(10) * get(15) + get(9) * get(14) * get(3) + get(13) * get(2) * get(11) - get(3) * get(10) * get(
                13
            ) - get(11) * get(14) * get(1) - get(15) * get(2) * get(9))
        )
        inv.put(
            5,
            +(get(0) * get(10) * get(15) + get(8) * get(14) * get(3) + get(12) * get(2) * get(11) - get(3) * get(10) * get(
                12
            ) - get(11) * get(14) * get(0) - get(15) * get(2) * get(8))
        )
        inv.put(
            6,
            -(get(0) * get(9) * get(15) + get(8) * get(13) * get(3) + get(12) * get(1) * get(11) - get(3) * get(9) * get(
                12
            ) - get(11) * get(13) * get(0) - get(15) * get(1) * get(8))
        )
        inv.put(
            7,
            +(get(0) * get(9) * get(14) + get(8) * get(13) * get(2) + get(12) * get(1) * get(10) - get(2) * get(9) * get(
                12
            ) - get(10) * get(13) * get(0) - get(14) * get(1) * get(8))
        )
        inv.put(
            8,
            +(get(1) * get(6) * get(15) + get(5) * get(14) * get(3) + get(13) * get(2) * get(7) - get(3) * get(6) * get(
                13
            ) - get(7) * get(14) * get(1) - get(15) * get(2) * get(5))
        )
        inv.put(
            9,
            -(get(0) * get(6) * get(15) + get(4) * get(14) * get(3) + get(12) * get(2) * get(7) - get(3) * get(6) * get(
                12
            ) - get(7) * get(14) * get(0) - get(15) * get(2) * get(4))
        )
        inv.put(
            10,
            +(get(0) * get(5) * get(15) + get(4) * get(13) * get(3) + get(12) * get(1) * get(7) - get(3) * get(5) * get(
                12
            ) - get(7) * get(13) * get(0) - get(15) * get(1) * get(4))
        )
        inv.put(
            11,
            -(get(0) * get(5) * get(14) + get(4) * get(13) * get(2) + get(12) * get(1) * get(6) - get(2) * get(5) * get(
                12
            ) - get(6) * get(13) * get(0) - get(14) * get(1) * get(4))
        )
        inv.put(
            12,
            -(get(1) * get(6) * get(11) + get(5) * get(10) * get(3) + get(9) * get(2) * get(7) - get(3) * get(6) * get(9) - get(
                7
            ) * get(10) * get(1) - get(11) * get(2) * get(5))
        )
        inv.put(
            13,
            +(get(0) * get(6) * get(11) + get(4) * get(10) * get(3) + get(8) * get(2) * get(7) - get(3) * get(6) * get(8) - get(
                7
            ) * get(10) * get(0) - get(11) * get(2) * get(4))
        )
        inv.put(
            14,
            -(get(0) * get(5) * get(11) + get(4) * get(9) * get(3) + get(8) * get(1) * get(7) - get(3) * get(5) * get(8) - get(
                7
            ) * get(9) * get(0) - get(11) * get(1) * get(4))
        )
        inv.put(
            15,
            +(get(0) * get(5) * get(10) + get(4) * get(9) * get(2) + get(8) * get(1) * get(6) - get(2) * get(5) * get(8) - get(
                6
            ) * get(9) * get(0) - get(10) * get(1) * get(4))
        )
        return set(inv.transpose().mult(1 / determinant()))
    }

    fun toQuaternion(res: Quaternion): Quaternion {
        val x = get(0) - get(5) - get(10)
        val y = get(5) - get(0) - get(10)
        val z = get(10) - get(0) - get(5)
        val w = get(0) + get(5) + get(10)
        var biggestIndex = 0
        var biggest = w
        if (x > biggest) {
            biggest = x
            biggestIndex = 1
        }
        if (y > biggest) {
            biggest = y
            biggestIndex = 2
        }
        if (z > biggest) {
            biggest = z
            biggestIndex = 3
        }
        val biggestVal = (Math.sqrt((biggest + 1).toDouble()) * 0.5).toFloat()
        val mult = 0.25f / biggestVal
        when (biggestIndex) {
            0 -> {
                res.w(biggestVal)
                res.x((get(6) - get(9)) * mult)
                res.y((get(8) - get(2)) * mult)
                res.z((get(1) - get(4)) * mult)
            }
            1 -> {
                res.w((get(6) - get(9)) * mult)
                res.x(biggestVal)
                res.y((get(1) + get(4)) * mult)
                res.z((get(8) + get(2)) * mult)
            }
            2 -> {
                res.w((get(8) - get(2)) * mult)
                res.x((get(1) + get(4)) * mult)
                res.y(biggestVal)
                res.z((get(6) + get(9)) * mult)
            }
            3 -> {
                res.w((get(1) - get(4)) * mult)
                res.x((get(8) + get(2)) * mult)
                res.y((get(6) + get(9)) * mult)
                res.z(biggestVal)
            }
        }
        return res
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
        const val LENGTH = 16
        private val direct = BufferUtils.createFloatBuffer(16)
    }
}