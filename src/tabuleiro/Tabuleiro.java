package tabuleiro;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private Peca[][] pecas;// qual pe�a est� em cada posi��o do tabuleiro

	public Tabuleiro(int linhas, int colunas) {
		if (linhas < 1 || colunas < 1) {
			throw new ExcecaoTabuleiro(
					"Erro ao criar tabuleiro," + " � necess�rio que exista ao menos uma linha e uma " + "coluna");
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
		if (!posicaoExiste(linha, coluna)) {
			throw new ExcecaoTabuleiro("Posi��o n�o existe");
		}
		return pecas[linha][coluna];
	}

	public Peca peca(Posicao posicao) {
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}

	public void colocarPeca(Peca peca, Posicao posicao) {
		if (temPeca(posicao)) {
			throw new ExcecaoTabuleiro("J� existe uma pe�a na posi��o " + posicao);
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}

	public Peca removePeca(Posicao posicao) {
		if (!posicaoExiste(posicao)) {
			throw new ExcecaoTabuleiro("Falha ao acessar posi��o. Esta posi��o n�o existe");
		}
		if (peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;//tirando a pe�a do tabuleiro
		
		pecas[posicao.getLinha()][posicao.getColuna()] = null;//apagando pe�a do tabuleiro
		return aux;//retornando qual pe�a foi retirada
	}

	private boolean posicaoExiste(int linha, int coluna) {
		return linha >= 0 && linha < this.linhas && coluna >= 0 && coluna < this.colunas;
	}

	public boolean posicaoExiste(Posicao posicao) {
		return posicaoExiste(posicao.getLinha(), posicao.getColuna());
	}

	public boolean temPeca(Posicao posicao) {
		if (!posicaoExiste(posicao)) {
			throw new ExcecaoTabuleiro("Posi��o n�o existe");
		}
		return peca(posicao) != null;
	}
}
