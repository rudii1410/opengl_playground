package core.math

import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

// Source: https://github.com/ra4king/LWJGL-OpenGL-Utils/tree/master/src/com/ra4king/opengl/util/math
class Vector3 {
    private var x = 0f
    private var y = 0f
    private var z = 0f

    constructor() {
        set(0f, 0f, 0f)
    }

    constructor(v: Float) {
        set(v, v, v)
    }

    constructor(x: Float, y: Float, z: Float) {
        set(x, y, z)
    }

    constructor(vec: Vector2, z: Float) {
        set(vec, z)
    }

    constructor(vec: Vector3) {
        set(vec)
    }

    fun x(): Float {
        return x
    }

    fun x(x: Float): Vector3 {
        this.x = x
        return this
    }

    fun y(): Float {
        return y
    }

    fun y(y: Float): Vector3 {
        this.y = y
        return this
    }

    fun z(): Float {
        return z
    }

    fun z(z: Float): Vector3 {
        this.z = z
        return this
    }

    fun equals(v: Vector3): Boolean {
        return x == v.x && y == v.y && z == v.z
    }

    override fun hashCode(): Int {
        return (x * (2 shl 4) + y * (2 shl 2) + z).toInt()
    }

    fun set(f: Float): Vector3 {
        return set(f, f, f)
    }

    operator fun set(x: Float, y: Float, z: Float): Vector3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    fun set2(vec: Vector2): Vector3 {
        return set(vec.x(), vec.y(), 0f)
    }

    operator fun set(vec: Vector2, z: Float): Vector3 {
        return set(vec.x(), vec.y(), z)
    }

    fun set(vec: Vector3): Vector3 {
        set(vec.x, vec.y, vec.z)
        return this
    }

    fun set4(vec: Vector4): Vector3 {
        return set(vec.x(), vec.y(), vec.z())
    }

    fun length(): Float {
        return Math.sqrt(lengthSquared().toDouble()).toFloat()
    }

    fun lengthSquared(): Float {
        return x * x + y * y + z * z
    }

    fun normalize(): Vector3 {
        val length = 1f / length()
        x *= length
        y *= length
        z *= length
        return this
    }

    fun dot(vec: Vector3): Float {
        return x * vec.x + y * vec.y + z * vec.z
    }

    fun cross(vec: Vector3, res: Vector3): Vector3 {
        return res.set(y * vec.z - vec.y * z, z * vec.x - vec.z * x, x * vec.y - vec.x * y)
    }

    fun add(x: Float, y: Float, z: Float): Vector3 {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(vec: Vector3): Vector3 {
        return add(vec.x, vec.y, vec.z)
    }

    fun sub(x: Float, y: Float, z: Float): Vector3 {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun sub(vec: Vector3): Vector3 {
        return sub(vec.x, vec.y, vec.z)
    }

    fun mult(f: Float): Vector3 {
        return mult(f, f, f)
    }

    fun mult(x: Float, y: Float, z: Float): Vector3 {
        this.x *= x
        this.y *= y
        this.z *= z
        return this
    }

    fun mult(vec: Vector3): Vector3 {
        return mult(vec.x, vec.y, vec.z)
    }

    fun divide(f: Float): Vector3 {
        return divide(f, f, f)
    }

    fun divide(x: Float, y: Float, z: Float): Vector3 {
        this.x /= x
        this.y /= y
        this.z /= z
        return this
    }

    fun divide(vec: Vector3): Vector3 {
        return divide(vec.x, vec.y, vec.z)
    }

    fun mod(f: Float): Vector3 {
        x %= f
        y %= f
        z %= f
        return this
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    fun toBuffer(): FloatBuffer {
        direct.clear()
        direct.put(x).put(y).put(z)
        direct.flip()
        return direct
    }

    companion object {
        val ZERO = Vector3(0f)
        val RIGHT = Vector3(1f, 0f, 0f)
        val LEFT = Vector3(-1f, 0f, 0f)
        val UP = Vector3(0f, 1f, 0f)
        val DOWN = Vector3(0f, -1f, 0f)
        val FORWARD = Vector3(0f, 0f, -1f)
        val BACK = Vector3(0f, 0f, 1f)
        private val direct = BufferUtils.createFloatBuffer(3)
    }
}