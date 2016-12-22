package Main;

import Utils.ArrayManipulation;
import Utils.DiverseUtilities;
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
    private static JPanel[] panels;
    private static JScrollPane[] scrollPanes;
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
    private static float[][] moisture, islandShape, temprature, smoothElevation, elevation;
    public static final int MESHLENGTH = 1000, NOISEMAPLENGTH = MESHLENGTH;
    private static MapCoord[][] mapCoords = new MapCoord[MESHLENGTH][MESHLENGTH];
    private static int offset = (NOISEMAPLENGTH-MESHLENGTH)/2;
    private static int offsetPlusLength = offset + mapCoords.length;
    private static Loader loader;
    private static Matrix4f projection, tile_pos;

    public World(Main main) {

        this.main = main;
        shader = main.getWorldShader();
        camera = main.getCamera();
        renderer = main.getRenderer();
        loader = main.getLoader();
        biomeGenerator = new BiomeGenerator();
        buildNoise();
        vertices = new float[18];
        rawModel = buildModel(0.05f);
        texture = buildTexture();
        texturedModel = new TexturedModel(rawModel, texture);
    }

    private static void buildNoise() {
        Random rand = new Random();
        simplexNoiseGenerator = new SimplexNoiseGenerator();
        islandShape = simplexNoiseGenerator.generateElevation(rand.nextInt(), NOISEMAPLENGTH, NOISEMAPLENGTH);
        moisture = new float[NOISEMAPLENGTH][NOISEMAPLENGTH];
        temprature = new float[NOISEMAPLENGTH][NOISEMAPLENGTH];
        smoothElevation = BiomeGenerator.smoothElevation(islandShape);
        //builds mountains
        for (int i = 0; i < 200; i++) {
            int x = rand.nextInt(islandShape.length);
            int y = rand.nextInt(islandShape.length);
            if (islandShape[x][y] > 0.01f) {
                int range = rand.nextInt(100) + 10;
                int size = (int) (range*1.5f);
                boolean direction = rand.nextBoolean();
                int plusOrMinus = rand.nextInt(2)*2-1;
                float a = (float) (Math.random()-0.5f)/5;
                float b = (float) (Math.random()-0.5f)/5;
                float c = (float) (Math.random()-0.5f)/5;
                float e = (float) (Math.random()-0.5f)/5;
                for (int j = 0; j < size; j++) {
                    int randomizer = rand.nextInt(range)-range/2; //Where along length of mountain range. 0 if no shift in x axis
                    int x2 = x + randomizer;
                    int y2 = rand.nextInt(10)-5 + y + (int) (Math.pow(e*randomizer, 4) + Math.pow(a*randomizer, 3) + Math.pow(b*randomizer, 2) + c*randomizer) * plusOrMinus;
                    if (direction) {
                        y2 = y + randomizer;
                        x2 = rand.nextInt(10)-5 + x + (int) (Math.pow(e*randomizer, 4) + Math.pow(a*randomizer, 3) + Math.pow(b*randomizer, 2) + c*randomizer) * plusOrMinus;
                    }
                    System.out.println(i + ": " + x2 + " " + y2);
                    if (x2 > 1 && y2 > 1 && x2 < islandShape.length-1 && y2 < islandShape.length-1) {
                        if (islandShape[x2][y2] > 0.01f) {
                            islandShape[x2][y2] += 0.2f;
                            islandShape[x2 + 1][y2 + 1] += 0.1f;
                            islandShape[x2 - 1][y2 + 1] += 0.1f;
                            islandShape[x2 + 1][y2 - 1] += 0.1f;
                            islandShape[x2 - 1][y2 - 1] += 0.1f;
                            islandShape[x2][y2 + 1] += 0.1f;
                            islandShape[x2][y2 - 1] += 0.1f;
                            islandShape[x2 - 1][y2] += 0.1f;
                            islandShape[x2 + 1][y2] += 0.1f;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {
                mapCoords[i][j] = new MapCoord();
                mapCoords[i][j].smoothElevation = smoothElevation[i+offset][j+offset];
//                float x = (float) i / mapCoords.length;
//                float y = (float) j / mapCoords.length;
//                float dist = (float)(Math.sqrt(Math.pow(Math.abs(x - 0.5f),2) + Math.pow(Math.abs(y - 0.5f),2)));
//                mapCoords[i][j].isOcean = (islandShape[i+offset][j+offset] < Math.pow(dist+0.5f, 3));
                mapCoords[i][j].isOcean = (islandShape[i+offset][j+offset] < 0.01f);
                mapCoords[i][j].x = i;
                mapCoords[i][j].y = j;
            }
        }
        for (int i = 0; i < islandShape.length; i++) {
            for (int j = 0; j < islandShape[i].length; j++) {
                moisture[i][j] = 1f;
                if (i >= offset && j >= offset) {
                    if (i < offsetPlusLength && j < offsetPlusLength) {
                        if (!mapCoords[i - offset][j - offset].isOcean)
                            moisture[i][j] = 0f;
                    }
                }
            }
        }
        float[][] ocean = moisture;
        int r1 = rand.nextInt(2) * 2 - 1;
        for (int i = 0; i < 35; i++) {
            moisture = Wind.spreadMoisture(moisture, ocean, r1, -1);
            moisture = ArrayManipulation.clampf(moisture, 1f, 0f);
        }
        moisture = Wind.spreadMoisture(moisture, ocean, -r1, -1);
//        moisture = Wind.spreadMoisture(moisture, ocean, 1, 1);
        moisture = ArrayManipulation.clampf(moisture, 1f, 0f);
        ArrayManipulation.gaussianBlur2Df(moisture);

        /*ArrayManipulation.gaussianBlur2Df(moisture);
        ArrayManipulation.gaussianBlur2Df(moisture);
        ArrayManipulation.gaussianBlur2Df(moisture);
        ArrayManipulation.gaussianBlur2Df(moisture);*/

        int numFrames = 1;
        frames = new JFrame[numFrames];
        panels = new JPanel[numFrames];
        scrollPanes = new JScrollPane[numFrames];


        makeImage(islandShape, 0, "Island Shape");
//          makeImage(moisture, 0, "Moisture");
//        makeImage(smoothElevation, 1, "Smooth smoothElevation");


        for (int i = 0; i < frames.length; i++) {
            frames[i].setVisible(true);
            frames[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        /*Assigns location, smoothElevation and moisture data for each Mapcoord*/
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {
                mapCoords[i][j].setMoisture(moisture[i+offset][j+offset]);
            }
        }

//        for (int i = 0; i < mapCoords.length; i++) {
//            for (int j = 0; j < mapCoords[i].length; j++) {
//                mapCoords[i][j] = new MapCoord();
//                mapCoords[i][j].smoothElevation = smoothElevation[i+offset][j+offset];
//                mapCoords[i][j].biomeElevation = islandShape[i+offset][j+offset];
//                float x = (float) i / mapCoords.length;
//                float y = (float) j / mapCoords.length;
//                float dist = (float)(Math.sqrt(Math.pow(Math.abs(x - 0.5f),2) + Math.pow(Math.abs(y - 0.5f),2)));
////                System.out.println(mapCoords[i][j].biomeElevation + " : " + dist + " " + x + " " + y);
//                mapCoords[i][j].isOcean = (mapCoords[i][j].biomeElevation < Math.pow(dist+0.5f, 3));
//                mapCoords[i][j].setMoisture(moisture[i+offset][j+offset]);
//                mapCoords[i][j].x = i;
//                mapCoords[i][j].y = j;
//            }
//        }

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
        ArrayList<MapCoord> riverList = new ArrayList();
        for (int i = 0; i < 1000; i++) {
            mapCoords[rand.nextInt(mapCoords.length)][rand.nextInt(mapCoords.length)].createRiver(false, riverList);
            riverList.clear();
        }


        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords.length; j++) {
//                if (mapCoords[i][j].smoothElevation > 0.9f) {
//                    mapCoords[i][j].createMountain(rand.nextInt(100));
//                    continue;
//                } else if (mapCoords[i][j].smoothElevation > 0.8f) {
//                    if (rand.nextInt(20) == 0)
//                        mapCoords[i][j].createMountain(rand.nextInt(100));
//                } else if (mapCoords[i][j].smoothElevation > 0.7f) {
//                    if (rand.nextInt(100) == 0)
//                        mapCoords[i][j].createMountain(rand.nextInt(100));
//                } else if (mapCoords[i][j].smoothElevation > 0.5f) {
//                    if (rand.nextInt(2000) == 0)
//                        mapCoords[i][j].createMountain(rand.nextInt(100));
//                } else if (mapCoords[i][j].smoothElevation > 0.3f) {
//                    if (rand.nextInt(5000) == 0)
//                        mapCoords[i][j].createMountain(rand.nextInt(100));
//                } else if (mapCoords[i][j].smoothElevation > 0.1f) {
//                    if (rand.nextInt(10000) == 0)
//                        mapCoords[i][j].createMountain(rand.nextInt(100));
//                }
            }
        }
        /*Sets biome for each Mapcoord*/
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {
                float latitude = (float)j/mapCoords.length;
                mapCoords[i][j].setBiome(biomeGenerator.generateBiome(
                        islandShape[i+offset][j+offset],
                        mapCoords[i][j].moisture,
                        mapCoords[i][j].isRiver,
                        mapCoords[i][j].isMountainTop,
                        mapCoords[i][j].isMountainSide,
                        mapCoords[i][j].isOcean,
                        latitude
                        )
                );
            }
        }
    }

//    private void buildModelVertices(float border) {
//        //Center vertex
//        vertices[0] = 0;
//        vertices[1] = 0;
//        vertices[2] = 0;
//        //North West vertex
//        vertices[3] = -0.5f+border;
//        vertices[4] = -1f/3+border;
//        vertices[5] = 0;
//        //North vertex
//        vertices[6] = 0;
//        vertices[7] = -2f/3+border;
//        vertices[8] = 0;
//        //North east vertex
//        vertices[9] = 0.5f-border;
//        vertices[10] = -1f/3+border;
//        vertices[11] = 0;
//        //South east vertex
//        vertices[12] = 0.5f-border;
//        vertices[13] = 1f/3-border;
//        vertices[14] = 0;
//        //South vertex
//        vertices[15] = 0;
//        vertices[16] = 2f/3-border;
//        vertices[17] = 0;
//        //South west vertex
//        vertices[18] = -0.5f+border;
//        vertices[19] = 1f/3-border;
//        vertices[20] = 0;
//    }

    private void buildModelVertices(float border) {
        //North West vertex
        vertices[0] = -0.5f+border;
        vertices[1] = -1f/3+border;
        vertices[2] = 0;
        //North vertex
        vertices[3] = 0;
        vertices[4] = -2f/3+border;
        vertices[5] = 0;
        //North east vertex
        vertices[6] = 0.5f-border;
        vertices[7] = -1f/3+border;
        vertices[8] = 0;
        //South east vertex
        vertices[9] = 0.5f-border;
        vertices[10] = 1f/3-border;
        vertices[11] = 0;
        //South vertex
        vertices[12] = 0;
        vertices[13] = 2f/3-border;
        vertices[14] = 0;
        //South west vertex
        vertices[15] = -0.5f+border;
        vertices[16] = 1f/3-border;
        vertices[17] = 0;
    }

//    private RawModel buildModel(float border) {
//        buildModelVertices(border);
//        indices = new int[] {
//                0, 1, 2,
//                0, 2, 3,
//                0, 3, 4,
//                0, 4, 5,
//                0, 5, 6,
//                0, 6, 1,
//        };

    private RawModel buildModel(float border) {
        buildModelVertices(border);
        indices = new int[] {
                0, 1, 2,
                0, 2, 3,
                0, 3, 4,
                0, 4, 5
        };
        return loader.createRawModel(vertices, indices);
    }

    private Texture buildTexture() {
        int[] colors;
        int[][] r = new int[MESHLENGTH][MESHLENGTH];
        int[][] g = new int[MESHLENGTH][MESHLENGTH];
        int[][] b = new int[MESHLENGTH][MESHLENGTH];
        int[][] a = new int[MESHLENGTH][MESHLENGTH];
        Random rand = new Random();
        ByteBuffer texture = BufferUtils.createByteBuffer(MESHLENGTH * MESHLENGTH * 4);
        /*Makes color map*/
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {

                r[i][j] = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome())[0];
                g[i][j] = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome())[1];
                b[i][j] = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome())[2];
                a[i][j] = biomeGenerator.getBiomeColor(mapCoords[i][j].getBiome())[2];
            }
        }
        /*Filters*/
    /*    for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {

                colors[0] -= 100 * smoothElevation[i][j];
                colors[1] -= 100 * smoothElevation[i][j];
                colors[2] -= 100 * smoothElevation[i][j];

                if (mapCoords[i][j].getBiome() == "OCEAN") {
                    colors[1] += 10 * moisture[i][j] - 5;

                    colors[0] += 250 * smoothElevation[i][j];
                    colors[1] += 250 * smoothElevation[i][j];
                    colors[2] += 400 * smoothElevation[i][j];
                }


                if (j > 9 && i < smoothElevation.length-10) {
                    if (mapCoords[i][j].getBiome() != "OCEAN" && smoothElevation[i + 10][j - 10] > 0.5f) {
                        r[i][j] += 40 * smoothElevation[i + 10][j - 10];
                        g[i][j] += 40 * smoothElevation[i + 10][j - 10];
                        b[i][j] += 40 * smoothElevation[i + 10][j - 10];
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
                texture.put((byte)(a[i][j] & 0xFF));; //alpha
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
        rawModel = buildModel(0.05f);
        texture = buildTexture();
        texturedModel = new TexturedModel(rawModel, texture);
    }

    public void changeModel() {
        if (camera.getPosition().z < -100) {
           buildModelVertices(0f);
           int vaoID = loader.createVAO(vertices, indices);
           rawModel.replaceVAO(vaoID);
        }
        else if (camera.getPosition().z >= -100) {
            buildModelVertices(0.05f);
            int vaoID = loader.createVAO(vertices, indices);
            rawModel.replaceVAO(vaoID);
        }
    }

    /*Makes an image of a noismap*/
    private static void makeImage(float[][] map, int num, String title) {
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
                int r = (int) (DiverseUtilities.clamp(tempMap[i][j], 0f, 1f) * 255);
                int g = (int) (DiverseUtilities.clamp(tempMap[i][j], 0f, 1f) * 255);
                int b = (int) (DiverseUtilities.clamp(tempMap[i][j], 0f, 1f) * 255);
                rgb = new Color(r, g, b).getRGB();
                img.setRGB(i, j, rgb);
            }
        }
        if (panels[num] == null)
            frames[num] = new JFrame();
            panels[num] = new JPanel();
        panels[num].add(new JLabel(new ImageIcon(img)));
        scrollPanes[num] = new JScrollPane(panels[num]);
        frames[num].setTitle(title);
        frames[num].setSize(1000,1000);
        frames[num].setContentPane(scrollPanes[num]);
        frames[num].getContentPane().setLayout(new ScrollPaneLayout());
//        frames[num].getContentPane().add(panels[num]);
        frames[num].pack();
    }

    public void update() {
        changeModel();
    }

    public void render(){
        shader.bind();
        shader.setUniform("projectionMatrix", camera.getProjection());
        shader.setUniform("rotationMatrix", camera.getRotation());
        shader.setUniform("meshLength", MESHLENGTH);
        renderer.render(texturedModel, MESHLENGTH * MESHLENGTH);
    }

    public static MapCoord[][] getMapCoords() {
        return mapCoords;
    }
}

