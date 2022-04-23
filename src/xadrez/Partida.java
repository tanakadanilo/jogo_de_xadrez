package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class Partida {

	private Tabuleiro tabuleiro;
	private int turno;
	private Cor jogadorAtual;
	private boolean check;
	private boolean checkMate;

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

	public boolean getCheckMate() {
		return this.checkMate;
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

		if (testaCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}
		return (PecaXadrez) pecaCapturada;
	}

	public boolean[][] movimentosPossiveis(posicaoXadrez posicaoinicial) {
		Posicao posicao = posicaoinicial.toPosicao();
		validaPosicaoInicial(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}

	private Peca mover(Posicao inicio, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(inicio);
		p.aumentaContMovimentos();

		Peca pecaCapturada = tabuleiro.removePeca(destino);

		tabuleiro.colocarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		return pecaCapturada;
	}

	private void desfazerMover(Posicao inicial, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(destino);
		p.diminuiContMovimentos();

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

	private boolean testaCheckMate(Cor cor) {
		if (!testaCheck(cor)) {
			return false;
		}

		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());

		for (Peca p : lista) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {// caso seja um movimento possível, move a peça e depois confere se saiu do
									// check
						Posicao inicial = ((PecaXadrez) p).getPosicaoXadres().toPosicao();
						Posicao destino = new Posicao(i, j);

						Peca pecaCapturada = mover(inicial, destino);
						boolean testaCheck = testaCheck(cor);
						desfazerMover(inicial, destino, pecaCapturada);

						if (!testaCheck) {// caso o movimento tenha tirado o rei do check
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new posicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void setupInicial() {

		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));

		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO));

		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 8, new Rainha(tabuleiro, Cor.PRETO));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));

		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO));
	}
}
