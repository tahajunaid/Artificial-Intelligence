import java.util.*;
//Approach : Each possible action is represented by a branch of a node and an entire state space tree is formed in this way.
//           The possible actions are fill A,fill B,transfer from A to B,transfer from B to A,empty A and empty B.
//           Class Node has been made as parent with all the above actions as child nodes.
//           Each Node has x and y which represent current level of water in A and B Jugs respectively.

class Node{
    int x;
    int y;   
    Node fillA;
    Node fillB;
    Node AtoB;
    Node BtoA;
    Node emptyA;
    Node emptyB;
}



public class WaterJug {

    //To keep a track of already encountered states.
    static ArrayList<ArrayList<Integer>> al = new ArrayList<ArrayList<Integer>>();

    //This function generates entire state space tree
    public static void generateStateSpace(Node node,int Amax,int Bmax){
        
        
        int flag=0;//flag 0 indicates the space has never been encountered before
        for(int i=0;i<al.size();i++)
            if(al.get(i).get(0)==node.x && al.get(i).get(1)==node.y)
                flag=1;
        
        if(flag==0)//if state is not encountered before,add it to the list
            al.add(new ArrayList<Integer>(Arrays.asList(node.x,node.y)));
        else//if state is encountered before,return
            return;
        
        //initialize nodes representing all actions
        node.fillA=new Node();
        node.fillB=new Node();
        
        node.AtoB=new Node();
        node.BtoA=new Node();
        
        node.emptyA=new Node();
        node.emptyB=new Node();
        
        node.fillA.x=Amax;
        node.fillA.y=node.y;
        
        node.fillB.y=Bmax;
        node.fillB.x=node.x;

        node.BtoA.x=node.x;
        node.BtoA.y=node.y;
        
        node.AtoB.x=node.x;
        node.AtoB.y=node.y;
        
        node.emptyA.x=0;
        node.emptyA.y=node.y;
        
        node.emptyB.x=node.x;
        node.emptyB.y=0;
        
        //transfer from A to B
        while(true)
        {
            if(node.BtoA.x==Amax || node.BtoA.y==0)
                break;
            node.BtoA.y--;
            node.BtoA.x++;
        }
        
        //transfer from A to B
        while(true)
        {
            if(node.AtoB.y==Bmax || node.AtoB.x==0)
                break;
            node.AtoB.y++;
            node.AtoB.x--;
        }

   
        //recurse through all the possible actions
        generateStateSpace(node.fillA,Amax,Bmax);
        generateStateSpace(node.fillB,Amax,Bmax);
        generateStateSpace(node.BtoA,Amax,Bmax);
        generateStateSpace(node.AtoB,Amax,Bmax);
        generateStateSpace(node.emptyA,Amax,Bmax);
        generateStateSpace(node.emptyB,Amax,Bmax);

    }
    
    //This function returns true if a path exists between root and target node,if true ,stores the path in ArrayList arr.
     public static boolean hasPath(Node root, ArrayList<ArrayList<Integer>> arr, int t)  
    {  
        //if no path, return false
        if (root==null)  
            return false;  
        
        //add state to list
        arr.add(new ArrayList<Integer>(Arrays.asList(root.x,root.y)));      
        
        //if target reached return true
        if (root.x == t && root.y==0)      
            return true;  
        //check if target is present in subtrees of all possible actions
        if (hasPath(root.fillA, arr, t) ||  hasPath(root.fillB, arr, t) ||  hasPath(root.AtoB, arr, t) ||  hasPath(root.BtoA, arr, t) || hasPath(root.emptyA, arr, t) ||  hasPath(root.emptyB, arr, t))
            return true;  
        
        //this part is reached only when no subtree/action has the target state.
        arr.remove(arr.size()-1);  
        return false;              
    }  
  
    //This function prints the Path given by hasPath function
    public static void DFS(Node root, int t)  
    {  
 
     ArrayList<ArrayList<Integer>> arr = new ArrayList<ArrayList<Integer>>();

        if (hasPath(root, arr, t))  
        {  System.out.println("The steps involved to achieve the target using DFS : ");
            for (int i=0; i<arr.size()-1; i++)      
                System.out.println(arr.get(i)); 
            System.out.print(arr.get(arr.size() - 1));     
        }  

        else
            System.out.print("No Path");  
    }  
    

    //driver function
    public static void main(String args[]) {
        Scanner in=new Scanner(System.in);
        
        System.out.print("Enter the capacity of Jug A : ");
        int A=in.nextInt();
        System.out.print("Enter the capacity of Jug B : ");
        int B=in.nextInt();
        System.out.print("Enter the quantity of water required : ");
        int Q=in.nextInt();
        
        //Initialize Jug
        Node node=new Node();
        node.x=0;
        node.y=0;
        
        generateStateSpace(node,A,B);//generate entire state space tree on 'node'

        DFS(node,Q);//DFS on 'node' for Q

    }
}