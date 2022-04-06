package core.math

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// Source: https://github.com/ra4king/LWJGL-OpenGL-Utils/tree/master/src/com/ra4king/opengl/util/math
class Quaternion {
    private var x = 0f
    private var y = 0f
    private var z = 0f
    private var w = 0f

    constructor() {
        reset()
    }

    constructor(x: Float, y: Float, z: Float, w: Float) {
        set(x, y, z, w)
    }

    constructor(angle: Float, vec: Vector3) {
        val s = sin((angle / 2).toDouble()).toFloat()
        x = vec.x() * s
        y = vec.y() * s
        z = vec.z() * s
        w = cos((angle / 2).toDouble()).toFloat()
    }

    constructor(q: Quaternion) {
        set(q)
    }

    fun x(): Float {
        return x
    }

    fun x(x: Float): Quaternion {
        this.x = x
        return this
    }

    fun y(): Float {
        return y
    }

    fun y(y: Float): Quaternion {
        this.y = y
        return this
    }

    fun z(): Float {
        return z
    }

    fun z(z: Float): Quaternion {
        this.z = z
        return this
    }

    fun w(): Float {
        return w
    }

    fun w(w: Float): Quaternion {
        this.w = w
        return this
    }

    operator fun set(x: Float, y: Float, z: Float, w: Float): Quaternion {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        return this
    }

    fun set(q: Quaternion): Quaternion {
        return set(q.x, q.y, q.z, q.w)
    }

    fun reset(): Quaternion {
        x = 0f
        y = 0f
        z = 0f
        w = 1f
        return this
    }

    fun length(): Float {
        return sqrt((x * x + y * y + z * z + w * w).toDouble()).toFloat()
    }

    fun normalize(): Quaternion {
        val length = 1f / length()
        x *= length
        y *= length
        z *= length
        w *= length
        return this
    }

    fun dot(q: Quaternion): Float {
        return x * q.x + y * q.y + z * q.z + w * q.w
    }

    fun mult3(v: Vector3?, result: Vector3): Vector3 {
        val quatVector = Vector3(x, y, z)
        val uv = quatVector.cross(v!!, Vector3())
        val uuv = quatVector.cross(uv, Vector3())
        uv.mult(w * 2)
        uuv.mult(2f)
        return result.set(v).add(uv).add(uuv)
    }

    fun mult(q: Quaternion): Quaternion {
        val xx = w * q.x + x * q.w + y * q.z - z * q.y
        val yy = w * q.y + y * q.w + z * q.x - x * q.z
        val zz = w * q.z + z * q.w + x * q.y - y * q.x
        val ww = w * q.w - x * q.x - y * q.y - z * q.z
        x = xx
        y = yy
        z = zz
        w = ww
        return this
    }

    fun conjugate(): Quaternion {
        x *= -1f
        y *= -1f
        z *= -1f
        return this
    }

    fun inverse(): Quaternion {
        return normalize().conjugate()
    }

    fun toMatrix(mat4: Matrix4): Matrix4 {
        return mat4.set(
            floatArrayOf(
                1 - 2 * y * y - 2 * z * z,
                2 * x * y + 2 * w * z,
                2 * x * z - 2 * w * y, 0f,
                2 * x * y - 2 * w * z,
                1 - 2 * x * x - 2 * z * z,
                2 * y * z + 2 * w * x, 0f,
                2 * x * z + 2 * w * y,
                2 * y * z - 2 * w * x,
                1 - 2 * x * x - 2 * y * y, 0f, 0f, 0f, 0f, 1f
            )
        )
    }
}