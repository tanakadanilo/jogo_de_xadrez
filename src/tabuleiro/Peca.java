package tabuleiro;

public abstract class Peca {

	protected Posicao posicao;
	private Tabuleiro tabuleiro;

	public Peca(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
		this.posicao = null;
	}

	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	public abstract boolean[][] movimentosPossiveis();

	public boolean podeMover(Posicao posicao) {
		return movimentosPossiveis()[posicao.getLinha()][posicao.getColuna()];
		// chamou o método para ver todos os movimentos possíveis e depois viu se o
		// movimento pedido está incluido nas respostas do método
	}

	public boolean existeMovimentoPossivel() {
		boolean[][] movimentos = movimentosPossiveis();
		for (boolean[] temp : movimentos) {
			for (boolean i : temp) {// caso exista algum movimento possível
				if (i) {
					return true;
				}
			}
		}
		return false;
	}
}
