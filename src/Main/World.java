package Main;

import Utils.ArrayManipulation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;


public class World {

    private static JFrame[] frames;

    private static Main main;

    private RawModel rawModel;

    private Texture texture;

    private TexturedModel texturedModel;

    private static Camera camera;

    private static Shader shader;

    private static Renderer renderer;

    private static BiomeGenerator biomeGenerator;

    private float[] vertices;

    private int[] indices;

    private static SimplexNoiseGenerator simplexNoiseGenerator;

    private static float[][] moisture, elevation, smoothElevation;

    private static MapCoord[][] mapCoords;

    public static final int MESHLENGTH = 1000, NOISEMAPLENGTH = MESHLENGTH + 500;

    private static Loader loader;

    public World(Main main) {

        frames = new JFrame[3];


        this.main = main;
        shader = main.getShader();
        camera = main.getCamera();
        renderer = main.getRenderer();
        loader = main.getLoader();

        biomeGenerator = new BiomeGenerator();

        mapCoords = new MapCoord[MESHLENGTH][MESHLENGTH];

        buildNoise();

        vertices = new float[24];

        rawModel = buildModel();
        texture = buildTexture();
        texturedModel = new TexturedModel(rawModel, texture);

        for (int i = 0; i < frames.length; i++) {
            frames[i].setVisible(true);
            frames[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }


    private static void buildNoise() {

        Random rand = new Random();

        simplexNoiseGenerator = new SimplexNoiseGenerator(NOISEMAPLENGTH, NOISEMAPLENGTH);

        /*elevation = simplexNoiseGenerator.generateElevation(557998954);*/
        elevation = simplexNoiseGenerator.generateElevation(rand.nextInt());
        //moisture = simplexNoiseGenerator.generateMoisture(rand.nextInt());
        moisture = new float[NOISEMAPLENGTH][NOISEMAPLENGTH];
        smoothElevation = BiomeGenerator.smoothElevation(elevation);

        for (int i = 0; i < elevation.length; i++) {
            for (int j = 0; j < elevation[i].length; j++) {
                if (elevation[i][j] < 0.1f)
                    moisture[i][j] = 1f;

            }
        }

        for (int i = 0; i < 20; i++) {
            moisture = Wind.spreadMoisture(moisture, elevation, 1, -1);
            moisture = ArrayManipulation.clampf(moisture, 1f, 0f);
            ArrayManipulation.gaussianBlur2Df(moisture);
        }
        moisture = Wind.spreadMoisture(moisture, elevation, 1, 1);
        moisture = ArrayManipulation.clampf(moisture, 1f, 0f);

        /*ArrayManipulation.gaussianBlur2Df(moisture);
        ArrayManipulation.gaussianBlur2Df(moisture);
        ArrayManipulation.gaussianBlur2Df(moisture);
        ArrayManipulation.gaussianBlur2Df(moisture);*/


        makeImage(elevation, 0, "Elevation");
        makeImage(moisture, 1, "Moisture");
        makeImage(smoothElevation, 2, "Smooth elevation");

        /*Assigns location, elevation and moisture data for each Mapcoord*/
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {
                mapCoords[i][j] = new MapCoord();
                mapCoords[i][j].setElevation(smoothElevation[i][j]);

                mapCoords[i][j].setBiomeElevation(elevation[i][j]);

                mapCoords[i][j].setMoisture(moisture[i][j]);

                mapCoords[i][j].x = i;
                mapCoords[i][j].y = j;

            }
        }

        /*Sets adjacenct Mapcoords for each Mapcoord*/
        for (int i = 1; i < mapCoords.length-1; i++) {
            for (int j = 1; j < mapCoords[i].length-1; j++) {

                    mapCoords[i][j].setEast(mapCoords[i+1][j]);
                    mapCoords[i][j].setWest(mapCoords[i-1][j]);


                    if (j % 2 == 0) {
                        mapCoords[i][j].setNw(mapCoords[i]      [j+1]);
                        mapCoords[i][j].setNe(mapCoords[i+1]    [j+1]);
                        mapCoords[i][j].setSw(mapCoords[i]      [j-1]);
                        mapCoords[i][j].setSe(mapCoords[i+1]    [j-1]);
                    }
                    else {
                        mapCoords[i][j].setNw(mapCoords[i-1]    [j+1]);
                        mapCoords[i][j].setNe(mapCoords[i]      [j+1]);
                        mapCoords[i][j].setSw(mapCoords[i-1]    [j-1]);
                        mapCoords[i][j].setSe(mapCoords[i]      [j-1]);
                    }

            }
        }

        /*Builds rivers*/
        for (int i = 0; i < 1000; i++) {
            ArrayList<MapCoord> riverList = new ArrayList();

            mapCoords[rand.nextInt(mapCoords.length)][rand.nextInt(mapCoords.length)].createRiver(false, riverList);

        }
        for (int i = 0; i < 1000; i++) {

            mapCoords[rand.nextInt(mapCoords.length)][rand.nextInt(mapCoords.length)].createMountain(rand.nextInt(100));
        }

        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords.length; j++) {

                if (mapCoords[i][j].elevation > 0.9f) {
                    mapCoords[i][j].createMountain(rand.nextInt(100));
                    continue;
                }
                else if (mapCoords[i][j].elevation > 0.8f) {

                    if (rand.nextInt(5) == 0) {
                        mapCoords[i][j].createMountain(rand.nextInt(100));
                        continue;
                    }

                } else if (mapCoords[i][j].elevation > 0.7f)
                    if (rand.nextInt(20) == 0)
                        mapCoords[i][j].createMountain(rand.nextInt(100));
            }
        }

