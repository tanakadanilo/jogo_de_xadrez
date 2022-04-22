package xadrez;

import tabuleiro.Posicao;

public class posicaoXadrez {

	private char coluna;
	private int linha;

	public posicaoXadrez(char coluna, int linha) {
		if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
			throw new ExcecaoXadrez("Erro ao mover peça. " + "Os valores válidos vão de a1 até h8");
		}
		this.coluna = coluna;
		this.linha = linha;
	}

	public char getColuna() {
		return coluna;
	}

	public int getLinha() {
		return linha;
	}

	protected Posicao toPosicao() {
		return new Posicao((8 - this.linha), (coluna - 'a'));
	}

	protected static posicaoXadrez fromPosicao(Posicao posicao) {
		return new posicaoXadrez((char) ('a' + posicao.getColuna()), (8 - posicao.getLinha()));
	}

	@Override
	public String toString() {
		return "" + this.coluna + this.linha;
	}
	
	
}
