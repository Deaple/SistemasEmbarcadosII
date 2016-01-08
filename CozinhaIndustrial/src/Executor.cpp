/*
 * Executor.cpp
 *
 *  Created on: 26 de nov de 2015
 */
#include <iostream>
#include "Comunicacao.h"

using namespace std;

struct InfoRF{
	short id;
	short Chamas;
	short temperatura;
	short gas; //ok
};

int main(int argc, char** argv){
	InfoRF info = {0};

	Comunicacao com((char*)"/COM4");
	com.iniciar();
	char ci, cf;
	while(true){
		//ler caractere inicial
		int resultado = com.ler((char*)&ci,sizeof(ci));
		if(ci == 'I' && resultado==EXIT_SUCCESS){
			//ler buffer de dados
			resultado = com.ler((char*)&info,sizeof(InfoRF));
			if(resultado==EXIT_SUCCESS){
				//ler carctere final
				resultado = com.ler((char*)&cf,sizeof(cf));
				if(cf == 'F'&& resultado==EXIT_SUCCESS){
					cout<<"ID " << info.id <<endl;
					cout<<"Chamas "<<info.Chamas<<endl;
					cout<<"temp "<<info.temperatura<<endl;
					cout<<"Gas "<<info.gas<<endl;
				}
			}
		}
		Sleep(50);
	}
	return 0;
}
