package aplicativo;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import xadrez.Cor;
import xadrez.Partida;
import xadrez.PecaXadrez;
import xadrez.posicaoXadrez;

public class InterfaceUsuario {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
//cores para ser impresssas no console	

	public static void limpaTela() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static posicaoXadrez lerPosicaoXadrez(Scanner sc) {
		try {
			String s = sc.nextLine();
			char coluna = s.charAt(0);
			int linha = Integer.parseInt(s.substring(1));
			return new posicaoXadrez(coluna, linha);
		} catch (RuntimeException e) {
			throw new InputMismatchException("Erro ao ler a posição de xadrez. As posições validas vão de a1 até h8");
		}
	}

	public static void imprimePartida(Partida partida, List<PecaXadrez> capturadas) {
		imprimeTabuleiro(partida.getpecas());

		System.out.println();
		imprimePecasCapturadas(capturadas);

		System.out.println();
		System.out.println("Turno: " + partida.getTurno());
		if (!partida.getCheckMate()) {//se não for checkMate
			System.out.println("Aguardando jogador: " + partida.getJogadorAtual());

			if (partida.getCheck()) {
				System.out.println("CHECK!");
			}
		} else {
			System.out.println("CHECKMATE!");
			System.out.println("Ganhador: " + partida.getJogadorAtual());
		}
	}

	public static void imprimeTabuleiro(PecaXadrez[][] pecas) {
		for (int i = 0; i < pecas.length; i++) {

			System.out.print((8 - i) + " ");
			for (int j = 0; j < pecas[i].length; j++) {
				imprimePeca(pecas[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}

	public static void imprimeTabuleiro(PecaXadrez[][] pecas, boolean[][] movimentosPossiveis) {
		for (int i = 0; i < pecas.length; i++) {

			System.out.print((8 - i) + " ");
			for (int j = 0; j < pecas[i].length; j++) {
				imprimePeca(pecas[i][j], movimentosPossiveis[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}

	private static void imprimePeca(PecaXadrez peca, boolean fundoColorido) {

		if (fundoColorido) {
			System.out.print(ANSI_BLUE_BACKGROUND);
		}
		if (peca == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			if (peca.getCor() == Cor.BRANCO) {
				System.out.print(ANSI_WHITE + peca + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
			}
		}
		System.out.print(" " + ANSI_RESET);
	}

	private static void imprimePecasCapturadas(List<PecaXadrez> pecasCapturadas) {
		List<PecaXadrez> branco = pecasCapturadas.stream().filter(x -> x.getCor() == Cor.BRANCO)
				.collect(Collectors.toList());
		List<PecaXadrez> preta = pecasCapturadas.stream().filter(x -> x.getCor() == Cor.PRETO)
				.collect(Collectors.toList());

		System.out.println("Pecas capturadas:");
		System.out.println("Brancas: ");

		System.out.print(ANSI_WHITE);// imprimindo na cor branca
		System.out.println(Arrays.toString(branco.toArray()));
		System.out.print(ANSI_RESET);// resetando cor

		System.out.println("Pretas: ");
		System.out.print(ANSI_YELLOW);// imprimindo na cor Amarela
		System.out.println(Arrays.toString(preta.toArray()));
		System.out.print(ANSI_RESET);// resetando cor
	}
}
