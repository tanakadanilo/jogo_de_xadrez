package aplicativo;

import tabuleiro.Tabuleiro;
import xadrez.Partida;

public class Programa {

	public static void main(String[] args) {
		Partida partida = new Partida();
		InterfaceUsuario.imprimePecas(partida.getpecas());
	}

}
