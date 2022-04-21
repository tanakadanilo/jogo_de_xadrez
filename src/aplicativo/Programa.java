package aplicativo;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.ExcecaoXadrez;
import xadrez.Partida;
import xadrez.PecaXadrez;
import xadrez.posicaoXadrez;
import aplicativo.InterfaceUsuario;

public class Programa {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Partida partida = new Partida();

		while (true) {
			try {
				InterfaceUsuario.limpaTela();
				
				InterfaceUsuario.imprimeTabuleiro(partida.getpecas());
				System.out.println();
				System.out.println("Origem: ");
				posicaoXadrez inicio = InterfaceUsuario.lerPosicaoXadrez(sc);
				
				boolean[][] movimentosPossiveis = partida.movimentosPossiveis(inicio);
				InterfaceUsuario.limpaTela();
				InterfaceUsuario.imprimeTabuleiro(partida.getpecas(), movimentosPossiveis);
				
				System.out.println();
				System.out.println("Alvo:");
				posicaoXadrez destino = InterfaceUsuario.lerPosicaoXadrez(sc);

				PecaXadrez pecaCapturada = partida.moverPecaXadrez(inicio, destino);
			} catch (ExcecaoXadrez e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
	}

}
