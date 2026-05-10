package graphics.shaders

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.GL_COMPILE_STATUS
import org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH
import org.lwjgl.opengl.GL20.glDeleteShader
import org.lwjgl.opengl.GL20.glGetShaderInfoLog
import org.lwjgl.opengl.GL20.glGetShaderi
import org.lwjgl.opengl.GL20C

class Shader(type: Int, source: String) {

    val handle = GL20.glCreateShader(type)

    init {
        GL20.glShaderSource(handle, source)

        GL20.glCompileShader(handle)
        val compiled = glGetShaderi(handle, GL_COMPILE_STATUS)
        if (compiled != GL_TRUE) {
            val log = glGetShaderInfoLog(handle, glGetShaderi(handle, GL_INFO_LOG_LENGTH))
            throw IllegalArgumentException("Could not compile shader: $type\n$log")
        }
    }

    fun destroy() {
        glDeleteShader(handle)
    }
}
