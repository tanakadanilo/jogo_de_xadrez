package aplicativo;

import java.util.Scanner;

import xadrez.Partida;
import xadrez.PecaXadrez;
import xadrez.posicaoXadrez;

public class Programa {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Partida partida = new Partida();

		while (true) {

			InterfaceUsuario.imprimePecas(partida.getpecas());
			System.out.println();
			System.out.println("Inicio: ");
			posicaoXadrez inicio = InterfaceUsuario.lerPosicaoXadrez(sc);
			System.out.println();
			System.out.println("Alvo:");
			posicaoXadrez destino = InterfaceUsuario.lerPosicaoXadrez(sc);
			
			PecaXadrez pecaCapturada = partida.moverPecaXadrez(inicio, destino);
			
		}
	}

}
