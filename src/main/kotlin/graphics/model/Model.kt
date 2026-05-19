package graphics.model

import graphics.model.mesh.Mesh
import graphics.shaders.ShaderProgram

class Model(private val shapes: List<Shape>) {

    fun render(shaderProgram: ShaderProgram) {
        shapes.forEach { it.render(shaderProgram) }
    }

    fun destroy() {
        shapes.map(Shape::mesh).distinct().forEach(Mesh::destroy)
    }
}
