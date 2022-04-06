package core.math

import java.util.ArrayList

// Source: https://github.com/ra4king/LWJGL-OpenGL-Utils/tree/master/src/com/ra4king/opengl/util/math
class MatrixStack {
    private val stack: ArrayList<Matrix4> = ArrayList<Matrix4>()
    private var currIdx = 0

    init {
        clear()
    }

    fun clear(): MatrixStack {
        stack.clear()
        stack.add(Matrix4())
        currIdx = 0
        return this
    }

    val top: Matrix4
        get() = stack[currIdx]

    fun setTop(m: Matrix4): MatrixStack {
        stack[currIdx].set(m)
        return this
    }

    fun pushMatrix(): MatrixStack {
        stack.add(Matrix4(top))
        currIdx++
        return this
    }

    fun popMatrix(): MatrixStack {
        check(currIdx != 0) { "Already at the bottom of the stack." }
        stack.removeAt(currIdx)
        currIdx--
        return this
    }
}