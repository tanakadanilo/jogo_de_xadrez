package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "R";
	}

	private boolean ehpossivelMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != this.getCor();
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
		return mat;
	}

}
