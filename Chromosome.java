import java.util.Random;

public class Chromosome {
    public int[][] grid;
    public int[] gridArray;
    public int fitnessValue;
    public boolean isBest;
    //private int[][] helpGrid;
    /* Constructors */
    public Chromosome() {
        
    }

    public Chromosome(Chromosome c) {
        this.gridArray = c.gridArray.clone();
        grid = new int[9][9];
        convertToGrid();
        this.calculateFitness();
    }

    public Chromosome(int[][] g) {
        this.grid = g;
        gridArray = new int[81];
        convertToArray();
    }

    /* Class methods */
    public int[][] getBoard() {
        return grid;
    }

    public int getFitnessValue() {
        return this.fitnessValue;
    }

    public int[] getGridArray() {
        return gridArray;
    }

    public void setGridArray(int[] arr) {
        this.gridArray = arr.clone();

        convertToGrid();
    }

    public void calculateFitness() {
        convertToGrid();
        fitnessValue = 0;
        int[] setA = {1,2,3,4,5,6,7,8,9};
        //for each row
        for(int i=0; i < 9; i++) {
            for(int k=0; k < setA.length; k++) {
                if(!contains(grid[i], setA[k])) {
                    fitnessValue++;
                }
                int occur = numberOfOccurences(grid[i], setA[k]);
                if(occur > 1) {
                    fitnessValue += occur-1;
                }
            }
        }
        //for each column
        for(int j=0; j < 9; j++) {
            for(int k=0; k < setA.length; k++) {
                if(!contains(getColumn(grid, j), setA[j])) {
                    fitnessValue++;
                }
                int occur = numberOfOccurences(getColumn(grid, j), setA[k]);
                if(occur > 1) {
                    fitnessValue += occur-1;
                }
            }
        }
    }

