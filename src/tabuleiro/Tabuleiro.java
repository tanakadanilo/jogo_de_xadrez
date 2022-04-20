package tabuleiro;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private Peca[][] pecas;// qual peça está em cada posição do tabuleiro

	public Tabuleiro(int linhas, int colunas) {
		if(linhas < 1  || colunas <1) {
			throw new excecaoTabuleiro("Erro ao criar tabuleiro,"
					+ " é necessário que exista ao menos uma linha e uma "
					+ "coluna");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public Peca peca(int linha, int coluna) {
		if(!posicaoExiste(linha, coluna)) {
			throw new excecaoTabuleiro("Posição não existe");
		}
		return pecas[linha][coluna];
	}

	public Peca peca(Posicao posicao) {
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}

	public void colocarPeca(Peca peca, Posicao posicao) {
		if(temPeca(posicao)) {
			throw new excecaoTabuleiro("Já existe uma peça na posição " + posicao);
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	private boolean posicaoExiste(int linha, int coluna) {
		return linha >= 0 && linha < this.linhas && coluna >= 0 && coluna < this.colunas;
	}
	public boolean posicaoExiste(Posicao posicao) {
		return posicaoExiste(posicao.getLinha(), posicao.getColuna());
	}
	
	public boolean temPeca(Posicao posicao) {
		if(!posicaoExiste(posicao)) {
			throw new excecaoTabuleiro("Posição não existe");
		}
		return peca(posicao) != null;
	}
}
