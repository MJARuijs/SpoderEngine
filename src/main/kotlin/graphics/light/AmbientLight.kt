import graphics.shaders.ShaderProgram
import math.Color

data class AmbientLight(private val color: Color) {

    fun apply(shaderProgram: ShaderProgram) {
        shaderProgram.set("ambient.color", color)
    }
}

