package xadrez.pecas;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.Partida;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {

	private Partida partida;

	public Peao(Tabuleiro tabuleiro, Cor cor, Partida partida) {
		super(tabuleiro, cor);
		this.partida = partida;
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

			// movimento especial enPEassant
			if (this.posicao.getLinha() == 3) {// a linha onde o peão tem de estar
				Posicao esquerda = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() - 1);

				if (getTabuleiro().posicaoExiste(esquerda) && existePecaAdversaria(esquerda)
						&& getTabuleiro().peca(esquerda) == partida.getvulneravelEnPeassant()) {// caso a peca na
																								// esquerda seja a peca
																								// vulneravel a
																								// enPeassant
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;// o peão se move para cima do peão q sera
																				// capturado, não para a mesma posicao
				}

				Posicao direita = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() + 1);

				if (getTabuleiro().posicaoExiste(direita) && existePecaAdversaria(direita)
						&& getTabuleiro().peca(direita) == partida.getvulneravelEnPeassant()) {// caso a peca na
																								// esquerda seja a peca
																								// vulneravel a
																								// enPeassant
					mat[direita.getLinha() - 1][direita.getColuna()] = true;// o peão se move para cima do peão q sera
																			// capturado, não para a mesma posicao
				}
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

			// movimento especial enPeassant
			if (this.posicao.getLinha() == 4) {// a linha onde o peão tem de estar
				Posicao esquerda = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() - 1);

				if (getTabuleiro().posicaoExiste(esquerda) && existePecaAdversaria(esquerda)
						&& getTabuleiro().peca(esquerda) == partida.getvulneravelEnPeassant()) {// caso a peca na
																								// esquerda seja a peca
																								// vulneravel a
																								// enPeassant
					mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;// o peão se move para cima do peão q sera
																				// capturado, não para a mesma posicao
				}

				Posicao direita = new Posicao(this.posicao.getLinha(), this.posicao.getColuna() + 1);

				if (getTabuleiro().posicaoExiste(direita) && existePecaAdversaria(direita)
						&& getTabuleiro().peca(direita) == partida.getvulneravelEnPeassant()) {// caso a peca na
																								// esquerda seja a peca
																								// vulneravel a
																								// enPeassant
					mat[direita.getLinha() + 1][direita.getColuna()] = true;// o peão se move para cima do peão q sera
																			// capturado, não para a mesma posicao
				}
			}
		}

		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}

}
