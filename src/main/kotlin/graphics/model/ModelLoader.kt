package graphics.model

import org.lwjgl.assimp.Assimp.*
import org.lwjgl.assimp.*
import util.File
import math.Color
import org.lwjgl.BufferUtils
import math.matrices.Matrix4
import graphics.model.mesh.MeshLoader
import graphics.model.Model
import graphics.model.Shape
import graphics.model.material.ColoredMaterial
import graphics.model.material.Material

class ModelLoader {

    private val meshLoader = MeshLoader()

    companion object {
        private const val DEFAULT_SPECULAR_VALUE = 50.0f
    }

    fun load(path: String): Model {
        val scene = loadScene(path)
        val root = scene.mRootNode() ?: throw Exception("Scene does not contain root node")
        val shapes = parseShapes(scene, root)

        return Model(shapes)
    }

    private fun loadScene(path: String): AIScene {
        return aiImportFile(
            File(path).getPath(),
            Assimp.aiProcess_Triangulate or Assimp.aiProcess_OptimizeGraph or Assimp.aiProcess_RemoveRedundantMaterials
        ) ?: throw Exception("Could not load scene: $path")
    }

    private fun parseShapes(scene: AIScene, node: AINode): List<Shape> {
        val materials = ArrayList<Material>()
        val aiMaterials = scene.mMaterials()
        for (i in 0 until scene.mNumMaterials()) {
            materials += parseMaterial(AIMaterial.create(aiMaterials!!.get(i)))
        }

        val shapes = ArrayList<Shape>(scene.mNumMeshes())
        val aiMeshes = scene.mMeshes()
        for (i in 0 until scene.mNumMeshes()) {
            val aiTransformation = node.mTransformation()
            val aiMesh = AIMesh.create(aiMeshes!!.get(i))

            val material = materials[aiMesh.mMaterialIndex()]
            val transformation = parseMatrix(aiTransformation)
            shapes += Shape(meshLoader.parseData(aiMesh, transformation), material)
        }

        return shapes
    }

    private fun parseMatrix(aiMatrix: AIMatrix4x4): Matrix4 {
        return Matrix4(
            floatArrayOf(
                aiMatrix.a1(), aiMatrix.a2(), aiMatrix.a3(), aiMatrix.a4(),
                aiMatrix.b1(), aiMatrix.b2(), aiMatrix.b3(), aiMatrix.b4(),
                aiMatrix.c1(), aiMatrix.c2(), aiMatrix.c3(), aiMatrix.c4(),
                aiMatrix.d1(), aiMatrix.d2(), aiMatrix.d3(), aiMatrix.d4()
            )
        )
    }

    private fun parseMaterial(material: AIMaterial) = ColoredMaterial(
        getColor(material, AI_MATKEY_COLOR_DIFFUSE),
        getColor(material, AI_MATKEY_COLOR_SPECULAR),
        getFloat(material, AI_MATKEY_SHININESS)
    )

    private fun getColor(material: AIMaterial, key: String): Color {
        val aiColor = AIColor4D.create()
        val result = aiGetMaterialColor(material, key, aiTextureType_NONE, 0, aiColor)
        return if (result == 0) {
            Color(aiColor.r(), aiColor.g(), aiColor.b(), aiColor.a())
        } else {
            Color()
        }
    }

    private fun getFloat(material: AIMaterial, key: String): Float {
        val intBuffer = BufferUtils.createIntBuffer(1)
        val floatBuffer = BufferUtils.createFloatBuffer(1)
        val result = aiGetMaterialFloatArray(material, key, aiTextureType_NONE, 1, floatBuffer, intBuffer)
        return if (result == 0) {
            floatBuffer.get()
        } else {
            DEFAULT_SPECULAR_VALUE
        }
    }
}
