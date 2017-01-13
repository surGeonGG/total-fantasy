package Utils;

import Entities.Entity;
import Main.Camera;
import Terrains.Terrain;
import nuklear.IOUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class DiverseUtilities {

    public static float clamp(float val, float lower, float upper) {
        if (val > upper) val = upper;
        if (val < lower) val = lower;
        return val;
    }

    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f targetMatrix = new Matrix4f();
        targetMatrix.identity();
        targetMatrix.translate(entity.getPosition());
        targetMatrix.rotate((float) Math.toRadians(entity.getRx()), new Vector3f(1,0,0));
        targetMatrix.rotate((float) Math.toRadians(entity.getRy()), new Vector3f(0,1,0));
        targetMatrix.rotate((float) Math.toRadians(entity.getRz()), new Vector3f(0,0,1));
        targetMatrix.scale(new Vector3f(entity.getScale(), entity.getScale(), entity.getScale()));
        return targetMatrix;
    }

    public static Matrix4f createTransformationMatrix(Terrain terrain) {
        Matrix4f targetMatrix = new Matrix4f();
        targetMatrix.identity();
        targetMatrix.translate(terrain.getPosition());
        targetMatrix.rotate((float) Math.toRadians(terrain.getRx()), new Vector3f(1,0,0));
        targetMatrix.rotate((float) Math.toRadians(terrain.getRy()), new Vector3f(0,1,0));
        targetMatrix.rotate((float) Math.toRadians(terrain.getRz()), new Vector3f(0,0,1));
        targetMatrix.scale(new Vector3f(terrain.getScale(), terrain.getScale(), terrain.getScale()));
        return targetMatrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f targetMatrix = new Matrix4f();
        targetMatrix.identity();
        targetMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0));
        targetMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0));
        Vector3f negativeCameraPosition = new Vector3f(camera.getPosition().x, camera.getPosition().y,
                camera.getPosition().z).mul(-1);
        targetMatrix.translate(negativeCameraPosition);
        return targetMatrix;
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        Path path = Paths.get(resource);
        if ( Files.isReadable(path) ) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
                while ( fc.read(buffer) != -1 ) ;
            }
        } else {
            try (
                    InputStream source = IOUtil.class.getClassLoader().getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)
            ) {
                buffer = createByteBuffer(bufferSize);
                while ( true ) {
                    int bytes = rbc.read(buffer);
                    if ( bytes == -1 )
                        break;
                    if ( buffer.remaining() == 0 )
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                }
            }
        }
        buffer.flip();
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
