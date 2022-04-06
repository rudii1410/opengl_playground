package core.math

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils

// Source: https://github.com/ra4king/LWJGL-OpenGL-Utils/tree/master/src/com/ra4king/opengl/util/math
class Vector4 {
    private var x = 0f
    private var y = 0f
    private var z = 0f
    private var w = 0f

    constructor() {
        set(0f, 0f, 0f, 0f)
    }

    constructor(v: Float) : this(v, v, v, v) {}
    constructor(x: Float, y: Float, z: Float, w: Float) {
        set(x, y, z, w)
    }

    constructor(vec: Vector2, z: Float, w: Float) {
        set(vec, z, w)
    }

    constructor(vec: Vector3, w: Float) {
        set(vec, w)
    }

    constructor(vec: Vector4) {
        set(vec)
    }

    fun x(): Float {
        return x
    }

    fun x(x: Float): Vector4 {
        this.x = x
        return this
    }

    fun y(): Float {
        return y
    }

    fun y(y: Float): Vector4 {
        this.y = y
        return this
    }

    fun z(): Float {
        return z
    }

    fun z(z: Float): Vector4 {
        this.z = z
        return this
    }

    fun w(): Float {
        return w
    }

    fun w(w: Float): Vector4 {
        this.w = w
        return this
    }

    fun equals(v: Vector4): Boolean {
        return x == v.x && y == v.y && z == v.z && w == v.w
    }

    fun set(f: Float): Vector4 {
        return set(f, f, f, f)
    }

    operator fun set(x: Float, y: Float, z: Float, w: Float): Vector4 {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        return this
    }

    fun set2(vec: Vector2): Vector4 {
        return set(vec, 0f, 0f)
    }

    operator fun set(vec: Vector2, z: Float, w: Float): Vector4 {
        return set(vec.x(), vec.y(), z, w)
    }

    fun set3(vec: Vector3): Vector4 {
        return set(vec, 0f)
    }

    operator fun set(vec: Vector3, w: Float): Vector4 {
        return set(vec.x(), vec.y(), vec.z(), w)
    }

    fun set(vec: Vector4): Vector4 {
        set(vec.x, vec.y, vec.z, vec.w)
        return this
    }

    fun length(): Float {
        return Math.sqrt(lengthSquared().toDouble()).toFloat()
    }

    fun lengthSquared(): Float {
        return x * x + y * y + z * z + w * w
    }

    fun normalize(): Vector4 {
        val length = 1f / length()
        x *= length
        y *= length
        z *= length
        w *= length
        return this
    }

    fun dot(vec: Vector4): Float {
        return x * vec.x + y * vec.y + z * vec.z + w * vec.w
    }

    fun add(x: Float, y: Float, z: Float, w: Float): Vector4 {
        this.x += x
        this.y += y
        this.z += z
        this.w += w
        return this
    }

    fun add(vec: Vector4): Vector4 {
        return add(vec.x, vec.y, vec.z, vec.w)
    }

    fun sub(x: Float, y: Float, z: Float, w: Float): Vector4 {
        this.x -= x
        this.y -= y
        this.z -= z
        this.w -= w
        return this
    }

    fun sub(vec: Vector4): Vector4 {
        return sub(vec.x, vec.y, vec.z, vec.w)
    }

    fun mult(f: Float): Vector4 {
        return mult(f, f, f, f)
    }

    fun mult(x: Float, y: Float, z: Float, w: Float): Vector4 {
        this.x *= x
        this.y *= y
        this.z *= z
        this.w *= w
        return this
    }

    fun mult(vec: Vector4): Vector4 {
        return mult(vec.x, vec.y, vec.z, vec.w)
    }

    fun divide(f: Float): Vector4 {
        return divide(f, f, f, f)
    }

    fun divide(x: Float, y: Float, z: Float, w: Float): Vector4 {
        this.x /= x
        this.y /= y
        this.z /= z
        this.w /= w
        return this
    }

    fun divide(vec: Vector4): Vector4 {
        return divide(vec.x, vec.y, vec.z, vec.w)
    }

    fun mod(f: Float): Vector4 {
        x %= f
        y %= f
        z %= f
        w %= f
        return this
    }

    override fun toString(): String {
        return "($x, $y, $z, $w)"
    }

    fun toBuffer(): FloatBuffer {
        direct.clear()
        direct.put(x).put(y).put(z).put(w)
        direct.flip()
        return direct
    }

    companion object {
        val RIGHT = Vector4(1f, 0f, 0f, 1f)
        val LEFT = Vector4(-1f, 0f, 0f, 1f)
        val UP = Vector4(0f, 1f, 0f, 1f)
        val DOWN = Vector4(0f, -1f, 0f, 1f)
        val FORWARD = Vector4(0f, 0f, -1f, 1f)
        val BACK = Vector4(0f, 0f, 1f, 1f)
        private val direct = BufferUtils.createFloatBuffer(4)
    }
}