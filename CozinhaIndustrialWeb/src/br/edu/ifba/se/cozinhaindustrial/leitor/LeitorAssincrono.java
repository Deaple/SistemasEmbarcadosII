package br.edu.ifba.se.cozinhaindustrial.leitor;

import br.edu.ifba.se.cozinhaindustrial.conector.SingleConector;

public class LeitorAssincrono implements Runnable {
	private boolean continuar;

	public LeitorAssincrono() {
	}

	@Override
	public void run() {
		continuar = true;

		while (continuar) {
			SingleConector.ler();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void parar() {
		continuar = false;
	}
}