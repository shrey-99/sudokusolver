import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithmClass {
    private int[][] initialBoard;
    private int[][] solutionBoard;
    private int[][] helpBoard;
    private Chromosome[] initialPopulation;
    private Chromosome[] population;
    
    private int generation;
    private int populationSize = 11;

    public GeneticAlgorithmClass() {
        /* Default Constructor */ 

    }
    public GeneticAlgorithmClass(int[][] g) {
        this.generation = 0;
        this.initialBoard = g;
    }

    public void setInitialBoard(int[][] b) {
        this.initialBoard = b;
    }

    public int[][] getInitialBoard() {
        return initialBoard;
    }
    
    public void printGrid(int[][] data) {
        for(int i=0; i < 9; i++) {
            System.out.println(Arrays.toString(data[i]));
        }        
    }

    public int performGA() {
        //System.out.println("Creating initial population...");
        setHelpBoard(convertToArray(initialBoard));
        createInitialPopulation();
        int bestFitness=255;
        while(bestFitness != 0) {
        	if(generation % 150000 == 0) {
        		createInitialPopulation();
        	}
            calculateFitnessess();
            System.out.println("generation: "+generation);
            for(int i=0; i<populationSize; i++) {
                System.out.println(population[i].getFitnessValue());
                if(population[i].getFitnessValue() == 0) {
                    System.out.println("solution found");
                    solutionBoard = population[i].getBoard();
                    bestFitness = population[i].getFitnessValue();
                }
            }
        	selectionProcess();
            mutateAll();
            generation++;
        }
            
        calculateFitnessess();
        return generation;
    }

    public void calculateFitnessess() {
        for(int i=0; i<population.length; i++) {
            population[i].calculateFitness();
        }
    }

    private void selectionProcess() {
        Chromosome[] newPopulation = new Chromosome[populationSize];

        int minIndex = bestOfPopulation(population);
        System.out.println("best fitness: "+population[minIndex].getFitnessValue());
        newPopulation[0] = new Chromosome(population[minIndex]);
        newPopulation[0].isBest = true;
        //newPopulation[0].calculateFitness();
        
        for(int i=1; i<newPopulation.length; i++) {
            //take two randomly generated numbers and indexes for two chromosomes and take the best of the two (i.e the individual with the lower fitness value)
            int rIndex1 = generateRandomNumber(populationSize);
            int rIndex2 = generateRandomNumber(populationSize);
            while(rIndex1 == rIndex2) {
                rIndex1 = generateRandomNumber(populationSize);
                rIndex2 = generateRandomNumber(populationSize);
            }
            //System.out.println(rIndex1 + " " + rIndex2);
            newPopulation[i] = crossover(population[rIndex1], population[rIndex2]);
        }
       
        /*for(int i=0; i<population.length; i++) {
            population[i] = new Chromosome(newPopulation[i]);
        }*/
        population = newPopulation.clone();
    }

    public Chromosome crossover(Chromosome a, Chromosome b) { 
        int[] arrA = a.getGridArray().clone();
        int[] arrB = b.getGridArray().clone();
        int[][] grid1 = ArrayToSeqGrid(arrA);
        int[][] grid2 = ArrayToSeqGrid(arrB);


        int pos = generateRandomNumber(9);
        //System.out.println(pos);
        int[] temp = new int[9];
        for(int i=pos; i < 9; i++) {
            temp = grid1[i].clone();
            grid1[i] = grid2[i].clone();
            grid2[i] = temp.clone();
        }
        arrA = SeqGridToArray(grid1);
        arrB = SeqGridToArray(grid2);
        a.setGridArray(arrA);
        b.setGridArray(arrB);
        a.calculateFitness();
        b.calculateFitness();
        if(a.getFitnessValue() < b.getFitnessValue()) {
            Chromosome newbaby = new Chromosome(a);
            return newbaby;
        }
        else {
            Chromosome newbaby = new Chromosome(b);
            return newbaby;
        }
        
    }

    public void mutateAll() {
        for(int i=1; i<population.length; i++) {
            population[i].mutate(helpBoard);
        }
    }

    //get the best fitness value of the current population
    protected int bestOfPopulation(Chromosome[] pop) {
        int min = pop[0].getFitnessValue();
        int minIndex = 0;
        
        for(int i=0; i<pop.length; i++) {
            if(pop[i].getFitnessValue() < min) {
                min = pop[i].getFitnessValue();
                minIndex = i;
            }
        }

        return minIndex;
    }

    public void createInitialPopulation() {
        population = new Chromosome[populationSize];
        int[][] seqGrid = new int[9][9];
        int count = 0;
        int[] arr = convertToArray(initialBoard);
        for(int num=0; num<populationSize; num++) {
            count = 0;
            arr = convertToArray(initialBoard);

            seqGrid = ArrayToSeqGrid(arr);
            count = 0;
            for(int i=0; i<9; i++) {
            //System.out.println(Arrays.toString(seqGrid[i]));
            for(int j=0; j<9; j++) {
                if (seqGrid[i][j] == 0) {
                    int randnum = generateRandomNumber();
                    while(contains(seqGrid[i], randnum)) {
                        randnum = generateRandomNumber();
                    }
                    seqGrid[i][j] = randnum;
                }

                arr[count++] = seqGrid[i][j];
            }
            }
            
            population[num] = new Chromosome(convertToGrid(arr));
            population[num].calculateFitness();
        }
    }

    public Chromosome[] getInitialPopulation() {
        return initialPopulation;
    }

    /**
     * @return the population
     */
    public Chromosome[] getPopulation() {
        return population;
    }

    public int[][] getSolutionBoard() {
        return solutionBoard;
    }

    private int generateRandomNumber() {
        Random rand = new Random();

        int num = rand.nextInt(10);

        return num;
    }

    private int generateRandomNumber(int max) {
        Random rand = new Random();

        int num = rand.nextInt(max);

        return num;
    }

    private boolean contains(int[] a, int num) {
        for(int i=0; i < a.length; i++) {
            if(a[i] == num) {
                return true;
            }
        }

        return false;
    }

    /*private Chromosome[] createNewPopulation() {
        Chromosome[] population = new Chromosome[populationSize];

        for(int i=0; i<populationSize; i++) {
            population[i] = new Chromosome();
        }

        return population;
    }*/

    private void setHelpBoard(int[] arr) {
        int count=0;
        helpBoard = new int[9][9];
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                helpBoard[i][j] = arr[count++];
            }
        }
    }

    protected Chromosome tournamentSelection(Chromosome a, Chromosome b) {
        if(a.getFitnessValue() < b.getFitnessValue()) {
            return a;
        }
        else {
            return b;
        }
    }

    private int[][] ArrayToSeqGrid (int[] arr) {
        int[][] seqGrid = new int[9][9];
        int count=0;
        for(int i=0;  i<9; i++) {
            for(int j=0; j<9; j++) {
                seqGrid[i][j] = arr[count++];
            }
        }

        return seqGrid;
    }   

    private int[] SeqGridToArray(int[][] sGrid) {
        int[] array= new int[9*9];
        int count=0;
        for(int i=0;  i<9; i++) {
            for(int j=0; j<9; j++) {
                array[count++] = sGrid[i][j];
            }
        }

        return array;
    }
    //=======================================================================================================================
    public int[] convertToArray(int[][] grid) {
        int count = 0;
        int[] gridArray = new int[81];
        
        /*for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 3; j++) {
                for(int k = 0; )
            }
        }*/
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
        return gridArray;
    }

    public int[][] convertToGrid(int[] gridArray) {
        int count = 0;
        int[][] grid = new int[9][9];
        
        /*for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 3; j++) {
                for(int k = 0; )
            }
        }*/
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
        return grid;
    }

}

    