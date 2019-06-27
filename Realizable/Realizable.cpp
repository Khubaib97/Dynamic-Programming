#include <iostream>
#include <string>
#include <sstream>
using namespace std;

/* run this program using the console pauser or add your own getch, system("pause") or input loop */

int s = 0;
int sol = 0;
int** p;

void part1(int a[], int t, int h){
	cout << "Part 1: " << endl;
	if(t==(s) || t==(-s)){
		cout << t << " is realizable" << endl;
	}
	else if(t>(s) || t<(-s)){
		cout << t << " is not realizable" << endl;
	}
	else{
		int w = (2*s)+1;
		p = new int*[h];
		for(int i = 0; i<h; i++){
			p[i] = new int[w];
		}
		// 0 to indicate node not visited, 1 for visited
		for(int i = 0; i<h; i++){
			for(int j = 0; j<w; j++){
				p[i][j] = 0;
			}
		}
		p[0][s] = 1;
		for(int i = 0; i<h; i++){
			if(i==(h-1)){
				break;
			}
			for(int j = 0; j<w; j++){
				if(p[i][j]==1){
					if(j+a[i]<w){
						p[i+1][j+a[i]] = 1;
					}
					if(j-a[i]>=0){
						p[i+1][j-a[i]] = 1;
					}
				}
			}
		}
		if(p[h-1][s+t]!=1){
			cout << t << " is not realizable" << endl;
		}
		else{
			cout << t << " is realizable" << endl;
		}
	}
}

void part2(int a[], int t, int h){
	cout << "Part 2: " << endl;
	if(t==(s) || t==(-s)){
		if(t==s){
			for(int i = 0; i<(h-1); i++){
				cout << "+" << a[i];
			}
			cout << "=" << t << endl;
		}
		else{
			for(int i = 0; i<(h-1); i++){
				cout << "-" << a[i];
			}
			cout << "=" << t << endl;
		}
	}
	else if(t>(s) || t<(-s)){
		cout << t << " is not realizable" << endl;
	}
	else{
		int w = (2*s)+1;
		p = new int*[h];
		for(int i = 0; i<h; i++){
			p[i] = new int[w];
		}
		// 0 to indicate node not visited, 1 for visited
		for(int i = 0; i<h; i++){
			for(int j = 0; j<w; j++){
				p[i][j] = 0;
			}
		}
		p[0][s] = 1;
		for(int i = 0; i<h; i++){
			if(i==(h-1)){
				break;
			}
			for(int j = 0; j<w; j++){
				if(p[i][j]==1){
					if(j+a[i]<w){
						p[i+1][j+a[i]] = 1;
					}
					if(j-a[i]>=0){
						p[i+1][j-a[i]] = 1;
					}
				}
			}
		}
		if(p[h-1][s+t]!=1){
			cout << t << " is not realizable" << endl;
		}
		else{
			int j = (s+t);
			string str = "";
			for(int i = h-2; i>=0; i--){
                if((j+a[i]<w) && p[i][j+a[i]]==1){
                    stringstream ss;
					ss << a[i];
					str = "-"+ss.str()+str;
					j += a[i];
                }
                else if((j-a[i]>=0) && p[i][j-a[i]]==1){
                    stringstream ss;
					ss << a[i];
					str = "+"+ss.str()+str;
					j -= a[i];
                }
            }
			cout << str << "=" << t << endl;
		}
	}
}

void graphsearch(int i, int j, int a[], int t, int h, string str){
	int w = (2*s)+1;
	if(i==-1){
		cout << "Solution " << sol++ << ": " << str << "=" << t << endl;
	}
	else{
		if((j+a[i]<w) && p[i][j+a[i]]==1){
			string str1 = "";
			stringstream ss;
			ss << a[i];
			str1 = "-"+ss.str()+str;
			graphsearch(i-1, j+a[i], a, t, h, str1);
		}
		if((j-a[i]>=0) && p[i][j-a[i]]==1){
			string str1 = "";
			stringstream ss;
			ss << a[i];
			str1 = "+"+ss.str()+str;
			graphsearch(i-1, j-a[i], a, t, h, str1);
		}
	}
}

void part3(int a[], int t, int h){
	cout << "Part 3: " << endl;
	if(t==(s) || t==(-s)){
		if(t==s){
			for(int i = 0; i<(h-1); i++){
				cout << "+" << a[i];
			}
			cout << "=" << t << endl;
			cout << "Solutions = 1" << endl;
		}
		else{
			for(int i = 0; i<(h-1); i++){
				cout << "-" << a[i];
			}
			cout << "=" << t << endl;
			cout << "Solutions = 1" << endl;
		}
	}
	else if(t>(s) || t<(-s)){
		cout << t << " is not realizable" << endl;
	}
	else{
		int w = (2*s)+1;
		p = new int*[h];
		for(int i = 0; i<h; i++){
			p[i] = new int[w];
		}
		// 0 to indicate node not visited, 1 for visited
		for(int i = 0; i<h; i++){
			for(int j = 0; j<w; j++){
				p[i][j] = 0;
			}
		}
		p[0][s] = 1;
		for(int i = 0; i<h; i++){
			if(i==(h-1)){
				break;
			}
			for(int j = 0; j<w; j++){
				if(p[i][j]==1){
					if(j+a[i]<w){
						p[i+1][j+a[i]] = 1;
					}
					if(j-a[i]>=0){
						p[i+1][j-a[i]] = 1;
					}
				}
			}
		}
		if(p[h-1][s+t]!=1){
			cout << t << " is not realizable" << endl;
		}
		else{
			string str = "";
			graphsearch(h-2, (s+t), a, t, h, str);
			cout << "Solutions = " << sol << endl;
		}
	}
}

int main(int argc, char** argv) {
	cout << "Enter size of array a" << endl;
	int size;
	cin >> size;
	int a[size];
	cout << "Enter each number and press enter" << endl;
	for(int i = 0; i<size; i++){
		cin >> a[i];
		s +=a[i];
	}
	int t;
	cout << "Enter value to be tested" << endl;
	cin >> t;
	part1(a, t, size+1);
	part2(a, t, size+1);
	part3(a, t, size+1);
	return 0;
}
