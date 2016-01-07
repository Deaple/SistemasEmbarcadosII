package br.edu.ifba.se.cozinhaindustrial;

import com.sun.jna.Library;

public interface IComunicacaoRF extends Library {

	public int iniciar(String porta);

	public int ler();

	public int getID();

	public int getTemperatura();

	public int getChamas();

	public int getGas();

	public int finalizar();
}
