package objloader

import core.math.Vector2
import core.math.Vector3
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


object OBJLoader {
    fun loadObjModel(filename: String): ModelData {
        val reader = BufferedReader(FileReader(File(filename)))
        val vertices = ArrayList<Vertex>()
        val textures = ArrayList<Vector2>()
        val normals = ArrayList<Vector3>()
        val indices = ArrayList<Int>()
        var line: String?

        while (true) {
            line = reader.readLine()
            val currentLine = line.split(" ")
            if (line.startsWith("v ")) {
                vertices.add(
                    Vertex(
                        vertices.size,
                        getVector3(currentLine)
                    )
                )
            } else if (line.startsWith("vt ")) {
                textures.add(getVector2(currentLine))
            } else if (line.startsWith("vn ")) {
                normals.add(getVector3(currentLine))
            } else if (line.startsWith("f ")) {
                break
            }
        }

        while (line != null && line.startsWith("f ")) {
            val currentLine = line.split(" ")
            processVertex(currentLine[1].split("/"), vertices, indices)
            processVertex(currentLine[2].split("/"), vertices, indices)
            processVertex(currentLine[3].split("/"), vertices, indices)
            line = reader.readLine()
        }

        reader.close()
        removeUnusedVertices(vertices)

        val verticesArray = FloatArray(vertices.size * 3)
        val texturesArray = FloatArray(vertices.size * 2)
        val normalsArray = FloatArray(vertices.size * 3)
        val furthest = convertDataToArrays(
            vertices, textures, normals, verticesArray,
            texturesArray, normalsArray
        )
        val indicesArray = convertIndicesListToArray(indices)

        return ModelData(
            verticesArray, texturesArray, normalsArray, indicesArray,
            furthest
        )
    }

    private fun processVertex(vertex: List<String>, vertices: MutableList<Vertex>, indices: MutableList<Int>) {
        val index = vertex[0].toInt() - 1
        val currentVertex = vertices[index]
        val textureIndex = vertex[1].toInt() - 1
        val normalIndex = vertex[2].toInt() - 1
        if (!currentVertex.isSet) {
            currentVertex.textureIndex = textureIndex
            currentVertex.normalIndex = normalIndex
            indices.add(index)
        } else {
            dealWithAlreadyProcessedVertex(
                currentVertex, textureIndex, normalIndex, indices,
                vertices
            )
        }
    }

    private fun convertIndicesListToArray(indices: List<Int>): IntArray {
        val indicesArray = IntArray(indices.size)
        for (i in indicesArray.indices) {
            indicesArray[i] = indices[i]
        }
        return indicesArray
    }

    private fun convertDataToArrays(
        vertices: List<Vertex>, textures: List<Vector2>,
        normals: List<Vector3>, verticesArray: FloatArray, texturesArray: FloatArray,
        normalsArray: FloatArray
    ): Float {
        var furthestPoint = 0f
        for (i in vertices.indices) {
            val currentVertex = vertices[i]
            if (currentVertex.length > furthestPoint) {
                furthestPoint = currentVertex.length
            }
            val position: Vector3 = currentVertex.position
            val textureCoord: Vector2 = textures[currentVertex.textureIndex]
            val normalVector: Vector3 = normals[currentVertex.normalIndex]
            verticesArray[i * 3] = position.x()
            verticesArray[i * 3 + 1] = position.y()
            verticesArray[i * 3 + 2] = position.z()
            texturesArray[i * 2] = textureCoord.x()
            texturesArray[i * 2 + 1] = 1 - textureCoord.y()
            normalsArray[i * 3] = normalVector.x()
            normalsArray[i * 3 + 1] = normalVector.y()
            normalsArray[i * 3 + 2] = normalVector.z()
        }
        return furthestPoint
    }

    private fun dealWithAlreadyProcessedVertex(
        previousVertex: Vertex, newTextureIndex: Int,
        newNormalIndex: Int, indices: MutableList<Int>, vertices: MutableList<Vertex>
    ) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.index)
        } else {
            val anotherVertex = previousVertex.duplicateVertex
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(
                    anotherVertex, newTextureIndex, newNormalIndex,
                    indices, vertices
                )
            } else {
                val duplicateVertex = Vertex(vertices.size, previousVertex.position)
                duplicateVertex.textureIndex = newTextureIndex
                duplicateVertex.normalIndex = newNormalIndex
                previousVertex.duplicateVertex = duplicateVertex
                vertices.add(duplicateVertex)
                indices.add(duplicateVertex.index)
            }
        }
    }

    private fun removeUnusedVertices(vertices: List<Vertex>) {
        for (vertex in vertices) {
            if (!vertex.isSet) {
                vertex.textureIndex = 0
                vertex.normalIndex = 0
            }
        }
    }

    private fun getVector3(currentLine: List<String>): Vector3 {
        return Vector3(currentLine[1].toFloat(), currentLine[2].toFloat(), currentLine[3].toFloat())
    }
    private fun getVector2(currentLine: List<String>): Vector2 {
        return Vector2(currentLine[1].toFloat(), currentLine[2].toFloat())
    }
}