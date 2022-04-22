package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca {

	private Cor cor;
	private int contMovimentos = 0;

	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}

	public int getContMovimentos() {
		return this.contMovimentos;
	}

	public void aumentaContMovimentos() {
		contMovimentos++;
	}

	public void diminuiContMovimentos() {
		contMovimentos--;
	}

	public posicaoXadrez getPosicaoXadres() {
		return posicaoXadrez.fromPosicao(posicao);
	}

	protected boolean existePecaAdversaria(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);

		return p != null && p.cor != this.cor;
	}

}
