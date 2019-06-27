/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realizable;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Khewbs
 */
public class Realizable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Enter array, delimit elements by a space");
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        String[] s_arr = str.split("\\s+");
        int[] arr = new int[s_arr.length];
        int count = 0;
        for(String s: s_arr){
            arr[count++] = Integer.parseInt(s);
        }
        System.out.println("Enter value of T");
        int T = in.nextInt();
        System.out.println("Part 1: Realizability Check");
        System.out.print("\t");
        realizable(arr, T);
        System.out.println("Part 2: One solution");
        System.out.print("\t");
        showone(arr, T);
        System.out.println("Part 3: All solutions");
        showall(arr, T);
    }
    public static void realizable(int[] arr, int T){
        int S = 0;
        for(int i: arr){
            S += i;
        }
        int P[][] = new int[arr.length+1][(2*S)+1];
        if(T<(S*-1) || T>S){
            System.out.println("The value "+T+" is not realizable");
        }
        else if(T==S || T==(S*-1)){
            System.out.println("The value "+T+" is realizable");
        }
        else{
            for(int i = 0; i<P.length-1; i++){
                if(i==0){
                    P[i][S] = 1;
                    P[i+1][S-arr[i]] = 1;
                    P[i+1][S+arr[i]] = 1;
                }
                else{
                    for(int j = 0; j<P[i].length; j++){
                        if(P[i][j]==1){
                            P[i+1][j-arr[i]] = 1;
                            P[i+1][j+arr[i]] = 1;
                        }
                    }
                }
            }
            if(P[P.length-1][S+T]==1){
                System.out.println("The value "+T+" is realizable");
            }
            else{
                System.out.println("The value "+T+" is not realizable");
            }
        }
        /*for(int i = 0; i<P.length; i++){
            for(int j = 0; j<P[i].length; j++){
                System.out.print(P[i][j]+" ");
            }
            System.out.println("");
        }*/
    }
    public static void showone(int[] arr, int T){
        int S = 0;
        for(int i: arr){
            S += i;
        }
        int P[][] = new int[arr.length+1][(2*S)+1];
        if(T<(S*-1) || T>S){
            System.out.println("The value "+T+" is not realizable");
        }
        else if(T==S){
            System.out.print("Solution: ");
            for(int i = 0; i<arr.length; i++){
                System.out.print("+"+arr[i]);
            }
            System.out.println(" = "+T);
        }
        else if(T==(S*-1)){
            System.out.print("Solution: ");
            for(int i = 0; i<arr.length; i++){
                System.out.print("-"+arr[i]);
            }
            System.out.println(" = "+T);
        }
        else{
            for(int i = 0; i<P.length-1; i++){
                if(i==0){
                    P[i][S] = 1;
                    P[i+1][S-arr[i]] = 1;
                    P[i+1][S+arr[i]] = 1;
                }
                else{
                    for(int j = 0; j<P[i].length; j++){
                        if(P[i][j]==1){
                            P[i+1][j-arr[i]] = 1;
                            P[i+1][j+arr[i]] = 1;
                        }
                    }
                }
            }
            if(P[P.length-1][S+T]==1){
                String str = " = "+T;
                int j = S+T;
                for(int i = P.length-1; i>0; i--){
                    if((j-arr[i-1]>=0) && P[i-1][j-arr[i-1]]==1){
                        str = "+"+arr[i-1]+str;
                        j = j-arr[i-1];
                    }
                    else if((j+arr[i-1]<=(2*S)) && P[i-1][j+arr[i-1]]==1){
                        str = "-"+arr[i-1]+str;
                        j = j+arr[i-1];
                    }
                }
                System.out.println("Solution: "+str);
            }
            else{
                System.out.println("The value "+T+" is not realizable");
            }
        }
    }
    public static void showall(int[] arr, int T){
        int S = 0;
        for(int i: arr){
            S += i;
        }
        int P[][] = new int[arr.length+1][(2*S)+1];
        if(T<(S*-1) || T>S){
            System.out.println("Number of solutions = 0");
        }
        else if(T==S){
            System.out.print("Sol 1: ");
            for(int i = 0; i<arr.length; i++){
                System.out.print("+"+arr[i]);
            }
            System.out.println(" = "+T);
            System.out.println("Number of solutions = 1");
        }
        else if(T==(S*-1)){
            System.out.print("Sol 1: ");
            for(int i = 0; i<arr.length; i++){
                System.out.print("-"+arr[i]);
            }
            System.out.println(" = "+T);
            System.out.println("Number of solutions = 1");
        }
        else{
            for(int i = 0; i<P.length-1; i++){
                if(i==0){
                    P[i][S] = 1;
                    P[i+1][S-arr[i]] = 1;
                    P[i+1][S+arr[i]] = 1;
                }
                else{
                    for(int j = 0; j<P[i].length; j++){
                        if(P[i][j]==1){
                            P[i+1][j-arr[i]] = 1;
                            P[i+1][j+arr[i]] = 1;
                        }
                    }
                }
            }
            if(P[P.length-1][S+T]==1){
                int[][] NP = new int[P.length][P[0].length];
                for(int i = NP.length-1; i>0; i--){
                    for(int j = 0; j<P[i].length; j++){
                        if(i==(NP.length-1) && j==(S+T)){
                            NP[i][j] = 1;
                        }
                        if(NP[i][j]==1){    
                            if((j-arr[i-1]>=0) && P[i-1][j-arr[i-1]]==1){
                                NP[i-1][j-arr[i-1]] = 1;
                            }
                            if((j+arr[i-1]<=(2*S)) && P[i-1][j+arr[i-1]]==1){
                                NP[i-1][j+arr[i-1]] = 1;
                            }
                        }    
                    }    
                }
                String path = "";
                Paths(0, S, NP, arr, path, T, S);
                System.out.println("Number of solutions = "+(counter-1));
            }
            else{
                System.out.println("Number of solutions = 0");
            }
        }
    }
    public static int counter = 1;
    public static void Paths(int i , int j, int[][] NP, int[] arr, String path, int T, int S){
        if(i==arr.length){
            path += " = "+T;
            System.out.println("\tSol "+(counter++)+": "+path);
            return;
        }
        else{
            if((j-arr[i]>=0) && NP[i+1][j-arr[i]]==1){
                String npath = path+"-"+arr[i];
                Paths(i+1, j-arr[i], NP, arr, npath, T, S);
            }
            if((j+arr[i]<=(2*S)) && NP[i+1][j+arr[i]]==1){
                String npath = path+"+"+arr[i];
                Paths(i+1, j+arr[i], NP, arr, npath, T, S);
            }
        }
    }
}
