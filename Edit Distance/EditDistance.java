/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editdistance;

import java.util.Scanner;

/**
 *
 * @author Khubaib
 */
public class EditDistance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter first string:");
        String str1 = in.nextLine();
        System.out.println("Enter second string:");
        String str2 = in.nextLine();
        boolean flag = false;
        if(str1.trim().length()==0){
            System.out.println("String 1 was empty");
            flag = true;
        }
        if(str2.trim().length()==0){
            System.out.println("String 2 was empty");
            flag = true;
        }
        if(!flag){
            char[] c1;
            char[] c2;
            int[] cost;
            Path fin = new Path();
            fin = match(str1, str2);
            int ed = fin.cost;
            System.out.println("Edit distance = "+ed);
            while(fin.next!=null){
                if(fin.cost-fin.next.cost==2){
                    if(fin.row-fin.next.row==1){
                        System.out.println(str1.charAt(fin.row)+" - "+(fin.cost-fin.next.cost));
                    }
                    else{
                        System.out.println("- "+str2.charAt(fin.col)+" "+(fin.cost-fin.next.cost));
                    }
                }
                else{
                    System.out.println(str1.charAt(fin.row)+" "+str2.charAt(fin.col)+" "+(fin.cost-fin.next.cost));
                }
                fin = fin.next;
            }
            System.out.println("Edit distance = "+ed);
            System.out.println("Path to be read from bottom to top");
        }
    }
    
    static Path match(String a, String b){
        Path head = new Path();
        int mat[][] = new int[a.length()+1][b.length()+1];
        for(int i = 0; i<mat.length; i++){ 
            for(int j = 0; j<mat[i].length; j++){  
                if(i==0){ 
                    mat[i][j] = j*2;
                }
                else if(j==0){ 
                    mat[i][j] = i*2;
                }
                else if(a.charAt(i-1) == b.charAt(j-1)){ 
                    mat[i][j] = mat[i-1][j-1];
                }
                else{
                    if(mat[i][j-1]<mat[i-1][j]){
                        if(mat[i][j-1]<mat[i-1][j-1]){
                            mat[i][j] = mat[i][j-1]+2;
                        }
                        else{
                            mat[i][j] = mat[i-1][j-1]+1;
                        }
                    }
                    else{
                        if(mat[i-1][j]<mat[i-1][j-1]){
                            mat[i][j] = mat[i-1][j]+2;
                        }
                        else{
                            mat[i][j] = mat[i-1][j-1]+1;
                        }
                    }
                }
            } 
        }
        int i = a.length(); int j = b.length();
        while(i>0&&j>0){
            if(a.charAt(i-1) == b.charAt(j-1)){ 
                Path in = head;
                while(in.next!=null){
                    in = in.next;
                }
                in.col = j-1;
                in.row = i-1;
                in.cost = mat[i][j];
                in.next = new Path();
                i--;
                j--;
            }
            else{
                if(mat[i-1][j-1]<=mat[i][j-1] && mat[i-1][j-1]<=mat[i-1][j]){
                    Path in = head;
                    while(in.next!=null){
                        in = in.next;
                    }
                    in.col = j-1;
                    in.row = i-1;
                    in.cost = mat[i][j];
                    in.next = new Path();
                    i--; j--;
                }
                else if(mat[i][j-1]<=mat[i-1][j] && mat[i][j-1]<=mat[i-1][j-1]){
                    Path in = head;
                    while(in.next!=null){
                        in = in.next;
                    }
                    in.col = j-1;
                    in.row = i-1;
                    in.cost = mat[i][j];
                    in.next = new Path();
                    j--;
                }
                else{
                    Path in = head;
                    while(in.next!=null){
                        in = in.next;
                    }
                    in.col = j-1;
                    in.row = i-1;
                    in.cost = mat[i][j];
                    in.next = new Path();
                    i--;
                }
            }
        }
        return head;
    }
}

class Path{
    public int row, col;
    public int cost;
    public Path next = null;
}