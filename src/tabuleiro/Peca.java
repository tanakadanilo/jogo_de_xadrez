package tabuleiro;

public class Peca {

	protected Posicao posicao;
	private Tabuleiro tabuleiro;
	public Peca(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
		this.posicao = null;
	}
	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}
	
	
}
