package Utils;

import java.util.Random;

public class ArrayManipulation {

    public static float[][] gaussianBlur2Df(float[][] array) {
        float[][] tempArray = array;
        for (int i = 1; i < tempArray.length-1; i++) {
            for (int j = 1; j < tempArray.length-1; j++) {
                array[i][j] = (
                        tempArray[i+1][j+1] +
                        tempArray[i-1][j-1] +
                        tempArray[i-1][j+1] +
                        tempArray[i+1][j-1] +
                        tempArray[i+1][j] +
                        tempArray[i-1][j] +
                        tempArray[i][j+1] +
                        tempArray[i][j-1] +
                        tempArray[i][j] * 4
                ) / 12;
            }
        }
        return array;
    }

    public static int[][] gaussianBlur2Di(int[][] array) {

        int[][] tempArray = array;

        for (int i = 1; i < tempArray.length-1; i++) {
            for (int j = 1; j < tempArray[i].length-1; j++) {

                array[i][j] = (
                    tempArray[i-1][j-1] +   tempArray[i][j-1] + tempArray[i+1][j-1] +
                    tempArray[i-1][j] +     tempArray[i][j] +   tempArray[i+1][j] +
                    tempArray[i-1][j+1] +   tempArray[i][j+1] + tempArray[i+1][j+1])
                    /9;
            }
        }
        return array;
    }

    public static float[][] convolutionFilterf(float[][] array, float[] kernel) {

        float[][] tempArray = array;

        float x = (float) (Math.random());

        for (int i = 1; i < tempArray.length-1; i++) {
            for (int j = 1; j < tempArray[i].length-1; j++) {
                if (array[i][j] < 0.9f)
                    tempArray[i][j] += x *(array[i-1][j-1] * kernel[0] + array[i][j-1] * kernel[1] + array[i+1][j-1] * kernel[2] +
                                            array[i-1][j] * kernel[3] + array[i][j] * kernel[4] + array[i+1][j] * kernel[5] +
                                            array[i-1][j+1] * kernel[6] + array[i][j+1] * kernel[7] + array[i+1][j+1] * kernel[8])
                                            / (kernel[0]+kernel[1]+kernel[2]+kernel[3]+kernel[4]+kernel[5]+kernel[6]+kernel[7]+kernel[8]);
            }
        }
        return tempArray;
    }

    public static float[][] clampf(float[][] array, float max, float min) {


        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] > max) array[i][j] = max;
                if (array[i][j] < min) array[i][j] = min;
            }

        }
        return array;
    }



}
