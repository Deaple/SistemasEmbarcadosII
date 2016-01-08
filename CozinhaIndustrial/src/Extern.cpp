/*
 * Extern.cpp
 *
 *  Created on: 3 de dez de 2015
 *      Author: isaac
 */
#include "Extern.h"
#include "Comunicacao.h"

/*
 STRUCT LEITURA DADOS
 */
struct InfoRF{
	short id;
	short chamas;
	short temperatura;
	short gas; //ok
} infoRF;

Comunicacao com = NULL;

int iniciar(char* porta) {
	com = Comunicacao(porta);
	return com.iniciar();
}

int ler() {
	char ci, cf;  //inicialização
	// realizar a leitura do caractere 'I' (Inicio)
	int resultado = com.ler((char*) &ci, sizeof(ci));

	if ((resultado == EXIT_SUCCESS) && (ci == 'I')) {
		// se a leitura de 'I' ocorrer bem, ler os eixos
		resultado = com.ler((char*)&infoRF, sizeof(InfoRF));
		//se a leitura dos eixos ocorrer bem, le o caractere 'F' (Final)
		if (resultado == EXIT_SUCCESS) {
			resultado = com.ler((char*)&cf, sizeof(cf));
			if ((resultado == EXIT_SUCCESS) && (cf == 'F')) {
				resultado = EXIT_SUCCESS;
			}
		}
	}
	return resultado;
}

/*declaracao de funcoes de retorno dos dados da estrutura*/

int getId(){
	return infoRF.id;
}

int getChamas(){
	return infoRF.chamas;
}

int getGas(){
	return infoRF.gas;
}

int getTemperatura(){
	return infoRF.temperatura;
}

int finalizar() {
	return com.finalizar();
}
