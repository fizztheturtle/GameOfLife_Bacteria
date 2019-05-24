import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.*;
public class Bacteria {
    private static ArrayList<Integer> counter =new ArrayList<>();

    public static void main(String[] args) {
        String CoOrds;
        ArrayList<Integer> Vals = new ArrayList<>();
        final int w = 2;
        final int width= 10;
        final int height= 10;
        ArrayList<Integer> xInput = new ArrayList<>();
        ArrayList<Integer> yInput = new ArrayList<>();
        final int columns = width/w;
        final int rows = height/w;
        Cell[][] board;

        Scanner enterValues = new Scanner(System.in);
        System.out.println("Input 2 integer values separated by a comma,");
        System.out.println("e.g: 10,10");
        System.out.println("\n To stop, type: end ");
        do {
             CoOrds = enterValues.nextLine();
            if (CoOrds.equals("end"))
            {
                break;
            }
             if(!Pattern.matches("\\d+[,]\\d+",CoOrds)){
                 System.out.println("pattern does not match required input of:");
                 System.out.println("x,y");
                 System.out.println("e.g: 123,123 ");
             }


             else if(Pattern.matches("\\d+[,]\\d+",CoOrds)){
                 Pattern pattern = Pattern.compile("\\d+");
                 Matcher matcher = pattern.matcher(CoOrds);


                 while(matcher.find()) {


                         Vals.add(Integer.parseInt(matcher.group()));

                 }


             }

        }while (!CoOrds.equals("end"));

        for(int i = 0; i < Vals.size(); i++){
            if ( (i & 1) == 0 ) {
                inputCheck(Vals, xInput, i);

            }
            else{
                inputCheck(Vals, yInput, i);
            }

        }

        board=  new Cell[columns][rows];
        init(board,xInput,yInput, columns, rows,w);


    }

    private static void inputCheck(ArrayList<Integer> vals, ArrayList<Integer> Input, int i) {
        if(vals.get(i)>10) {

            String number =String.valueOf(vals.get(i));
            int lastVal= Integer.parseInt(number.substring(number.length() - 1));
            int restofVal= Integer.parseInt(number.substring(0,number.length() - 1));
            counter.add(restofVal);
            Input.add(lastVal);
        }
        else {
            counter.add(0);
            Input.add(vals.get(i));
        }
    }

    private static void init(Cell[][] board, ArrayList<Integer> xInput, ArrayList<Integer> yInput, int columns, int rows, int w) {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {

                    board[i][j] = new Cell(i*w, j*w, w);
                if(xInput.contains(j)&&yInput.contains(i)){
                    board[i][j].x=j;
                    board[i][j].y=i;
                    board[i][j].newState(1);

                }

            }
        }
        display(columns,rows,board);
        gen(columns,rows,board);
    }


    private static void gen(int columns, int rows, Cell [][] board){
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {

                 board[i][j].savePrevious();

            }
        }
        // Loop through every spot in our 2D array and check spots neighbors
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {

                // Add up all the states in a 3x3 surrounding grid
                int neighbors = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        neighbors += board[(x+i+columns)%columns][(y+j+rows)%rows].previous;
                    }
                }

                neighbors -= board[x][y].previous;

                // Rules required
                // under-population
                if      ((board[x][y].state == 1) && (neighbors <  2)) board[x][y].newState(0);
                    // Overcrowding
                else if ((board[x][y].state == 1) && (neighbors >  3)) board[x][y].newState(0);
                    // Reproduction
                else if ((board[x][y].state == 0) && (neighbors == 3)) board[x][y].newState(1);

            }
        }


        display(columns,rows,board);

    }
    private static void display(int rows , int columns, Cell [][] board) {
        int counter2=0;
        long numberOfUniqueVals=counter.stream().distinct().count();

        for(int x = 0; x < numberOfUniqueVals; x++){
            for ( int j = 0; j < rows;j++) {
                for ( int i = 0; i < columns;i++) {
                        if(board[i][j].state==1) {
                            try{
                                if(counter.get(counter2*2)!=0){
                                    System.out.println(counter.get(counter2*2).toString()+j +
                                            "," + counter.get(counter2*2).toString()+i);
                                }
                                else if(counter.get(counter2*2)==0 ){
                                    System.out.println(j + "," + i);
                                }
                            }catch (Exception e){
                            }
                            counter2++;
                        }
                }
            }
        }
    }
}





class Cell {

    float x, y;
    float w;

    int state;
    int previous;

    Cell(float x_, float y_, int w_) {
        x = x_;
        y = y_;
        w= w_;

        state = 0;
        previous = state;
    }

    void savePrevious() {
        previous = state;
    }

    void newState(int s) {
        state = s;
    }

    void display(){

        if(state==1){
            System.out.println(x+","+y);
        }

    }
}

