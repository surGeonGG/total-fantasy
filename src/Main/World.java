package Main;

import Utils.ArrayManipulation;
import Utils.DiverseUtilities;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;


public class World {

    private JFrame[] frames;
    private JPanel[] panels;
    private JScrollPane[] scrollPanes;
    private Main main;
    private RawModel rawModel;
    private Texture texture;
    private TexturedModel texturedModel;
    private Camera camera;
    private Shader worldShader;
    private Renderer renderer;
    private BiomeGenerator biomeGenerator;
    private float[] vertices;
    private int[] indices;
    private SimplexNoiseGenerator simplexNoiseGenerator;
    private float[][] moisture, islandShape, temprature, smoothElevation, elevation;
    public final int MESHLENGTH = 1000, NOISEMAPLENGTH = MESHLENGTH;
    private MapCoord[][] mapCoords = new MapCoord[MESHLENGTH][MESHLENGTH];
    private int offset = (NOISEMAPLENGTH-MESHLENGTH)/2;
    private int offsetPlusLength = offset + mapCoords.length;
    private Loader loader;
    private Matrix4f projection, tile_pos;

    public World(Main main) {
        this.camera = camera;
        this.main = main;
        this.worldShader = worldShader;
        worldShader.bind();
        worldShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        this.renderer = renderer;
        this.loader = loader;
        biomeGenerator = new BiomeGenerator();
        buildNoise();
        vertices = new float[18];
        rawModel = buildModel(0.05f);
        texture = buildTexture();
        texturedModel = new TexturedModel(rawModel, texture);
    }

    private void buildNoise() {
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
                int size = (int) (range * 1.5f);
                boolean direction = rand.nextBoolean();
                int plusOrMinus = rand.nextInt(2) * 2 - 1;
                float a = (float) (Math.random() - 0.5f) / 5;
                float b = (float) (Math.random() - 0.5f) / 5;
                float c = (float) (Math.random() - 0.5f) / 5;
                float e = (float) (Math.random() - 0.5f) / 5;
                for (int j = 0; j < size; j++) {
                    int randomizer = rand.nextInt(range) - range / 2; //Where along length of mountain range. 0 if no shift in x axis
                    int x2 = x + randomizer;
                    int y2 = rand.nextInt(10) - 5 + y + (int) (Math.pow(e * randomizer, 4) + Math.pow(a * randomizer, 3) + Math.pow(b * randomizer, 2) + c * randomizer) * plusOrMinus;
                    if (direction) {
                        y2 = y + randomizer;
                        x2 = rand.nextInt(10) - 5 + x + (int) (Math.pow(e * randomizer, 4) + Math.pow(a * randomizer, 3) + Math.pow(b * randomizer, 2) + c * randomizer) * plusOrMinus;
                    }
                    System.out.println(i + ": " + x2 + " " + y2);
                    if (x2 > 1 && y2 > 1 && x2 < islandShape.length - 1 && y2 < islandShape.length - 1) {
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
                mapCoords[i][j].smoothElevation = smoothElevation[i + offset][j + offset];
//                float x = (float) i / mapCoords.length;
//                float y = (float) j / mapCoords.length;
//                float dist = (float)(Math.sqrt(Math.pow(Math.abs(x - 0.5f),2) + Math.pow(Math.abs(y - 0.5f),2)));
//                mapCoords[i][j].isOcean = (islandShape[i+offset][j+offset] < Math.pow(dist+0.5f, 3));
                mapCoords[i][j].isOcean = (islandShape[i + offset][j + offset] < 0.01f);
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

        int numFrames = 0;
        frames = new JFrame[numFrames];
        panels = new JPanel[numFrames];
        scrollPanes = new JScrollPane[numFrames];


//        makeImage(islandShape, 0, "Island Shape");
//          makeImage(moisture, 0, "Moisture");
//        makeImage(smoothElevation, 1, "Smooth smoothElevation");


        for (int i = 0; i < frames.length; i++) {
            frames[i].setVisible(true);
            frames[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        /*Assigns location, smoothElevation and moisture data for each Mapcoord*/
        for (int i = 0; i < mapCoords.length; i++) {
            for (int j = 0; j < mapCoords[i].length; j++) {
                mapCoords[i][j].setMoisture(moisture[i + offset][j + offset]);
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
        for (int i = 1; i < mapCoords.length - 1; i++) {
            for (int j = 1; j < mapCoords[i].length - 1; j++) {
                mapCoords[i][j].setEast(mapCoords[i + 1][j]);
                mapCoords[i][j].setWest(mapCoords[i - 1][j]);
                if (j % 2 == 0) {
                    mapCoords[i][j].setNw(mapCoords[i][j + 1]);
                    mapCoords[i][j].setNe(mapCoords[i + 1][j + 1]);
                    mapCoords[i][j].setSw(mapCoords[i][j - 1]);
                    mapCoords[i][j].setSe(mapCoords[i + 1][j - 1]);
                } else {
                    mapCoords[i][j].setNw(mapCoords[i - 1][j + 1]);
                    mapCoords[i][j].setNe(mapCoords[i][j + 1]);
                    mapCoords[i][j].setSw(mapCoords[i - 1][j - 1]);
                    mapCoords[i][j].setSe(mapCoords[i][j - 1]);
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

            }
        }
    }


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
        /*Makes color terrain*/


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
                texture.put((byte)(a[i][j] & 0xFF)); //alpha
            }
        }
        texture.flip();
        return loader.create2DTextureFromByteBuffer(texture, mapCoords.length, mapCoords[0].length);
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
    private void makeImage(float[][] map, int num, String title) {
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
        worldShader.bind();
        worldShader.setUniform("viewMatrix", DiverseUtilities.createViewMatrix(camera));
        worldShader.setUniform("meshLength", MESHLENGTH);
        renderer.renderInstanced(texturedModel, MESHLENGTH * MESHLENGTH);
    }

    public MapCoord[][] getMapCoords() {
        return mapCoords;
    }
}

