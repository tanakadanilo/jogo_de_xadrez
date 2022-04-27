package xadrez;

import java.security.InvalidParameterException;
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
	private PecaXadrez vulneravelEnPeassant;
	private PecaXadrez promovida;

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

	public PecaXadrez getvulneravelEnPeassant() {
		return this.vulneravelEnPeassant;
	}

	public PecaXadrez getPromovida() {
		return this.promovida;
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

		// jogada especial promocao

		if (testaCheck(jogadorAtual)) {// caso o jogador tenha colocado a si mesmo em check
			desfazerMover(inicial, alvo, pecaCapturada);
			throw new ExcecaoXadrez("Você não pode se colocar em xeque");
		}

		PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.peca(alvo);

		promovida = null;
		if (pecaMovida instanceof Peao) {
			if (pecaMovida.getCor() == Cor.BRANCO && alvo.getLinha() == 0
					|| pecaMovida.getCor() == Cor.PRETO && alvo.getLinha() == 7) {

				promovida = (PecaXadrez) tabuleiro.peca(alvo);
				promovida = trocarPecaPromovida("Q");
			}
		}
		check = testaCheck(oponente(jogadorAtual)) ? true : false;// testa se deu check no oponente

		if (testaCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}

		// movimento especial enPeassant
		if (pecaMovida instanceof Peao
				&& (alvo.getLinha() == inicial.getLinha() + 2 || alvo.getLinha() == inicial.getLinha() - 2)) {
			vulneravelEnPeassant = pecaMovida;
		} else {
			vulneravelEnPeassant = null;
		}
		return (PecaXadrez) pecaCapturada;
	}

	public boolean[][] movimentosPossiveis(posicaoXadrez posicaoinicial) {
		Posicao posicao = posicaoinicial.toPosicao();
		validaPosicaoInicial(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}

	public PecaXadrez trocarPecaPromovida(String tipo) {
		if (promovida == null) {// caso não tenha peça a ser promovida
			throw new IllegalStateException("Não existe peça para ser promovida");
		}
		if (!tipo.equalsIgnoreCase("Q") && !tipo.equalsIgnoreCase("B") && !tipo.equalsIgnoreCase("C")
				&& !tipo.equalsIgnoreCase("T")) {
			return promovida;
		}

		Posicao posicao = promovida.getPosicaoXadres().toPosicao();

		Peca p = tabuleiro.removePeca(posicao);
		pecasNoTabuleiro.remove(p);// retirando peão do jogo, para ser substituido

		PecaXadrez novaPeca = novaPeca(tipo, promovida.getCor());

		tabuleiro.colocarPeca(novaPeca, posicao);
		pecasNoTabuleiro.add(novaPeca);

		return novaPeca;
	}

	private PecaXadrez novaPeca(String tipo, Cor cor) {
		if (tipo.equalsIgnoreCase("Q")) {
			return new Rainha(tabuleiro, cor);
		}
		if (tipo.equalsIgnoreCase("B")) {
			return new Bispo(tabuleiro, cor);
		}
		if (tipo.equalsIgnoreCase("C")) {
			return new Cavalo(tabuleiro, cor);
		}

		// sem if para não dar problema de compilação
		return new Torre(tabuleiro, cor);
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

		// movimento especial castling pequeno
		if (p instanceof Rei && destino.getColuna() == inicio.getColuna() + 2) {
			Posicao origemTorre = new Posicao(inicio.getLinha(), inicio.getColuna() + 3);
			Posicao DestinoTorre = new Posicao(inicio.getLinha(), inicio.getColuna() + 1);

			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(origemTorre);
			tabuleiro.colocarPeca(torre, DestinoTorre);

			torre.aumentaContMovimentos();
		}

		// movimento especial castling grande
		if (p instanceof Rei && destino.getColuna() == inicio.getColuna() - 2) {
			Posicao origemTorre = new Posicao(inicio.getLinha(), inicio.getColuna() - 4);
			Posicao DestinoTorre = new Posicao(inicio.getLinha(), inicio.getColuna() - 1);

			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(origemTorre);
			tabuleiro.colocarPeca(torre, DestinoTorre);

			torre.aumentaContMovimentos();
		}

		// movimento especial enPeassant
		if (p instanceof Peao) {
			if (inicio.getColuna() != destino.getColuna() && pecaCapturada == null) {// caso o peao atacou uma casa
																						// vazia, foi um enPeassant

				Posicao posicaoPeaoCapturado;

				if (p.getCor() == Cor.BRANCO) {
					posicaoPeaoCapturado = new Posicao(destino.getLinha() + 1, destino.getColuna());
				} else {
					posicaoPeaoCapturado = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}

				pecaCapturada = tabuleiro.removePeca(posicaoPeaoCapturado);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);

			}
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

		// movimento especial castling pequeno
		if (p instanceof Rei && destino.getColuna() == inicial.getColuna() + 2) {
			Posicao origemTorre = new Posicao(inicial.getLinha(), inicial.getColuna() + 3);
			Posicao DestinoTorre = new Posicao(inicial.getLinha(), inicial.getColuna() + 1);

			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(DestinoTorre);
			tabuleiro.colocarPeca(torre, origemTorre);

			torre.diminuiContMovimentos();
		}

		// movimento especial castling grande
		if (p instanceof Rei && destino.getColuna() == inicial.getColuna() - 2) {
			Posicao origemTorre = new Posicao(inicial.getLinha(), inicial.getColuna() - 4);
			Posicao DestinoTorre = new Posicao(inicial.getLinha(), inicial.getColuna() - 1);

			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(DestinoTorre);
			tabuleiro.colocarPeca(torre, origemTorre);

			torre.diminuiContMovimentos();
		}

		// movimento especial enPeassant
		if (p instanceof Peao) {
			if (inicial.getColuna() != destino.getColuna() && pecaCapturada == vulneravelEnPeassant) {// caso o peao
																										// tenha feito
																										// um enPeassant

				PecaXadrez peao = (PecaXadrez) tabuleiro.removePeca(destino);// a peça foi devolvida para a posição
																				// errada, por essa jogada fugir ao
																				// padrao do xadrez, então mover para a
																				// posição certa
				Posicao posicaoPeaoCapturado;

				if (p.getCor() == Cor.BRANCO) {
					posicaoPeaoCapturado = new Posicao(3, destino.getColuna());
				} else {
					posicaoPeaoCapturado = new Posicao(4, destino.getColuna());
				}

				tabuleiro.colocarPeca(peao, posicaoPeaoCapturado);// colocando peao na posicao certa

			}
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
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));

		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));

		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));

		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));
	}
}
