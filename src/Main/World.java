package Main;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


public class World {

    private static JFrame frame = new JFrame();

    private static Main main;

    private static Camera camera;

    private static Shader shader;

    private static BiomeGenerator biomeGenerator;

    private static SimplexNoiseGenerator simplexNoiseGenerator;

    private static float[][] moisture, elevation, moistureC, elevationC, heat, heatC;

    private static Corner[][] corners;

    private static Center[][] centers;

    public static final int XMESHES = 10, YMESHES = 10,  MESHSIZE = 64, MESHSIZEC = MESHSIZE-1;

    private static int meshCorners, meshCenters;

    private static Mesh[][] meshes;

    public World(Main main) {

        frame.getContentPane().setLayout(new FlowLayout());

        this.main = main;
        shader = main.getShader();
        camera = main.getCamera();

        biomeGenerator = new BiomeGenerator();

        meshCorners = MESHSIZE * MESHSIZE;
        meshCenters = (MESHSIZEC) * (MESHSIZEC);

        corners = new Corner[MESHSIZE*XMESHES][MESHSIZE*YMESHES];

        centers = new Center[MESHSIZE*XMESHES-1][MESHSIZE*YMESHES-1];

        meshes = new Mesh[XMESHES][YMESHES];
        
        buildNoise();
        for (int i = 0; i < YMESHES; i++) {
            for (int j = 0; j < XMESHES; j++) {
                meshes[j][i] = new Mesh(corners, centers, j, i);

            }
        }

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private static void buildNoise() {

        Random r = new Random();

        simplexNoiseGenerator = new SimplexNoiseGenerator(XMESHES*MESHSIZE , YMESHES*MESHSIZE);

        elevation = simplexNoiseGenerator.generateNoise(r.nextInt(), true);
        moisture = simplexNoiseGenerator.generateNoiseM(r.nextInt(), true);
        heat = simplexNoiseGenerator.generateNoiseH(r.nextInt(), true);

        elevationC = new float[MESHSIZE*XMESHES-1][MESHSIZE*XMESHES-1];
        moistureC = new float[MESHSIZE*XMESHES-1][MESHSIZE*XMESHES-1];
        heatC = new float[MESHSIZE*XMESHES-1][MESHSIZE*XMESHES-1];

       makeImage(elevation, true);
       makeImage(moisture, true);
       makeImage(heat, true);

        for (int i = 0; i < centers.length; i++) {
            for (int j = 0; j < centers[i].length; j++) {
                elevationC[i][j] = average(elevation[i][j], elevation[i+1][j], elevation[i][j+1], elevation[i+1][j+1]);
                moistureC[i][j] = average(moisture[i][j], moisture[i+1][j], moisture[i][j+1], moisture[i+1][j+1]);
                heatC[i][j] = average(heat[i][j], heat[i+1][j], heat[i][j+1], heat[i+1][j+1]);
            }
        }
        for (int i = 0; i < corners.length; i++) {
            for (int j = 0; j < corners[i].length; j++) {
                corners[i][j] = new Corner();
                corners[i][j].setElevation(biomeGenerator.smoothElevation(elevation[i][j]));
                corners[i][j].setMoisture(moisture[i][j]);
                corners[i][j].setHeat(heat[i][j]);

                corners[i][j].x = i;
                corners[i][j].y = j;

            }
        }

        for (int i = 0; i < centers.length; i++) {
            for (int j = 0; j < centers[i].length; j++) {
                centers[i][j] = new Center();
                centers[i][j].setElevation(biomeGenerator.smoothElevation(elevationC[i][j]));
                centers[i][j].setMoisture(moistureC[i][j]);
                centers[i][j].setHeat(heat[i][j]);

                centers[i][j].x = i;
                centers[i][j].y = j;
            }
        }

        for (int i = 0; i < corners.length; i++) {
            for (int j = 0; j < corners[i].length; j++) {

                if (j > 0)
                    corners[i][j].setNorth(corners[i][j-1]);
                if (i < corners.length-1)
                    corners[i][j].setEast(corners[i+1][j]);
                if (j < corners[i].length-1)
                    corners[i][j].setSouth(corners[i][j+1]);
                if (i > 0)
                    corners[i][j].setWest(corners[i-1][j]);

                if (i > 0 && j > 0)
                    corners[i][j].setNw(centers[i-1][j-1]);
                if (i < centers.length-1 && j > 0)
                    corners[i][j].setNe(centers[i][j-1]);
                if (i > 0 && j < centers[0].length-1)
                    corners[i][j].setSw(centers[i-1][j]);
                if (i < centers.length-1 && j < centers[0].length-1)
                    corners[i][j].setSe(centers[i][j]);

            }
        }

        for (int i = 0; i < centers.length; i++) {
            for (int j = 0; j < centers[i].length; j++) {
                if (j > 0)
                    centers[i][j].setNorth(centers[i][j-1]);
                if (i < centers.length-1)
                    centers[i][j].setEast(centers[i+1][j]);
                if (j < centers[i].length-1)
                    centers[i][j].setSouth(centers[i][j+1]);
                if (i > 0)
                    centers[i][j].setWest(centers[i-1][j]);

                centers[i][j].setNw(corners[i][j]);
                centers[i][j].setNe(corners[i+1][j]);
                centers[i][j].setSw(corners[i][j+1]);
                centers[i][j].setSe(corners[i+1][j+1]);


            }
        }

        for (int i = 0; i < 1000; i++) {
            Random random = new Random();
            ArrayList<MapCoord> riverList = new ArrayList();

            centers[random.nextInt(centers.length)][random.nextInt(centers.length)].createRiver(false, riverList);
        }

       for (int i = 0; i < corners.length; i++) {
            for (int j = 0; j < corners[i].length; j++) {
                corners[i][j].setBiome(biomeGenerator.generateBiome(corners[i][j].getElevation(),
                        corners[i][j].getMoisture(), corners[i][j].getHeat(), corners[i][j].isLake(), corners[i][j].isRiver()));
            }
        }

        for (int i = 0; i < centers.length; i++) {
            for (int j = 0; j < centers[i].length; j++) {

                 centers[i][j].setBiome(biomeGenerator.generateBiome(centers[i][j].getElevation(),
                        centers[i][j].getMoisture(), centers[i][j].getHeat(), centers[i][j].isLake(), centers[i][j].isRiver()));
            }
        }
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

    public static void rebuild() {
        frame.getContentPane().removeAll();
        buildNoise();
        for (int i = 0; i < YMESHES; i++) {
            for (int j = 0; j < XMESHES; j++) {
                //meshes[i][j] = buildMesh(i, j);
                meshes[i][j] = new Mesh(corners, centers, i, j);
            }
        }
    }


    public static void update() {
        shader.bind();
    }

    public static void render(){

        for (int i = 0; i < XMESHES; i++) {
            for (int j = 0; j < YMESHES; j++) {
                shader.bind();

                Matrix4f projection = new Matrix4f();
                Matrix4f rotation = new Matrix4f();
                Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(MESHSIZEC*i, MESHSIZEC*j, 0));
                camera.getProjection().mul(tile_pos, projection);
                camera.getRotation().mul(tile_pos, rotation);

                shader.setUniform("projectionMatrix", projection);
                shader.setUniform("rotationMatrix", rotation);
                meshes[i][j].render();
            }
        }
        shader.setUniform("projectionMatrix", camera.getProjection());
    }

    public static Corner[][] getCorners() {
        return corners;
    }

    public static Center[][] getCenters() {
        return centers;
    }
}