    public void mutate(int[][] help) {
        int mutationSeq = generateRandomNumber(5)+1;
        //System.out.println(mutationSeq);
        int mutateCounter = 0;
        int count=0;
        int[][] seqGrid = new int[9][9];
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                seqGrid[i][j] = gridArray[count++];
            }
        }
        int[] prev = new int[gridArray.length];
        prev = gridArray.clone();
        while(isEqual(prev, gridArray) && mutateCounter < 2) {
            for(int i=0; i<mutationSeq; i++) {
                int randBlock = generateRandomNumber(9);
                int ipos = generateRandomNumber(9);
                int jpos = generateRandomNumber(9);
                prev = seqGrid[randBlock].clone();
                //System.out.println(randBlock + " " + ipos + " " + jpos);
                //System.out.println(Arrays.toString(seqGrid[randBlock]));
                //System.out.println(Arrays.toString(help[randBlock]));
                if(isLegal(help[randBlock], ipos) && isLegal(help[randBlock], jpos)) {
                    //seqGrid[randBlock] = swap(seqGrid[randBlock],ipos,jpos).clone();
                    int col = getCol(randBlock, ipos);
                    int row = getRow(randBlock, ipos);
                    int ioccur = numberOfOccurences(grid[row], seqGrid[randBlock][jpos]);
                    int joccur = numberOfOccurences(getColumn(grid, col), seqGrid[randBlock][jpos]);
                    col = getCol(randBlock, jpos);
                    row = getRow(randBlock, jpos);
                    int ioccur1 = numberOfOccurences(grid[row], seqGrid[randBlock][ipos]);
                    int joccur1 = numberOfOccurences(getColumn(grid, col), seqGrid[randBlock][ipos]);
                    if((ioccur <= 3 && joccur <= 3) && (ioccur1 <= 3 && joccur1 <= 3)){
                        seqGrid[randBlock] = swap(seqGrid[randBlock],ipos,jpos).clone();
                    }
                    else {
                        break;
                    }
                }
                else {
                    break;
                }
            }
            mutateCounter++;
        }
        
        count=0; 
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                gridArray[count++] = seqGrid[i][j];
            }
        }

        convertToGrid();
    }

    private int[] swap(int[] array, int i, int j) {
        int temp;
        temp = array[i];
        array[i] = array[j]; 
        array[j] = temp;

        return array;
    }

    private boolean isLegal(int[] help, int pos) {
        if(help[pos] == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isEqual(int[] a, int[] b) {
        boolean flag = false;
        for(int i=0; i < a.length; i++) {
            if(a[i] == b[i]) {
                flag = true;
            }
            else {
                flag = false;
            }
        }

        return flag;
    }

    private boolean contains(int[] a, int num) {
        for(int i=0; i < a.length; i++) {
            if(a[i] == num) {
                return true;
            }
        }

        return false;
    }

    private int numberOfOccurences(int[] a, int num) {
        int app = 0;
        for(int i=0; i < a.length; i++) {
            if(a[i] == num) {
                app++;
            }
        }

        return app;
    }

    private int[] getColumn(int[][] array, int index){
        int[] column = new int[9];
        for(int i=0; i<column.length; i++){
           column[i] = array[i][index];
        }
        return column;
    }

    private int generateRandomNumber(int max) {
        Random rand = new Random();

        int num = rand.nextInt(max);

        return num;
    }
//====================================================================
/*  
    If you are looking at the code then please do not judge any of 
    the functions below as this was done late at night(and i couldn't think of any other alternate method) 
    and I just wasn't bothered to change or redo them. 
    Thank you.

    This was the only way I could make my mutation work.
*/
    public void convertToArray() {
        int count = 0;
        int i=0;
        //--------------------------------------------------
        for(i = 0; i <= 2; i++) {
            for(int j=0; j <= 2; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }   
        for(i = 0; i <= 2; i++) {
            for(int j=3; j <= 5; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }
        for(i = 0; i <= 2; i++) {
            for(int j=6; j <= 8; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }
        //--------------------------------------------------
        for(i = 3; i <= 5; i++) {
            for(int j=0; j <= 2; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }
        for(i = 3; i <= 5; i++) {
            for(int j=3; j <= 5; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }
        for(i = 3; i <= 5; i++) {
            for(int j=6; j <= 8; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }
        //--------------------------------------------------
        for(i = 6; i <= 8; i++) {
            for(int j=0; j <= 2; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }
        for(i = 6; i <= 8; i++) {
            for(int j=3; j <= 5; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }
        for(i = 6; i <= 8; i++) {
            for(int j=6; j <= 8; j++) {
                gridArray[count++] = grid[i][j];
            }  
        }
        //--------------------------------------------------
        //System.out.println(count);
    }

    public void convertToGrid() {
        int count = 0;
    
        int i=0;
        //--------------------------------------------------
        for(i = 0; i <= 2; i++) {
            for(int j=0; j <= 2; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }   
        for(i = 0; i <= 2; i++) {
            for(int j=3; j <= 5; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }
        for(i = 0; i <= 2; i++) {
            for(int j=6; j <= 8; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }
        //--------------------------------------------------
        for(i = 3; i <= 5; i++) {
            for(int j=0; j <= 2; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }
        for(i = 3; i <= 5; i++) {
            for(int j=3; j <= 5; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }
        for(i = 3; i <= 5; i++) {
            for(int j=6; j <= 8; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }
        //--------------------------------------------------
        for(i = 6; i <= 8; i++) {
            for(int j=0; j <= 2; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }
        for(i = 6; i <= 8; i++) {
            for(int j=3; j <= 5; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }
        for(i = 6; i <= 8; i++) {
            for(int j=6; j <= 8; j++) {
                grid[i][j] = gridArray[count++];
            }  
        }
        //--------------------------------------------------
        //System.out.println(count);
    }
    
    private int getRow(int block, int no) {
        int num=0;
        switch (block) {
            case 0:
                if(no == 0 || no == 1 || no == 2) {
                    num = 0;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 1;
                }
                else {
                    num = 2;
                }
                break;

            case 1:
                if(no == 0 || no == 1 || no == 2) {
                    num = 0;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 1;
                }
                else {
                    num = 2;
                }
                break;

            case 2:
                if(no == 0 || no == 1 || no == 2) {
                    num = 0;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 1;
                }
                else {
                    num = 2;
                }
                break;

            case 3:
                if(no == 0 || no == 1 || no == 2) {
                    num = 3;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 4;
                }
                else {
                    num = 5;
                }
                break;

            case 4:
                if(no == 0 || no == 1 || no == 2) {
                    num = 3;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 4;
                }
                else {
                    num = 5;
                }
                break;

            case 5:
                if(no == 0 || no == 1 || no == 2) {
                    num = 3;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 4;
                }
                else {
                    num = 5;
                }
                break;

            case 6:
                if(no == 0 || no == 1 || no == 2) {
                    num = 6;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 7;
                }
                else {
                    num = 8;
                }
                break;

            case 7:
                if(no == 0 || no == 1 || no == 2) {
                    num = 6;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 7;
                }
                else {
                    num = 8;
                }
                break;

            case 8:
                if(no == 0 || no == 1 || no == 2) {
                    num = 6;
                }
                else if(no == 3 || no == 4 || no == 5) {
                    num = 7;
                }
                else {
                    num = 8;
                }
                break;
        }

        return num;
    }

    private int getCol(int block, int no) {
        int num=0;
        switch (block) {
            case 0:
                if(no == 0 || no == 3 || no == 6) {
                    num = 0;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 1;
                }
                else {
                    num = 2;
                }
                break;

            case 1:
                if(no == 0 || no == 3 || no == 6) {
                    num = 3;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 4;
                }
                else {
                    num = 5;
                }
                break;

            case 2:
                if(no == 0 || no == 3 || no == 6) {
                    num = 6;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 7;
                }
                else {
                    num = 8;
                }
                break;

            case 3:
                if(no == 0 || no == 3 || no == 6) {
                    num = 0;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 1;
                }
                else {
                    num = 2;
                }
                break;

            case 4:
                if(no == 0 || no == 3 || no == 6) {
                    num = 3;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 4;
                }
                else {
                    num = 5;
                }
                break;

            case 5:
                if(no == 0 || no == 3 || no == 6) {
                    num = 6;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 7;
                }
                else {
                    num = 8;
                }
                break;

            case 6:
                if(no == 0 || no == 3 || no == 6) {
                    num = 0;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 1;
                }
                else {
                    num = 2;
                }
                break;

            case 7:
                if(no == 0 || no == 3 || no == 6) {
                    num = 3;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 4;
                }
                else {
                    num = 5;
                }
                break;

            case 8:
                if(no == 0 || no == 3 || no == 6) {
                    num = 6;
                }
                else if(no == 1 || no == 4 || no == 7) {
                    num = 7;
                }
                else {
                    num = 8;
                }
                break;
        }

        return num;
    }
}