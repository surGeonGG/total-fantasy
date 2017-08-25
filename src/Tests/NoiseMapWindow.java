package Tests;

import Main.SimplexNoiseGenerator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;



public class NoiseMapWindow {

    JFrame frame = new JFrame();
    float freq = 3;
    float weight = 1;
    int seed;
    BufferedImage image;
    JLabel jlabel;

    public NoiseMapWindow() {
        Random random = new Random();
        seed = random.nextInt();

        frame = new JFrame();
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("noise map window");
        frame.setLayout(new FlowLayout());
        jlabel = new JLabel();
        frame.add(jlabel);
        frame.setVisible(true);
    }

    private float[] collapseArray(float[][] map) {
        float[] arr = new float[map.length * map[0].length * 3];
        int c = 0;
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                arr[c++] = map[j][i]*255;
                arr[c++] = map[j][i]*255;
                arr[c++] = map[j][i]*255;
            }
        }
        return arr;
    }

    public ImageIcon loadNoiseMap(float[][] map) {
        SimplexNoiseGenerator simplexNoiseGenerator = new SimplexNoiseGenerator();
        image = new BufferedImage(200, 150, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        float[] pixels = collapseArray(map);
        raster.setPixels(0, 0, map.length, map[0].length, pixels);
        image.setData(raster);
        return new ImageIcon(image);
    }

    public void loadImage(ImageIcon imageIcon) {
        jlabel.setIcon(imageIcon);
    }

}
