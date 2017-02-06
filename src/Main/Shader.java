package Main;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int program, vs, fs;

    public Shader(String filename) {
        program = glCreateProgram();
        vs = glCreateShader(GL_VERTEX_SHADER);
        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vs, readShader(filename + ".vs.glsl"));
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }
        glShaderSource(fs, readShader(filename + ".fs.glsl"));
        glCompileShader(fs);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }
        glAttachShader(program, vs);
        glAttachShader(program, fs);
        glBindAttribLocation(program, 0, "coordinates");
        glBindAttribLocation(program, 1, "texCoords");
        glBindAttribLocation(program, 2, "normal");
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
//        glValidateProgram(program);
//        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
//            System.err.println(glGetProgramInfoLog(program));
//            System.exit(1);
//        }
    }

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform1i(location, value);
    }

    public void setUniform(String name, float value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform1f(location, value);
    }

    public void setUniform(String name, Vector2i value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform2i(location, value.x, value.y);
    }

    public void setUniform(String name, Vector3f value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform3f(location, value.x, value.y, value.z);
    }

    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location != -1)
            glUniformMatrix4fv(location, false, buffer);
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() { glUseProgram(0); }

    static String readShader(String filename) {
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Shader.class.getClassLoader().getResourceAsStream(filename)));
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
        return builder.toString();
    }
}
