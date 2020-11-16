//TAHA JUNAID
//18BCD7072
import java.util.*;

public class TicTacToe {
    
    static int[][] a=new int[3][3];//the game board
    static int flag=0;//flag tells us if a triplet if formed or not and a players wins.
    static Scanner in=new Scanner(System.in);
    
//function to check if horizontal triplets are made
    public static void Hor(){
        int sum=0,i,j;//sum keeps a count of similar numbers detected
        for(i=0;i<a.length;i++){
            sum=0;
            for(j=0;j<a[i].length-1;j++)
            {
                if(a[i][j]==a[i][j+1])
                    sum++;
            }
            if(sum==a.length-1){//if sum becomes 2 means 3 matches have been made(eg : a=b and b=c) ,
                System.out.println("Player "+a[i][j]+" wins at row "+(i+1));
                flag=1;//and flag is raised
                break;
            }
        }
   
    }
    //function to check if vertical triplets are made
    public static void Ver(){
        int sum=0,i,j;
        for(i=0;i<a.length;i++){
            sum=0;
            for(j=0;j<a[i].length-1;j++)
            {
                if(a[j][i]==a[j+1][i])
                    sum++;
            }
            if(sum==a.length-1){
                System.out.println("Player "+a[i][j]+" wins at column "+(i+1));
                flag=1;
                break;
            }
        }
    }
    //function to check if diagonal triplets are made
    public static void Diag(){
        int sum=0;
        //Check for triplets at left diagonal
        for(int i=0;i<a.length-1;i++)
                {
                    {
                    for(int j=0;j<a[i].length-1;j++)
                        if(i==j)
                        {
                            if(a[i][j]==a[i+1][j+1])
                            sum++;
                        }
                    }
                    if(sum==a.length-1)
                        {System.out.println("Player "+a[1][1]+" wins at left diagonal ");flag=1;break;}
                }
        sum=0;
        //check for triplets at right diagonal
        for(int i=0;i<a.length-1;i++)
        {
            {
            for(int j=1;j<a[i].length;j++){
                if(i+j==a.length-1)
                {
                    if(a[i][j]==a[i+1][j-1])
                        sum++;
                }
                
            }
            }
            if(sum==a.length-1)
                {
                    System.out.println("Player "+a[1][1]+" wins at right diagonal ");
                    flag=1;
                    break;
                }
        }
    }
   
    public static void main(String args[]) {
        System.out.println("Instructions :\n\n\tEnter 2 space seperated Integers for the location as \"i j\"\n");
        System.out.println("\tUse this as reference to enter locations :\n\t00 01 02\n\t10 11 12\n\t20 21 22\n");

        int c=2;
        //Initialize the game matrix with numbers other than 1 or 2.if we dont all will be 
        // initialized with 0 and algorithm will detect triplets formed by 0.
        for(int i=0;i<a.length;i++)
        {
            for(int j=0;j<a[i].length;j++)
            {
                a[i][j]=c+1;c++;
            }
        }

         
         
        //enter location to play as i j
        int loci,locj;
        //loop gives alternate turns to player 1 and player 2
        while(flag!=1){
            
            //player 1's turn
            System.out.print("\nPlayer 1 enter location : ");
            loci=in.nextInt();
            locj=in.nextInt();
            a[loci][locj]=1;
            System.out.println("Current Game :");
            //print current game board
            for(int i=0;i<a.length;i++)
            {
            {
                for(int j=0;j<a[i].length;j++)
                {
                    if(a[i][j]!=1 && a[i][j]!=2)//if any number other than 1 or 2 is found ,
                    System.out.print("_"+" ");   //print blank
                    else
                    System.out.print(a[i][j]+" ");//else print 1 or 2 (whatever it is)
                }
            }
                System.out.println();
            }
            //check all 3 cases
            Ver();
            Hor();
            Diag();
            
            
            if(flag==1)//check if flag raised(meaning a triplet is formed) after player 1's turn)
            break;
            
            //Player 2's turn
            System.out.print("\nPlayer 2 enter location : ");
                loci=in.nextInt();
                locj=in.nextInt();
                a[loci][locj]=2;
                System.out.println("Current Game :");
            for(int i=0;i<a.length;i++)
            {
                {
                for(int j=0;j<a[i].length;j++)
                {
                    if(a[i][j]!=1 && a[i][j]!=2)
                        System.out.print("_"+" ");
                    else
                        System.out.print(a[i][j]+" ");
                }
                }
                System.out.println();
            }
            //check all 3 cases
            Ver();
            Hor();
            Diag();
        }
    }
}
//00 01 02
//10 11 12
//20 21 22