package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.Partida;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {

	private Partida partida;

	public Rei(Tabuleiro tabuleiro, Cor cor, Partida partida) {
		super(tabuleiro, cor);
		this.partida = partida;
	}

	@Override
	public String toString() {
		return "R";
	}

	private boolean ehpossivelMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != this.getCor();
	}

	private boolean testaTorreParaCastling(Posicao posicao) {

		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p != null && p instanceof Torre && p.getCor() == getCor() // caso tenha uma torre da mesma cor do rei
				&& p.getContMovimentos() == 0;// torre ainda não se moveu
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		// acima
		p.setValues(this.posicao.getLinha() - 1, this.posicao.getColuna());
		if (getTabuleiro().posicaoExiste(p) && ehpossivelMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// abaixo
		p.setValues(this.posicao.getLinha() + 1, this.posicao.getColuna());
		if (getTabuleiro().posicaoExiste(p) && ehpossivelMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// direita
		p.setValues(this.posicao.getLinha(), this.posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && ehpossivelMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// esquerda
		p.setValues(this.posicao.getLinha(), this.posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && ehpossivelMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// nordeste
		p.setValues(this.posicao.getLinha() - 1, this.posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && ehpossivelMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// noroeste
		p.setValues(this.posicao.getLinha() + 1, this.posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && ehpossivelMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// sudeste
		p.setValues(this.posicao.getLinha() + 1, this.posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && ehpossivelMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// sudoeste
		p.setValues(this.posicao.getLinha() - 1, this.posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && ehpossivelMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// movimento especial castling
		if (getContMovimentos() == 0 && !partida.getCheck()) {

			// castling pequeno
			Posicao posicaoTorre1 = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() + 3);

			if (testaTorreParaCastling(posicaoTorre1)) {
				Posicao p1 = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() + 1);
				Posicao p2 = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() + 2);

				if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {// todas as casas entre o rei e
																							// a torre estão vazias
					mat[this.posicao.getLinha()][this.posicao.getColuna() + 2] = true;
				}
			}

			// castling grande
			Posicao posicaoTorre2 = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() - 4);

			if (testaTorreParaCastling(posicaoTorre1)) {
				Posicao p1 = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() - 1);
				Posicao p2 = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() - 2);
				Posicao p3 = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() - 3);

				if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null
						&& getTabuleiro().peca(p2) == null) {
					mat[this.posicao.getLinha()][this.posicao.getColuna() - 2] = true;
				}
			}
		}
		return mat;
	}

}
