package tabuleiro;

public class posicao {

	private int linha;
	private int coluna;

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	public posicao(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	public void mudarPosicao(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	@Override
	public String toString() {
		return linha + ", " + coluna;
	}

}
