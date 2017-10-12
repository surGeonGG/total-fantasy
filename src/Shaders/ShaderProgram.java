package Shaders;

import Main.Main;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private int program, vs, fs;

    public ShaderProgram(String filename) {
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

    public void bindAttribute(String name, int index) {
        glBindAttribLocation(program, index, name);
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(program, name);
    }

    public void setUniform(int location, int value) {
        glUniform1i(location, value);
    }

    public void setUniform(int location, float value) {
        glUniform1f(location, value);
    }

    public void setUniform(int location, Vector2i value) {
        glUniform2i(location, value.x, value.y);
    }

    public void setUniform(int location, Vector3f value) {
        glUniform3f(location, value.x, value.y, value.z);
    }

    public void setUniform(int location, Vector4f value) {
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public void setUniform(int location, Matrix4f value) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderProgram.class.getClassLoader().getResourceAsStream(filename)));
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
