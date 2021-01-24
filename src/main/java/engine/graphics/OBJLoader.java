package engine.graphics;

import engine.models.MeshModel;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    // TODO: TOO LONG need some refactor in here
    public static MeshModel loadObjModel(String fileName, Loader loader) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(OBJLoader.class.getResourceAsStream("/objects/" + fileName + ".obj"));
        } catch (Exception e) {
            System.err.println("The file " + fileName + ".obj wasn't found");
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;
        try {
            while(true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    vertices.add(vertex);
                } else if(line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2])
                    );
                    textures.add(texture);
                } else if(line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    normals.add(normal);
                } else if(line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while(line != null) { // TODO: try to integrate that loop to the first one...
                 if(!line.startsWith("f ")) {
                     line = reader.readLine();
                     continue;
                 }
                 String[] currentLine = line.split(" ");
                 String[] vertex1 = currentLine[1].split("/");
                 String[] vertex2 = currentLine[2].split("/");
                 String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for(int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray, texturesArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
                                      List<Vector3f> normals, float[] texturesArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentText  = textures.get(Integer.parseInt(vertexData[1]) - 1);
        texturesArray[currentVertexPointer * 2] = currentText.x;
        texturesArray[currentVertexPointer * 2 + 1] = 1 - currentText.y; // Because openGL start top left
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }
}
