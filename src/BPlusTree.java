public class BPlusTree {
    private No raiz;

    public BPlusTree() {
        this.raiz = null;
    }

    public void inserir(int info) {
        No folha, pai;
        int pos;
        if (raiz == null) {
            raiz = new No(info);
        } else {
            folha = navegarAteFolha(info);
            pos = folha.procurarPosicao(info);
            folha.remanejar(pos);
            folha.setvInfo(pos, info);
            folha.setTl(folha.getTl() + 1);
            if (folha.getTl() == No.N) {
                pai = localizarPai(folha, info);
                int qtd = calculaNoFolha();
                splitFolha(folha, pai, qtd);
            }
        }
    }

    private void splitFolha(No folha, No pai, int qtd) {
        No cx1 = new No();
        No cx2 = new No();
        No auxAnt, auxProx;
        int i;

        for (i = 0; i < qtd; i++) {
            cx1.setvInfo(i, folha.getvInfo(i));
            cx1.setTl(cx1.getTl() + 1);
        }

        for (i = 0; qtd < folha.getTl(); i++, qtd++) {
            cx2.setvInfo(i, folha.getvInfo(qtd));
            cx2.setTl(cx2.getTl() + 1);
        }

        cx1.setProx(cx2);
        cx2.setAnt(cx1);

        if (folha.getAnt() != null) {
            auxAnt = folha.getAnt();
            auxAnt.setProx(cx1);
            cx1.setAnt(auxAnt);
        }

        if (folha.getProx() != null) {
            auxProx = folha.getProx();
            auxProx.setAnt(cx2);
            cx2.setProx(auxProx);
        }

        if (folha == pai) {
            pai.setvInfo(0, cx2.getvInfo(0));
            pai.setvLig(0, cx1);
            pai.setvLig(1, cx2);
            pai.setTl(1);
        } else {
            int pos = pai.procurarPosicao(cx2.getvInfo(0));
            pai.remanejar(pos);
            pai.setvInfo(pos, cx2.getvInfo(0));
            pai.setTl(pai.getTl() + 1);
            pai.setvLig(pos, cx1);
            pai.setvLig(pos + 1, cx2);

            if (pai.getTl() > No.N - 1) {
                folha = pai;
                pai = localizarPai(folha, folha.getvInfo(0));
                int qtdLig = calculaNoLig();
                splitNaoFolha(folha, pai, qtdLig);
            }
        }
    }

    private void splitNaoFolha(No folha, No pai, int qtd) {
        No cx1 = new No();
        No cx2 = new No();

        for (int i = 0; i < qtd; i++) {
            cx1.setvInfo(i, folha.getvInfo(i));
            cx1.setvLig(i, folha.getvLig(i));
            cx1.setTl(cx1.getTl() + 1);
        }
        cx1.setvLig(qtd, folha.getvLig(qtd));

        for (int j = qtd + 1, i = 0; j < folha.getTl(); i++, j++) {
            cx2.setvInfo(i, folha.getvInfo(j));
            cx2.setvLig(i, folha.getvLig(j));
            cx2.setTl(cx2.getTl() + 1);
        }
        cx2.setvLig(cx2.getTl(), folha.getvLig(folha.getTl()));

        if (folha == pai) {
            pai.setvInfo(0, folha.getvInfo(qtd));
            pai.setvLig(0, cx1);
            pai.setvLig(1, cx2);
            pai.setTl(1);
        } else {
            int pos = pai.procurarPosicao(folha.getvInfo(qtd));
            pai.remanejar(pos);
            pai.setvInfo(pos, folha.getvInfo(qtd));
            pai.setvLig(pos, cx1);
            pai.setvLig(pos + 1, cx2);
            pai.setTl(pai.getTl() + 1);

            if (pai.getTl() > No.N - 1) {
                folha = pai;
                pai = localizarPai(folha, folha.getvInfo(0));
                int qtdLig = calculaNoLig();
                splitNaoFolha(folha, pai, qtdLig);
            }
        }
    }

    public void excluir(int info) {
        No folha = navegarAteFolha(info);
        if (folha != null) {
            int pos = folha.procurarPosicao(info);
            if (pos > 0 && pos <= folha.getTl() && folha.getvInfo(pos - 1) == info) {
                folha.remanejarExclusao(pos - 1);
                folha.setTl(folha.getTl() - 1);
                if (folha.getTl() == 0) {
                    if (folha == raiz) {
                        raiz = null;
                    } else {
                        No pai = localizarPai(folha, info);
                        int posPai = pai.procurarPosicao(info);
                        pai.remanejarExclusao(posPai - 1);
                        pai.setTl(pai.getTl() - 1);
                        if (pai.getTl() == 0) {
                            redistribuicao(pai);
                        }
                    }
                } else if (folha != raiz && folha.getTl() < calculaNoLig()) {
                    redistribuicao(folha);
                }
            }
        }
    }

    private void redistribuicao(No no) {
        if (no == raiz) {
            return;
        }

        No pai = localizarPai(no, no.getvInfo(0));
        int posPai = pai.procurarPosicao(no.getvInfo(0));
        No irmaEsquerda = (posPai - 1 >= 0) ? pai.getvLig(posPai - 1) : null;
        No irmaDireita = (posPai + 1 <= pai.getTl()) ? pai.getvLig(posPai + 1) : null;

        int min = calculaNoLig();

        if (irmaEsquerda != null && irmaEsquerda.getTl() > min) {
            redistribuirComIrmaoEsquerdo(no, irmaEsquerda, pai, posPai);
        } else if (irmaDireita != null && irmaDireita.getTl() > min) {
            redistribuirComIrmaoDireito(no, irmaDireita, pai, posPai);
        } else if (irmaDireita != null) {
            concatenarComIrmaoDireito(no, irmaDireita, pai, posPai);
        } else if (irmaEsquerda != null) {
            concatenarComIrmaoEsquerdo(no, irmaEsquerda, pai, posPai);
        }
    }

    private void redistribuirComIrmaoEsquerdo(No no, No irmaEsquerda, No pai, int posPai) {
        no.remanejar(0);
        no.setvInfo(0, pai.getvInfo(posPai - 1));
        no.setvLig(0, irmaEsquerda.getvLig(irmaEsquerda.getTl()));
        no.setTl(no.getTl() + 1);

        pai.setvInfo(posPai - 1, irmaEsquerda.getvInfo(irmaEsquerda.getTl() - 1));
        irmaEsquerda.setTl(irmaEsquerda.getTl() - 1);
    }

    private void redistribuirComIrmaoDireito(No no, No irmaDireita, No pai, int posPai) {
        no.setvInfo(no.getTl(), pai.getvInfo(posPai));
        no.setTl(no.getTl() + 1);
        no.setvLig(no.getTl(), irmaDireita.getvLig(0));

        pai.setvInfo(posPai, irmaDireita.getvInfo(0));
        irmaDireita.remanejarExclusao(0);
        irmaDireita.setTl(irmaDireita.getTl() - 1);
    }

    private void concatenarComIrmaoDireito(No no, No irmaDireita, No pai, int posPai) {
        for (int i = 0; i < no.getTl(); i++) {
            irmaDireita.remanejar(0);
            irmaDireita.setvInfo(0, no.getvInfo(i));
            irmaDireita.setvLig(1, no.getvLig(i + 1));
        }
        irmaDireita.setvLig(0, no.getvLig(0));
        irmaDireita.setTl(irmaDireita.getTl() + no.getTl());

        pai.remanejarExclusao(posPai);
        pai.setTl(pai.getTl() - 1);

        if (pai.getTl() == 0) {
            raiz = irmaDireita;
        } else {
            redistribuicao(pai);
        }
    }

    private void concatenarComIrmaoEsquerdo(No no, No irmaEsquerda, No pai, int posPai) {
        for (int i = 0; i < no.getTl(); i++) {
            irmaEsquerda.setvInfo(irmaEsquerda.getTl(), no.getvInfo(i));
            irmaEsquerda.setvLig(irmaEsquerda.getTl(), no.getvLig(i));
            irmaEsquerda.setTl(irmaEsquerda.getTl() + 1);
        }
        irmaEsquerda.setvLig(irmaEsquerda.getTl(), no.getvLig(no.getTl()));

        pai.remanejarExclusao(posPai - 1);
        pai.setTl(pai.getTl() - 1);

        if (pai.getTl() == 0) {
            raiz = irmaEsquerda;
        } else {
            redistribuicao(pai);
        }
    }

    public int calculaNoLig() {
        return (No.N / 2) - 1;
    }

    public int calculaNoFolha() {
        return (int) Math.ceil((double) (No.N - 1) / 2);
    }

    public No navegarAteFolha(int info) {
        No aux = raiz;
        int pos;
        while (aux != null && aux.getvLig(0) != null) {
            pos = aux.procurarPosicao(info);
            aux = aux.getvLig(pos);
        }
        return aux;
    }

    public No localizarPai(No folha, int info) {
        No ant = raiz;
        No aux = raiz;
        int pos;
        while (aux != null && aux != folha) {
            ant = aux;
            pos = aux.procurarPosicao(info);
            aux = aux.getvLig(pos);
        }
        return aux == folha ? ant : null;
    }

    public void exibeFolhas() {
        if (raiz == null) {
            System.out.println("A árvore está vazia.");
            return;
        }

        No aux = raiz;
        while (aux.getvLig(0) != null) {
            aux = aux.getvLig(0);
        }
        while (aux != null) {
            for (int i = 0; i < aux.getTl(); i++) {
                System.out.println(aux.getvInfo(i));
            }
            aux = aux.getProx();
        }
    }
}
