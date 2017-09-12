package Gui;

import Main.Main;

import java.io.BufferedReader;
import Main.Texture;
import Main.Loader;
import org.joml.Vector4i;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Hashtable;

public class Font {

    private static final int X = 0;
    private static final int Y = 1;
    private static final int WIDTH = 2;
    private static final int HEIGHT = 3;
    private static final int XOFFSET = 4;
    private static final int YOFFSET = 5;
    private static final int XADVANCE = 6;

    private Hashtable<Integer, int[]> fontData = new Hashtable<>();
    private float pixelToScreenCoordRatioX;
    private float pixelToScreenCoordRatioY;
    private int lineHeight = 98;
    private float lineHeightScreenSpace = 0.04f;
    private float space = 0.001f;
    private float texSizeX = 512;
    private float texSizeY = 512;
    private int padding = 0;
    private Texture fontTexture;

    public Font(String filename) {
        pixelToScreenCoordRatioY = lineHeight / lineHeightScreenSpace;
        pixelToScreenCoordRatioX = pixelToScreenCoordRatioY;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("res/fonts/"+filename+".fnt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = " ";
        try {
            while (line != null) {
                if (line.startsWith("char id")) {
                    line = line.replaceAll("[^0-9- ]", "");
                    line = line.replaceAll("\\s+", " ").trim();
                    String[] token = line.split(" ");
                    int[] data = new int[7];
                    int charID = Integer.parseInt(token[0]);
                    for (int i = 1; i < 8; i++) {
                        token[i] = token[i].trim();
                        data[i - 1] = Integer.parseInt(token[i]);
                    }
                    fontData.put(charID, data);
                }
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Loader loader = new Loader();
        fontTexture = loader.createTextureFromImageFile("fonts/" + filename + ".png", 512, 512);
    }

    public float getX(int charID) {
        return (float) (fontData.get(charID)[X] - padding) / texSizeX;
    }

    public float getY(int charID) {
        return (float) (fontData.get(charID)[Y] - padding) / texSizeY;
    }

    public float getX2(int charID) {
        return (float) (fontData.get(charID)[X] + fontData.get(charID)[WIDTH] + padding) / texSizeX;
    }

    public float getY2(int charID) {
        return (float) (fontData.get(charID)[Y]  + fontData.get(charID)[HEIGHT] + padding) / texSizeY;
    }

    public float getWidthInScreenCoords(int charID, float fontSize) {
        return fontData.get(charID)[WIDTH] * fontSize / pixelToScreenCoordRatioX;
    }

    public float getHeightInScreenCoords(int charID, float fontSize) {
        return fontData.get(charID)[HEIGHT] * fontSize / pixelToScreenCoordRatioY;
    }

    public float getXOffset(int charID, float fontSize) {
        return fontData.get(charID)[XOFFSET] * fontSize / pixelToScreenCoordRatioX;
    }

    public float getYOffset(int charID, float fontSize) {
        return fontData.get(charID)[YOFFSET] * fontSize / pixelToScreenCoordRatioY;
    }

    public float getXAdvance(int charID, float fontSize) {
        return fontData.get(charID)[XADVANCE] * fontSize / pixelToScreenCoordRatioX;
    }

    public float getXAdvance(String word, float fontSize) {
        char[] chars = word.toCharArray();
        float xAdvance = 0;
        for (int i = 0; i < chars.length; i++) {
            xAdvance += getXAdvance(chars[i], fontSize);
        }
        return xAdvance;
    }

    public float getLineHeight(float fontSize) {
        return lineHeightScreenSpace * fontSize;
    }

    public float getSpace(float fontSize) {
        return fontData.get(32)[XADVANCE] * fontSize / pixelToScreenCoordRatioX;
    }

    public Texture getFontTexture() {
        return fontTexture;
    }

}
