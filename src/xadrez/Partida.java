package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class Partida {

	private Tabuleiro tabuleiro;
	private int turno;
	private Cor jogadorAtual;
	private boolean check;

	List<Peca> pecasNoTabuleiro = new ArrayList<>();
	List<Peca> pecasCapturadas = new ArrayList<>();

	public Partida() {
		turno = 1;
		jogadorAtual = Cor.BRANCO;
		check = false;
		tabuleiro = new Tabuleiro(8, 8);
		setupInicial();

	}

	public int getTurno() {
		return this.turno;
	}

	public Cor getJogadorAtual() {
		return this.jogadorAtual;
	}
	
	public boolean getCheck() {
		return check; 
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

		if (testaCheck(jogadorAtual)) {
			desfazerMover(inicial, alvo, pecaCapturada);
			throw new ExcecaoXadrez("Você não pode se colocar em xeque");
		}

		check = testaCheck(oponente(jogadorAtual)) ? true : false;// testa se deu check no oponente

		proximoTurno();
		return (PecaXadrez) pecaCapturada;
	}

	public boolean[][] movimentosPossiveis(posicaoXadrez posicaoinicial) {
		Posicao posicao = posicaoinicial.toPosicao();
		validaPosicaoInicial(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}

	private Peca mover(Posicao inicio, Posicao destino) {
		Peca p = tabuleiro.removePeca(inicio);
		Peca pecaCapturada = tabuleiro.removePeca(destino);

		tabuleiro.colocarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		return pecaCapturada;
	}

	private void desfazerMover(Posicao inicial, Posicao destino, Peca pecaCapturada) {
		Peca p = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, inicial);

		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}
	}

	private void validaPosicaoInicial(Posicao posicao) {
		if (!tabuleiro.temPeca(posicao)) {
			throw new ExcecaoXadrez("Não existe peça na posição de origem");
		}
		if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new ExcecaoXadrez("A peça escolhida não é sua!");
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

	private void proximoTurno() {
		this.turno++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;// mudando a cor do jogador de acordo com a
																				// condicional
	}

	private Cor oponente(Cor cor) {
		return (cor == cor.BRANCO) ? cor.PRETO : cor.BRANCO;
	}

	private PecaXadrez rei(Cor cor) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : lista) {
			if (p instanceof Rei) {
				return (PecaXadrez) p;
			}
		}

		throw new IllegalStateException("Não existe nenhum Rei da cor: " + cor + " no tabuleiro.");
	}

	private boolean testaCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadres().toPosicao();
		List<Peca> pecasOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for (Peca p : pecasOponente) {// passando por todas as peças do oponente
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {// caso alguma peça possa captturar o rei
				return true;
			}
		}
		return false;// caso saiu do for, significa q nenhuma peça pode capturar o rei nessa rodada
	}

	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new posicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
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
