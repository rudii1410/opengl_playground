package core

import core.math.Vector2
import core.math.Vector3
import core.renderengine.Loader
import core.renderengine.RawModel
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

object OBJLoader {
    fun loadObjModel(filename: String, loader: Loader): RawModel {
        val reader = BufferedReader(FileReader(File(filename)))
        val vertices = ArrayList<Vector3>()
        val textures = ArrayList<Vector2>()
        val normals = ArrayList<Vector3>()
        val indices = ArrayList<Int>()
        val normalsArray: FloatArray
        val texturesArray: FloatArray
        var line: String? = null

        while (true) {
            line = reader.readLine()
            val currentLine = line.split(" ")
            if (line.startsWith("v ")) {
                vertices.add(getVector3(currentLine))
            } else if (line.startsWith("vt ")) {
                textures.add(getVector2(currentLine))
            } else if (line.startsWith("vn ")) {
                normals.add(getVector3(currentLine))
            } else if (line.startsWith("f ")) {
                texturesArray = FloatArray(vertices.size * 2)
                normalsArray = FloatArray(vertices.size * 3)
                break
            }
        }

        while (line != null) {
            if (!line.startsWith("f ")) {
                line = reader.readLine()
                continue
            }
            val currentLine = line.split(" ")
            val vertex1 = currentLine[1].split("/")
            val vertex2 = currentLine[2].split("/")
            val vertex3 = currentLine[3].split("/")
            processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray)
            processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray)
            processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray)
            line = reader.readLine()
        }

        reader.close()

        val verticesArray = FloatArray(vertices.size * 3)
        val indicesArray = IntArray(indices.size)

        var vertexPointer = 0
        for (vertex in vertices) {
            verticesArray[vertexPointer++] = vertex.x()
            verticesArray[vertexPointer++] = vertex.y()
            verticesArray[vertexPointer++] = vertex.z()
        }
        indices.forEachIndexed { idx, element ->
            indicesArray[idx] = element
        }

        return loader.loadToVao(verticesArray, texturesArray, indicesArray)
    }

    private fun processVertex(
        vertexData: List<String>,
        indices: ArrayList<Int>,
        textures: ArrayList<Vector2>,
        normals: ArrayList<Vector3>,
        texturesArray: FloatArray,
        normalsArray: FloatArray
    ) {
        val currentVertexPointer = vertexData[0].toInt() - 1
        indices.add(currentVertexPointer)
        textures[vertexData[1].toInt() - 1].let {
            texturesArray[currentVertexPointer * 2] = it.x()
            texturesArray[currentVertexPointer * 2 + 1] = it.y()
        }
        normals[vertexData[2].toInt() - 1].let {
            normalsArray[currentVertexPointer * 3] = it.x()
            normalsArray[currentVertexPointer * 3 + 1] = it.y()
            normalsArray[currentVertexPointer * 3 + 2] = it.z()
        }
    }

    private fun getVector3(currentLine: List<String>): Vector3 {
        return Vector3(currentLine[1].toFloat(), currentLine[2].toFloat(), currentLine[3].toFloat())
    }
    private fun getVector2(currentLine: List<String>): Vector2 {
        return Vector2(currentLine[1].toFloat(), currentLine[2].toFloat())
    }
}