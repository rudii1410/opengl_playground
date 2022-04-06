package core.math

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils

// Source: https://github.com/ra4king/LWJGL-OpenGL-Utils/tree/master/src/com/ra4king/opengl/util/math
class Vector2 {
    private var x = 0f
    private var y = 0f

    constructor(v: Float) : this(v, v)
    constructor(x: Float = 0f, y: Float = 0f) {
        set(x, y)
    }
    constructor(vec: Vector2) {
        set(vec)
    }

    fun x(): Float {
        return x
    }

    fun x(x: Float): Vector2 {
        this.x = x
        return this
    }

    fun y(): Float {
        return y
    }

    fun y(y: Float): Vector2 {
        this.y = y
        return this
    }

    fun equals(v: Vector2): Boolean {
        return x == v.x && y == v.y
    }

    fun set(f: Float): Vector2 {
        return set(f, f)
    }

    operator fun set(x: Float, y: Float): Vector2 {
        this.x = x
        this.y = y
        return this
    }

    fun set(vec: Vector2): Vector2 {
        set(vec.x, vec.y)
        return this
    }

    fun set3(vec: Vector3): Vector2 {
        return set(vec.x(), vec.y())
    }

    fun set4(vec: Vector4): Vector2 {
        return set(vec.x(), vec.y())
    }

    fun length(): Float {
        return Math.sqrt(lengthSquared().toDouble()).toFloat()
    }

    fun lengthSquared(): Float {
        return x * x + y * y
    }

    fun normalize(): Vector2 {
        val length = 1f / length()
        x *= length
        y *= length
        return this
    }

    fun dot(vec: Vector2): Float {
        return x * vec.x + y * vec.y
    }

    fun add(x: Float, y: Float): Vector2 {
        this.x += x
        this.y += y
        return this
    }

    fun add(vec: Vector2): Vector2 {
        return add(vec.x, vec.y)
    }

    fun sub(x: Float, y: Float): Vector2 {
        this.x -= x
        this.y -= y
        return this
    }

    fun sub(vec: Vector2): Vector2 {
        return sub(vec.x, vec.y)
    }

    fun mult(f: Float): Vector2 {
        return mult(f, f)
    }

    fun mult(x: Float, y: Float): Vector2 {
        this.x *= x
        this.y *= y
        return this
    }

    fun mult(vec: Vector2): Vector2 {
        return mult(vec.x, vec.y)
    }

    fun divide(f: Float): Vector2 {
        return divide(f, f)
    }

    fun divide(x: Float, y: Float): Vector2 {
        this.x /= x
        this.y /= y
        return this
    }

    fun divide(vec: Vector2): Vector2 {
        return divide(vec.x, vec.y)
    }

    fun mod(f: Float): Vector2 {
        x %= f
        y %= f
        return this
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    fun toBuffer(): FloatBuffer {
        direct.clear()
        direct.put(x).put(y)
        direct.flip()
        return direct
    }

    companion object {
        val RIGHT = Vector2(1f, 0f)
        val LEFT = Vector2(-1f, 0f)
        val UP = Vector2(0f, 1f)
        val DOWN = Vector2(0f, -1f)
        private val direct = BufferUtils.createFloatBuffer(2)
    }
}