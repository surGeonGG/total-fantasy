package Tests;

import Main.SimplexNoiseGenerator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

public class NoiseTest {

    JFrame frame = new JFrame();
    float freq = 3;
    float weight = 1;
    int seed;
    BufferedImage image;
    JLabel jlabel;

    public static void main(String[] args) {
        new NoiseTest();
    }

    public NoiseTest() {
        Random random = new Random();
        seed = random.nextInt();

        frame = new JFrame();
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("noise map tester");
        frame.setLayout(new FlowLayout());
        jlabel = new JLabel(loadNoiseMap());
        frame.add(jlabel);
        JSlider freqSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 3);
        JSlider weightSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
        freqSlider.setMajorTickSpacing(1);
        freqSlider.setMinorTickSpacing(1);
        weightSlider.setMajorTickSpacing(1);
        weightSlider.setMinorTickSpacing(1);

        freqSlider.setPaintTicks(true);
        weightSlider.setPaintTicks(true);
        frame.add(freqSlider);
        frame.add(weightSlider);
        freqSlider.addChangeListener((ChangeEvent changeEvent) -> {
                freq = (float) freqSlider.getValue() / 10;
                reload();
        });
        weightSlider.addChangeListener((ChangeEvent changeEvent) -> {
                weight = (float) weightSlider.getValue() / 10;
                reload();

        });
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

    private ImageIcon loadNoiseMap() {
        SimplexNoiseGenerator simplexNoiseGenerator = new SimplexNoiseGenerator();
        image = new BufferedImage(200, 150, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        float[] pixels = collapseArray(simplexNoiseGenerator.generateIslandShape(seed, 200, 150));
        raster.setPixels(0, 0, 200, 150, pixels);
        image.setData(raster);
        return new ImageIcon(image);
    }



    private void reload() {
        jlabel.setIcon(loadNoiseMap());
    }
}
