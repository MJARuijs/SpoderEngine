package graphics.model

import graphics.shaders.ShaderProgram
import graphics.model.mesh.Mesh
import graphics.model.material.Material

class Shape(val mesh: Mesh, private val material: Material) {

    fun render(shaderProgram: ShaderProgram) {
        material.setProperties(shaderProgram)
        mesh.render()
    }
}
