package Main;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Random;


public class World {

    private static JFrame frame = new JFrame();

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

    private static float[][] moisture, elevation;

    private static MapCoord[][] mapCoords;

    public static final int MESHLENGTH = 1000;

    private static Loader loader;

    public World(Main main) {

        frame.getContentPane().setLayout(new FlowLayout());

        this.main = main;
        shader = main.getShader();
        camera = main.getCamera();
        renderer = main.getRenderer();
        loader = main.getLoader();

        biomeGenerator = new BiomeGenerator();

        mapCoords = new MapCoord[MESHLENGTH][MESHLENGTH];

        buildNoise();

        vertices = new float[24];

        rawModel = buildMesh();
        texture = buildTexture();
        texturedModel = new TexturedModel(rawModel, texture);
/*
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/



    }


    private static void buildNoise() {

        Random r = new Random();

        simplexNoiseGenerator = new SimplexNoiseGenerator(MESHLENGTH, MESHLENGTH);

        elevation = simplexNoiseGenerator.generateNoise(r.nextInt(), true);
        moisture = simplexNoiseGenerator.generateNoiseM(r.nextInt(), true);

       /*makeImage(elevation, true);
       makeImage(moisture, true);*/


        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {
                mapCoords[i][j] = new MapCoord();
                mapCoords[i][j].setElevation(biomeGenerator.smoothElevation(elevation[i][j]));
                mapCoords[i][j].setMoisture(moisture[i][j]);

                mapCoords[i][j].x = i;
                mapCoords[i][j].y = j;

            }
        }

/*
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {


                if (i < mapCoords.length-1)
                    mapCoords[i][j].setEast(mapCoords[i+1][j]);
                if (i > 0)
                    mapCoords[i][j].setWest(mapCoords[i-1][j]);

                if (i > 0 && j > 0)
                    corners[i][j].setNw(centers[i-1][j-1]);
                if (i < centers.length-1 && j > 0)
                    corners[i][j].setNe(centers[i][j-1]);
                if (i > 0 && j < centers[0].length-1)
                    corners[i][j].setSw(centers[i-1][j]);
                if (i < centers.length-1 && j < centers[0].length-1)
                    corners[i][j].setSe(centers[i][j]);

            }
        }*/


       /*for (int i = 0; i < 2000; i++) {
            Random random = new Random();
            ArrayList<MapCoord> riverList = new ArrayList();

            centers[random.nextInt(centers.length)][random.nextInt(centers.length)].createRiver(false, riverList);
        }*/

        ByteBuffer texture = BufferUtils.createByteBuffer(MESHLENGTH * MESHLENGTH * 4);
        int[] colors = new int[3];

       for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {
                mapCoords[i][j].setBiome(biomeGenerator.generateBiome(
                        mapCoords[i][j].getElevation(),
                        mapCoords[i][j].getMoisture(),
                        mapCoords[i][j].getHeat(),
                        mapCoords[i][j].isLake(),
                        mapCoords[i][j].isRiver())
                );


                colors = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome());


                texture.put((byte)(colors[0] & 0xFF)); //red
                texture.put((byte)((colors[0] >> 8) & 0xFF)); //green
                texture.put((byte)((colors[0] >> 16) & 0xFF)); // blue
                texture.put((byte)(255 & 0xFF)); //alpha
            }
        }



        /*

        for (int i = 0; i < centers.length; i++) {
            for (int j = 0; j < centers[i].length; j++) {

                 centers[i][j].setBiome(biomeGenerator.generateBiome(centers[i][j].getElevation(),
                        centers[i][j].getMoisture(), centers[i][j].getHeat(), centers[i][j].isLake(), centers[i][j].isRiver()));
            }
        }*/


    }

    private RawModel buildMesh() {

        float border = 0.05f;

        //Center
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        //North West
        vertices[3] = -0.5f+border;
        vertices[4] = -1f/3+border;
        vertices[5] = 0;
        //North
        vertices[6] = 0;
        vertices[7] = -2f/3+border;
        vertices[8] = 0;
        //North east
        vertices[9] = 0.5f-border;
        vertices[10] = -1f/3+border;
        vertices[11] = 0;
        //South east
        vertices[12] = 0.5f-border;
        vertices[13] = 1f/3-border;
        vertices[14] = 0;
        //South
        vertices[15] = 0;
        vertices[16] = 2f/3-border;
        vertices[17] = 0;
        //South west
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

        //ByteBuffer texture = BufferUtils.createByteBuffer(MESHLENGTH * MESHLENGTH * 4);


        int[] colors = new int[3];
        ByteBuffer texture = BufferUtils.createByteBuffer(MESHLENGTH * MESHLENGTH * 4);

        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {

                colors = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome());

                texture.put((byte)(colors[0] & 0xFF)); //red
                texture.put((byte)((colors[0] >> 8) & 0xFF)); //green
                texture.put((byte)((colors[0] >> 16) & 0xFF)); // blue
                texture.put((byte)(255 & 0xFF)); //alpha


            }
        }
        return loader.createTextureFromByteBuffer(texture, mapCoords.length, mapCoords[0].length);
    }


    private static float average(float ul, float ur, float ll, float lr) {
        return (ul+ur+ll+lr)/4;
    }

    private static void makeImage(float[][] map, boolean isClamped) {

        BufferedImage img = new BufferedImage(map.length, map[0].length, BufferedImage.TYPE_INT_RGB);

        float[][] tempMap = new float[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {

                tempMap[j][map.length-1-i] = map[i][j];

            }
        }

        int rgb = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {

                if (!isClamped) {
                    rgb = new Color((int) (((tempMap[i][j] + 1) / 2) * 255), (int) (((tempMap[i][j] + 1) / 2) * 255), (int) (((tempMap[i][j] + 1) / 2) * 255)).getRGB();
                }

                if (isClamped) {
                    rgb = new Color((int) (tempMap[i][j] * 255), (int) (tempMap[i][j] * 255), (int) (tempMap[i][j] * 255)).getRGB();
                }

                img.setRGB(i, j, rgb);
            }
        }

        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();

    }

    public void rebuild() {
        //frame.getContentPane().removeAll();
        buildNoise();
        rawModel = buildMesh();
    }


    public static void update() {
        shader.bind();
    }

    public void render(){

        shader.bind();

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