        /*Sets biome for each Mapcoord*/
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {
                mapCoords[i][j].setBiome(biomeGenerator.generateBiome(
                        mapCoords[i][j].biomeElevation,
                        mapCoords[i][j].moisture,
                        mapCoords[i][j].isRiver,
                        mapCoords[i][j].isMountainTop,
                        mapCoords[i][j].isMountainSide
                        )
                );

            }
        }
    }

    private RawModel buildModel() {

        float border = 0.05f;

        //Center vertex
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        //North West vertex
        vertices[3] = -0.5f+border;
        vertices[4] = -1f/3+border;
        vertices[5] = 0;
        //North vertex
        vertices[6] = 0;
        vertices[7] = -2f/3+border;
        vertices[8] = 0;
        //North east vertex
        vertices[9] = 0.5f-border;
        vertices[10] = -1f/3+border;
        vertices[11] = 0;
        //South east vertex
        vertices[12] = 0.5f-border;
        vertices[13] = 1f/3-border;
        vertices[14] = 0;
        //South vertex
        vertices[15] = 0;
        vertices[16] = 2f/3-border;
        vertices[17] = 0;
        //South west vertex
        vertices[18] = -0.5f+border;
        vertices[19] = 1f/3-border;
        vertices[20] = 0;

        indices = new int[] {
                0, 1, 2,
                0, 2, 3,
                0, 3, 4,
                0, 4, 5,
                0, 5, 6,
                0, 6, 1,
        };

        return loader.createRawModel(vertices, indices);

    }

    private Texture buildTexture() {

        int[] colors;
        int[][] r = new int[MESHLENGTH][MESHLENGTH];
        int[][] g = new int[MESHLENGTH][MESHLENGTH];
        int[][] b = new int[MESHLENGTH][MESHLENGTH];
        Random rand = new Random();

        ByteBuffer texture = BufferUtils.createByteBuffer(MESHLENGTH * MESHLENGTH * 4);


        /*Makes color map*/
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {

                r[i][j] = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome())[0];
                g[i][j] = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome())[1];
                b[i][j] = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome())[2];
            }
        }

        /*Filters*/
    /*    for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {

                colors[0] -= 100 * elevation[i][j];
                colors[1] -= 100 * elevation[i][j];
                colors[2] -= 100 * elevation[i][j];

                if (mapCoords[i][j].getBiome() == "OCEAN") {
                    colors[1] += 10 * moisture[i][j] - 5;

                    colors[0] += 250 * elevation[i][j];
                    colors[1] += 250 * elevation[i][j];
                    colors[2] += 400 * elevation[i][j];
                }


                if (j > 9 && i < elevation.length-10) {
                    if (mapCoords[i][j].getBiome() != "OCEAN" && elevation[i + 10][j - 10] > 0.5f) {
                        r[i][j] += 40 * elevation[i + 10][j - 10];
                        g[i][j] += 40 * elevation[i + 10][j - 10];
                        b[i][j] += 40 * elevation[i + 10][j - 10];
                    }
                }
            }
        }*/

      /*  Puts colors into bytebuffer*/
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {

                if (r[i][j] > 255) r[i][j] = 255;
                if (g[i][j] > 255) g[i][j] = 255;
                if (b[i][j] > 255) b[i][j] = 255;

                if (r[i][j] < 0) r[i][j] = 0;
                if (g[i][j] < 0) g[i][j] = 0;
                if (b[i][j] < 0) b[i][j] = 0;

                texture.put((byte)(r[i][j] & 0xFF)); //red
                texture.put((byte)(g[i][j] & 0xFF)); //green
                texture.put((byte)(b[i][j] & 0xFF)); // blue
                texture.put((byte)(255 & 0xFF)); //alpha
            }
        }

        texture.flip();
        return loader.createTextureFromByteBuffer(texture, mapCoords.length, mapCoords[0].length);
    }

    public void rebuild() {
        for (int i = 0; i < frames.length; i++) {
            frames[i].getContentPane().removeAll();
        }
        buildNoise();
        rawModel = buildModel();
        texture = buildTexture();
        texturedModel = new TexturedModel(rawModel, texture);
    }

    /*Makes an image of a noismap*/
    private static void makeImage(float[][] map, int frame, String title) {

        BufferedImage img = new BufferedImage(map.length, map[0].length, BufferedImage.TYPE_INT_RGB);

        float[][] tempMap = new float[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {

                tempMap[j][map.length - 1 - i] = map[i][j];

            }
        }

        int rgb = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {

                rgb = new Color((int) (tempMap[i][j] * 255), (int) (tempMap[i][j] * 255), (int) (tempMap[i][j] * 255)).getRGB();

                img.setRGB(i, j, rgb);
            }
        }

        if (frames[frame] == null)
            frames[frame] = new JFrame();
        frames[frame].setTitle(title);
        frames[frame].getContentPane().setLayout(new FlowLayout());
        frames[frame].getContentPane().add(new JLabel(new ImageIcon(img)));
        frames[frame].pack();
    }

    public static void update() {
        shader.bind();
    }

    public void render(){

        Matrix4f projection = new Matrix4f();
        Matrix4f rotation = new Matrix4f();
        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(0, 0, 0));
        camera.getProjection().mul(tile_pos, projection);
        camera.getRotation().mul(tile_pos, rotation);

        shader.setUniform("projectionMatrix", projection);
        shader.setUniform("rotationMatrix", rotation);
        shader.setUniform("meshLength", MESHLENGTH);

        renderer.render(texturedModel, MESHLENGTH * MESHLENGTH);

        shader.setUniform("projectionMatrix", camera.getProjection());
    }

    public static MapCoord[][] getMapCoords() {
        return mapCoords;
    }

}

