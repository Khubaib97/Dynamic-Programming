#include <iostream>
#include <string>
using namespace std;
/* run this program using the console pauser or add your own getch, system("pause") or input loop */

class Path{
	static Path n;
	int cost;
	int row, col;
};

int** matrix;
int* c;
char* c1;
char* c2;

static void match(string s1, string s2){
	matrix = new int*[s1.length()+1];
	for(int i = 0; i<=s1.length(); i++){
		matrix[i] = new int[s2.length()+1];
	}
	int indel = 2; int sub = 1;
	for(int i = 0; i<=s1.length(); i++){
		for(int j = 0; j<=s2.length(); j++){
			if(i==0){
				matrix[i][j] = j*indel;
			}
			else if(j==0){
				matrix[i][j] = i*indel;
			}
			else if(s1.at(i-1)==s2.at(j-1)){
				matrix[i][j] = matrix[i-1][j-1];
			}
			else{
				int ret = min(min(matrix[i-1][j-1], matrix[i][j-1]), matrix[i-1][j]);
				if(ret==matrix[i-1][j-1]){
					matrix[i][j] = ret+sub;
				}
				else{
					matrix[i][j] = ret+indel;
				}
			}
		}
	}
	cout << endl;
	cout << "Edit distance = " << matrix[s1.length()][s2.length()] << endl;
	int k = max(s1.length(), s2.length());
	c = new int[k];
	int i = s1.length(); int j = s2.length();
	c1 = new char[k]; c2 = new char[k];
	k = k-1;
	int i1 = s1.length()-1; int j1 = s2.length()-1;
	for(; i>0 && j>0 ;){
		int ret = min(min(matrix[i-1][j-1], matrix[i][j-1]), matrix[i-1][j]);
		if(s1.at(i1) == s2.at(j1)){
			c1[k] = s1.at(i1--);
			c2[k] = s2.at(j1--);
			c[k] = (1-1);
			i--; j--; k--;
		}
		else{
			if(ret==matrix[i-1][j-1]){
				c1[k] = s1.at(i1--);
				c2[k] = s2.at(j1--);
				c[k] = 1;
				i--; j--; k--;
			}
			else if(ret==matrix[i][j-1]){
				c1[k] = '_';
				c2[k] = s2.at(j1--);
				c[k] = 2;
				j--; k--;
			}
			else{
				c1[k] = s1.at(i1--);
				c2[k] = '_';
				c[k] = 2;
				i--; k--;
			}
		}
	}
	for(i = 0; i<max(s1.length(), s2.length()); i++){
		cout << c1[i] << " " << c2[i] << " " << c[i] << endl;
	}
}

int main(int argc, char** argv) {
	string s1; string s2;
	cout << "Input string 1" << endl;
	getline(cin, s1);
	cout << "Input string 2" << endl;
	getline(cin, s2);
	if(s1.empty() || s2.empty()){
		cout << "Empty String. Terminating Program." << endl;
		exit(0);
	}
	match(s1, s2);
	return 0;
}
