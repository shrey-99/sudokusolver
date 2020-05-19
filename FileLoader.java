import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileLoader {
    private String fileName;
    private int[][] matrix;

    public FileLoader(String name) {
        this.fileName = name;
    }

    public void readFile() throws java.io.IOException {
        try {
            File myFile = new File(fileName);
            System.out.println("Reading file from location: " + myFile.getCanonicalPath());
            Scanner myReader = new Scanner(myFile);
            
            System.out.println("Reading matrix data...");
            int i=0;
            matrix = new int[9][9];
            while(myReader.hasNextLine() && i < 9) {
                int k = 0;
                while(myReader.hasNext() && k < 9) {
                    String num = myReader.next();
                    matrix[i][k] = Integer.parseInt(num);
                    k++;
                }
                //System.out.println(" ");

                //String[] row = data.split("");
                //System.out.println(row.length);
                //int[] arr = new int[row.length];
                /*for(int j=0; j < arr.length; j++) {
                    arr[j] = Integer.parseInt(row[j]);
                }*/
                //myReader.nextLine();
                i++;
            }
            //System.out.println("output string: " + Arrays.toString(matrix[0]));
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public int[][] getFileData() {
        return matrix;
    }
}