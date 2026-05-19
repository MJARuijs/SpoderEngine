package graphics.model.material

import graphics.shaders.ShaderProgram

interface Material {

    fun setProperties(shaderProgram: ShaderProgram)

}
