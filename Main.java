import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner fileObj  = new Scanner(System.in);
        System.out.println("Enter the file name: ");


        String filename = fileObj.nextLine();
        fileObj.close();

        FileLoader file = new FileLoader(filename);

        try {
            file.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[][] gamespace = new int[9][9];

        gamespace = file.getFileData();
        printGrid(gamespace);
        System.out.println("Performing Genetic Algorithm...");
        GeneticAlgorithmClass A = new GeneticAlgorithmClass(gamespace);
        int gen = A.performGA();
        System.out.println("Final generation count: "+gen);
        
        System.out.println("============================================================================================");
        System.out.println("Solution:");
        printGrid(A.getSolutionBoard());

        //---------------------------------------------------------------------------------------------

    }

    public static void printGrid(int[][] data) {
        for(int i=0; i < 9; i++) {
            for(int j=0; j < 9; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println(" ");
        }        
    }
}