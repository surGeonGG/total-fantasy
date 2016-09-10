import org.joml.Matrix4f;

public class Mesh {

    private Model model;

    private static BiomeGenerator biomeGenerator;

    private float[] vertices;

    private int[] indices;

    private float[] colors;

    private static Corner[][] corners;

    private static Center[][] centers;

    public static final int MESHSIZE = 64, MESHSIZEC = MESHSIZE-1, DEPTH = 0;

    public Mesh(Corner[][] corners, Center[][] centers, int x, int y) {

        this.corners = corners;
        this.centers = centers;

        biomeGenerator = new BiomeGenerator();

        vertices = new float[(MESHSIZE*MESHSIZE + MESHSIZEC*MESHSIZEC) * 3];
        indices = new int[MESHSIZEC*MESHSIZEC * 12];
        colors = new float[(MESHSIZE*MESHSIZE + MESHSIZEC*MESHSIZEC) * 4];

        model = buildMesh(x, y);


    }

    private Model buildMesh(int meshX, int meshY) {
        for (int i = 0; i < MESHSIZE; i++) {
            for (int j = 0; j < MESHSIZE; j++) {
                //Building vertices and color array for corners
                int count = (j * 3) + (i * MESHSIZE * 3);

                vertices[count] = j;
                vertices[count+1] = i;
                vertices[count+2] = corners[(meshX*MESHSIZEC)+i][(meshY*MESHSIZEC)+j].getElevation()*DEPTH;
                if (vertices[count+2] < 0) vertices[count+2] = 0f;

                count = (j * 4) + (i * MESHSIZE * 4);

                colors[count] = biomeGenerator.getBiomeColor(corners[(meshX*MESHSIZEC)+i][(meshY*MESHSIZEC)+j].getBiome())[0];
                colors[count+1] = biomeGenerator.getBiomeColor(corners[(meshX*MESHSIZEC)+i][(meshY*MESHSIZEC)+j].getBiome())[1];
                colors[count+2] = biomeGenerator.getBiomeColor(corners[(meshX*MESHSIZEC)+i][(meshY*MESHSIZEC)+j].getBiome())[2];
                colors[count+3] = 1;


            }
        }

        for (int i = 0; i < MESHSIZEC; i++) {
            for (int j = 0; j < MESHSIZEC; j++) {
                //Building vertices and color array for centers
                int l = (j * 3) + (i * (MESHSIZEC) * 3);
                int count = (MESHSIZE*MESHSIZE * 3) + l;

                vertices[count] = 0.5f + j;
                vertices[count+1] = 0.5f + i;
                vertices[count+2] = centers[meshX*MESHSIZEC+i][meshY*MESHSIZEC+j].getElevation()*DEPTH;

                if (vertices[count+2] < 0) vertices[count+2] = 0f;

                l = (j * 4) + (i * (MESHSIZEC) * 4);
                count = (MESHSIZE*MESHSIZE * 4) + l;

                colors[count] = biomeGenerator.getBiomeColor(centers[meshX*MESHSIZEC+i][meshY*MESHSIZEC+j].getBiome())[0];
                colors[count+1] = biomeGenerator.getBiomeColor(centers[meshX*MESHSIZEC+i][meshY*MESHSIZEC+j].getBiome())[1];
                colors[count+2] = biomeGenerator.getBiomeColor(centers[meshX*MESHSIZEC+i][meshY*MESHSIZEC+j].getBiome())[2];
                colors[count+3] = 1;
            }
        }

        for (int i = 0; i < MESHSIZEC; i++) {
            for (int j = 0; j < MESHSIZEC; j++) {

                int upperLeftLocation = (i*MESHSIZE)+j;
                int centerLocation = MESHSIZE*MESHSIZE + (i*MESHSIZEC)+j;
                int nextRow = MESHSIZE;
                int count = (j * 12) + (i * (MESHSIZEC * 12));
                //upper triangle
                indices[count] = upperLeftLocation;
                indices[count+1] = upperLeftLocation + 1;
                indices[count+2] = centerLocation;
                //left triangle
                indices[count+3] = (upperLeftLocation);
                indices[count+4] = (upperLeftLocation + nextRow);
                indices[count+5] = centerLocation;
                //lower triangle
                indices[count+6] = (upperLeftLocation + nextRow);
                indices[count+7] = (upperLeftLocation + nextRow) + 1;
                indices[count+8] = centerLocation;
                //right triangle
                indices[count+9] = upperLeftLocation + 1;
                indices[count+10] = (upperLeftLocation + nextRow) + 1;
                indices[count+11] = centerLocation;
            }
        }

        return new Model(vertices, indices, colors);
    }

    public void render() {
        model.render();
    }
}
