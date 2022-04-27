package aplicativo;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.ExcecaoXadrez;
import xadrez.Partida;
import xadrez.PecaXadrez;
import xadrez.posicaoXadrez;

public class Programa {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Partida partida = new Partida();
		List<PecaXadrez> capturadas = new ArrayList<>();

		while (!partida.getCheckMate()) {
			InterfaceUsuario.limpaTela();

			try {
				InterfaceUsuario.limpaTela();

				InterfaceUsuario.imprimePartida(partida, capturadas);
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

				if (pecaCapturada != null) {
					capturadas.add(pecaCapturada);
				}

				if (partida.getPromovida() != null) {
					System.out.println("Pra Qual peça deseja promover este peão? (Q/B/C/T)");
					String tipo = sc.nextLine();
					
					while (!tipo.equalsIgnoreCase("Q") && !tipo.equalsIgnoreCase("B") && !tipo.equalsIgnoreCase("C")
							&& !tipo.equalsIgnoreCase("T")) {
						System.out.println("valor inválido!");
						System.out.println("Pra Qual peça deseja promover este peão? (Q/B/C/T)");
						 tipo = sc.nextLine();
						
					}
					partida.trocarPecaPromovida(tipo);
				}
			} catch (ExcecaoXadrez e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}

		InterfaceUsuario.limpaTela();
		InterfaceUsuario.imprimePartida(partida, capturadas);
	}

}
