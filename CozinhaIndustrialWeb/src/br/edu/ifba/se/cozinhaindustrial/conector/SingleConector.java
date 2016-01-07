package br.edu.ifba.se.cozinhaindustrial.conector;

import br.edu.ifba.se.cozinhaindustrial.IComunicacaoRF;
import br.edu.ifba.se.cozinhaindustrial.Informacao; //Classe informacao

import java.util.concurrent.Semaphore; //Semaforo 3 unidade

public class SingleConector {
	//private static final String PORTA = "/dev/ttyUSB0";
	private static final String PORTA = "/COM4";
	private static IComunicacaoRF comRF = null;
	private static Informacao info; // Classe informacao
	private static Semaphore semaforo; // Semaforo 3 unidade

	public static void iniciarComunicacoRF(String libPath) {
		info = new Informacao(); // Classe informacao
		comRF = FabricaConectores.getConector(libPath);
		semaforo = new Semaphore(1, true);
		if (comRF.iniciar(PORTA) == 0) {
			System.out.println("Acesso a sensores iniciado com sucesso.");
			dispensarPrimeirasLeituras();
		} else
			System.out.println("Nao foi possível iniciar acesso a sensores.");
	}

	public static void dispensarPrimeirasLeituras() {
		for (int i = 0; i < 10; i++) {
			comRF.ler();

			System.out.println("Dispensando leitura do ID: " + comRF.getID() + " [T/C/G] : " + comRF.getTemperatura()
					+ "/" + comRF.getChamas() + "/" + comRF.getGas());
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static Informacao getInformacao() {
		Informacao info_ = new Informacao();

		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		info_.setTemperatura(info.getTemperatura());
		info_.setChamas(info.getChamas());
		info_.setGas(info.getGas());
		semaforo.release();

		return info_;
	}

	public static int ler() {
		// Semaforo 3 unidade
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int resultado = comRF.ler();

		if (resultado == 0) {
			info.setTemperatura(comRF.getTemperatura());
			info.setChamas(comRF.getChamas());
			info.setGas(comRF.getGas());
		}

		// Semaforo 3 unidade
		semaforo.release();

		return resultado;
	}

	public static void finalizar() {
		comRF.finalizar();
	}
}
