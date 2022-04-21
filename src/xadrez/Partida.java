package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class Partida {

	private Tabuleiro tabuleiro;

	public Partida() {
		tabuleiro = new Tabuleiro(8, 8);
		setupInicial();
	}

	public PecaXadrez[][] getpecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];

		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getLinhas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	public PecaXadrez moverPecaXadrez(posicaoXadrez posicaoInicial, posicaoXadrez posicaoFinal) {
		Posicao inicial = posicaoInicial.toPosicao();
		Posicao alvo = posicaoFinal.toPosicao();

		validaPosicaoInicial(inicial);
		validaPosicaoDestino(inicial, alvo);

		Peca pecaCapturada = mover(inicial, alvo);
		return (PecaXadrez) pecaCapturada;
	}

	public boolean[][] movimentosPossiveis(posicaoXadrez posicaoinicial){
		Posicao posicao = posicaoinicial.toPosicao();
		validaPosicaoInicial(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	private Peca mover(Posicao inicio, Posicao destino) {
		Peca p = tabuleiro.removePeca(inicio);
		Peca pecaCapturada = tabuleiro.removePeca(destino);

		tabuleiro.colocarPeca(p, destino);

		return pecaCapturada;
	}

	private void validaPosicaoInicial(Posicao posicao) {
		if (!tabuleiro.temPeca(posicao)) {
			throw new ExcecaoXadrez("Não existe peça na posição de origem");
		}
		if (!tabuleiro.peca(posicao).existeMovimentoPossivel()) {
			throw new ExcecaoXadrez("Não existem movimentos possiveis para a peça escolhida");
		}
	}

	private void validaPosicaoDestino(Posicao inicial, Posicao destino) {
		if (!tabuleiro.peca(inicial).podeMover(destino)) {
			throw new ExcecaoXadrez("A peça escolhida não pode se mover para a posição de destino");
		}
	}

	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new posicaoXadrez(coluna, linha).toPosicao());
	}

	private void setupInicial() {

		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO));
		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
	}
}
