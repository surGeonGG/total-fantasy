import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkPanel;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static org.lwjgl.nuklear.Nuklear.*;


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

    private static Player player;

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
                /*meshes[i][j] = buildMesh(i, j);*/
                meshes[i][j] = new Mesh(corners, centers, i, j);

            }
        }

        player = new Player(new Vector3f(300,300,0), camera, shader);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        
    }

    private static void buildNoise() {

        Random r = new Random();

        simplexNoiseGenerator = new SimplexNoiseGenerator(XMESHES*MESHSIZE , YMESHES*MESHSIZE);

        elevation = simplexNoiseGenerator.generateNoise(r.nextInt(), false);
        moisture = simplexNoiseGenerator.generateNoiseM(r.nextInt(), true);
        heat = simplexNoiseGenerator.generateNoiseH(r.nextInt(), true);

        elevationC = new float[MESHSIZE*XMESHES-1][MESHSIZE*XMESHES-1];
        moistureC = new float[MESHSIZE*XMESHES-1][MESHSIZE*XMESHES-1];
        heatC = new float[MESHSIZE*XMESHES-1][MESHSIZE*XMESHES-1];

       // makeImage(elevation, false);
        //makeImage(moisture, true);
       // makeImage(heat, true);

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
                corners[i][j].setElevation(elevation[i][j]);
                corners[i][j].setMoisture(moisture[i][j]);
                corners[i][j].setHeat(heat[i][j]);
                corners[i][j].setBiome(biomeGenerator.generateBiome(corners[i][j].getElevation(), corners[i][j].getMoisture(), corners[i][j].getHeat(), false, false));
            }
        }
        for (int i = 0; i < centers.length; i++) {
            for (int j = 0; j < centers[i].length; j++) {
                centers[i][j] = new Center();
                centers[i][j].setElevation(elevationC[i][j]);
                centers[i][j].setMoisture(moistureC[i][j]);
                centers[i][j].setHeat(heat[i][j]);
                centers[i][j].setBiome(biomeGenerator.generateBiome(centers[i][j].getElevation(), centers[i][j].getMoisture(), centers[i][j].getHeat(), false, false));
            }
        }
    }

    private static float average(float ul, float ur, float ll, float lr) {
        return (ul+ur+ll+lr)/4;
    }

    private static void makeImage(float[][] map, boolean isClamped) {

        BufferedImage img = new BufferedImage(map.length, map[1].length, BufferedImage.TYPE_INT_RGB);

        float[][] tempMap = new float[map.length][map[1].length];

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
                Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(MESHSIZEC*j, MESHSIZEC*i, 0));
                camera.getProjection().mul(tile_pos, projection);

                shader.setUniform("projectionMatrix", projection);
                meshes[i][j].render();

            }
        }
        shader.setUniform("projectionMatrix", camera.getProjection());
        //borders.renderBorders();

        player.render();
    }

    public static Player getPlayer() {
        return player;
    }
}

