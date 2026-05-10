package graphics.shaders

import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER
import util.File

class ShaderLoader {

    private fun getType(file: File): Int {
        val extension = file.getExtension()
        return when {
            extension.contains("vert", true) -> GL20.GL_VERTEX_SHADER
            extension.contains("frag", true) -> GL20.GL_FRAGMENT_SHADER
            extension.contains("geom", true) -> GL_GEOMETRY_SHADER
            else -> throw Exception("Unknown shader extension: $extension")
        }
    }

    fun load(path: String): Shader {
        val file = File(path)
        var source = ""

        file.getLines().forEach { line -> source += "$line\n" }
        return Shader(getType(file), source)
    }	
}
