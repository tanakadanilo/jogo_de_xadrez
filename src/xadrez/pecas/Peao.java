package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {

	public Peao(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public boolean[][] movimentosPossiveis() {

		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);
		if (getCor() == Cor.BRANCO) {
			p.setValues(super.posicao.getLinha() - 1, super.posicao.getColuna());

			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {// se pode mover uma casa pra frente
				mat[p.getLinha()][p.getColuna()] = true;
			}

			p.setValues(super.posicao.getLinha() - 2, super.posicao.getColuna());
			Posicao casaAFrente = new Posicao(super.posicao.getLinha() - 1, super.posicao.getColuna());

			if (super.getContMovimentos() == 0 && getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)
					&& getTabuleiro().posicaoExiste(casaAFrente) && !getTabuleiro().temPeca(casaAFrente)) {
//no primeiro movimento do peão, ele pode se mover 2 casas, caso estejam desocupadas
				mat[p.getLinha()][p.getColuna()] = true;
			}

			p.setValues(super.posicao.getLinha() - 1, super.posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {// movimento de ataque para noroeste
				mat[p.getLinha()][p.getColuna()] = true;
			}

			p.setValues(super.posicao.getLinha() - 1, super.posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {// movimento de ataque para nordeste
				mat[p.getLinha()][p.getColuna()] = true;
			}
		} else {
			p.setValues(super.posicao.getLinha() + 1, super.posicao.getColuna());

			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {// se pode mover uma casa pra frente
				mat[p.getLinha()][p.getColuna()] = true;
			}

			p.setValues(super.posicao.getLinha() + 2, super.posicao.getColuna());
			Posicao casaAFrente = new Posicao(super.posicao.getLinha() + 1, super.posicao.getColuna());

			if (super.getContMovimentos() == 0 && getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)
					&& getTabuleiro().posicaoExiste(casaAFrente) && !getTabuleiro().temPeca(casaAFrente)) {
//no primeiro movimento do peão, ele pode se mover 2 casas, caso estejam desocupadas
				mat[p.getLinha()][p.getColuna()] = true;
			}

			p.setValues(super.posicao.getLinha() + 1, super.posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {// movimento de ataque para noroeste
				mat[p.getLinha()][p.getColuna()] = true;
			}

			p.setValues(super.posicao.getLinha() + 1, super.posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {// movimento de ataque para nordeste
				mat[p.getLinha()][p.getColuna()] = true;
			}
		}

		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}

}
