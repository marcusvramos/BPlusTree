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
            int pos = folha.procurarPosicaoExclusao(info);
            if (pos < folha.getTl() && folha.getvInfo(pos) == info) {
                if (folha != raiz) {
                    int quantidade = calculaNoLig();
                    if (folha.getTl() > quantidade) {
                        folha.remanejarExclusao(pos);
                        folha.setTl(folha.getTl() - 1);
                        No pai = localizarPai(folha, info);
                        int posPai = pai.procurarPosicaoExclusao(info);
                        if (pai.getvInfo(posPai) == info)
                            pai.setvInfo(posPai, folha.getvInfo(0));
                        else
                            substituirNo(info, folha.getvInfo(0));
                    } else {
                        redistribuicaoFolha(folha, info);
                    }
                } else {
                    folha.remanejarExclusao(pos);
                    folha.setTl(folha.getTl() - 1);
                    if (folha.getTl() == 0)
                        raiz = null;
                }
            }
        }
    }

    private void substituirNo(int info, int infoSubstituir) {
        No noRecebeSubtituir = raiz;
        int pos = noRecebeSubtituir.procurarPosicaoExclusao(info);
        while (noRecebeSubtituir.getvLig(0) != null && noRecebeSubtituir.getvInfo(pos) != info) {
            noRecebeSubtituir = noRecebeSubtituir.getvLig(pos);
            pos = noRecebeSubtituir.procurarPosicaoExclusao(info);
        }
        if (noRecebeSubtituir.getvLig(0) != null) {
            noRecebeSubtituir.setvInfo(pos, infoSubstituir);
        }
    }

    private void redistribuicaoFolha(No no, int info) {
            No pai = localizarPai(no, no.getvInfo(0));
            int posPai = pai.procurarPosicao(no.getvInfo(0));

            int pos = no.procurarPosicaoExclusao(info);

            no.remanejarExclusao(pos);
            no.setTl(no.getTl()-1);

            No irmaEsquerda = (posPai - 1 >= 0) ? pai.getvLig(posPai - 1) : null;
            No irmaDireita = (posPai + 1 <= pai.getTl()) ? pai.getvLig(posPai + 1) : null;

            int qtde = calculaNoLig();

            if (irmaEsquerda != null && irmaEsquerda.getTl() > qtde) {
                redistribuirComIrmaoEsquerdo(no, irmaEsquerda, pai, posPai);
            } else if (irmaDireita != null && irmaDireita.getTl() > qtde) {
                redistribuirComIrmaoDireito(no, irmaDireita, pai, posPai);
            } else if (irmaEsquerda != null) {
                concatenarComIrmaoEsquerdo(no, irmaEsquerda, pai, posPai);
            } else if (irmaDireita != null) {
                concatenarComIrmaoDireito(no, irmaDireita, pai, posPai, pos, info);
            }
    }

    private void redistribuicaoPai(No no) {
        if (no != raiz) {
            int min = calculaNoLig();

            if (no != null && no.getTl() < min) {
                No pai = localizarPai(no, no.getvInfo(0));
                int posPai = pai.procurarPosicao(no.getvInfo(0));

                No irmaE = (posPai - 1 >= 0) ? pai.getvLig(posPai - 1) : null;
                No irmaD = (posPai + 1 <= pai.getTl()) ? pai.getvLig(posPai + 1) : null;

                if (irmaE != null && irmaE.getTl() > min) {
                    redistribuirComIrmaoEsquerdo(no, irmaE, pai, posPai);
                } else if (irmaD != null && irmaD.getTl() > min) {
                    redistribuirComIrmaoDireito(no, irmaD, pai, posPai);
                } else if (irmaD != null) {
                    concatenarComIrmaoDireito(no, irmaD, pai, posPai, 0, 0);
                } else if (irmaE != null) {
                    concatenarComIrmaoEsquerdo(no, irmaE, pai, posPai);
                }
            }
        }
    }

    private void redistribuirComIrmaoEsquerdo(No no, No irmaEsquerda, No pai, int posPai) {
        boolean isFolha = no.getvLig(0) == null;

        if (isFolha) {
            no.remanejar(0);
            no.setvInfo(0, irmaEsquerda.getvInfo(irmaEsquerda.getTl()-1));
            no.setTl(no.getTl() + 1);
            irmaEsquerda.setTl(irmaEsquerda.getTl() - 1);
            pai.setvInfo(posPai - 1, no.getvInfo(0));
        } else {
            no.setvInfo(0,pai.getvInfo(posPai-1));
            no.setvLig(0,irmaEsquerda.getvLig(irmaEsquerda.getTl()));
            no.setTl(no.getTl()+1);
            pai.setvInfo(posPai-1,irmaEsquerda.getvInfo(irmaEsquerda.getTl()-1));
            irmaEsquerda.setTl(irmaEsquerda.getTl()-1);
        }
    }

    private void redistribuirComIrmaoDireito(No no, No irmaDireita, No pai, int posPai) {
        boolean isFolha = no.getvLig(0) == null;

        if (isFolha) {
            no.setvInfo(no.getTl(), irmaDireita.getvInfo(0));
            no.setTl(no.getTl() + 1);
            irmaDireita.remanejarExclusao(0);
            irmaDireita.setTl(irmaDireita.getTl() - 1);
            pai.setvInfo(posPai, irmaDireita.getvInfo(0));
        } else {
            no.setvInfo(no.getTl(),pai.getvInfo(posPai));
            no.setTl(no.getTl()+1);
            no.setvLig(no.getTl(),irmaDireita.getvLig(0));
            pai.setvInfo(posPai,irmaDireita.getvInfo(0));
            irmaDireita.remanejarExclusao(0);
            irmaDireita.setTl(irmaDireita.getTl()-1);
        }
    }

    private void concatenarComIrmaoDireito(No no, No irmaDireita, No pai, int posPai, int pos, int info) {
        boolean isFolha = no.getvLig(0) == null;

        if (isFolha) {
            concatenarFolhaComIrmaoDireito(no, irmaDireita, pai, posPai, pos, info);
        } else {
            concatenarNaoFolhaComIrmaoDireito(no, irmaDireita, pai, posPai);
        }
    }

    private void concatenarFolhaComIrmaoDireito(No no, No irmaDireita, No pai, int posPai, int pos, int info) {
        no.setProx(irmaDireita.getProx());
        if (irmaDireita.getProx() != null) {
            irmaDireita.getProx().setAnt(no);
        }

        for (int i = 0; i < irmaDireita.getTl(); i++) {
            no.setvInfo(no.getTl(), irmaDireita.getvInfo(i));
            no.setTl(no.getTl() + 1);
        }

        pai.remanejarExclusao(posPai);
        pai.setTl(pai.getTl() - 1);
        pai.setvLig(posPai, no);

        if (pai.getTl() == 0) {
            raiz = no;
        } else {
            redistribuicaoPai(pai);
        }

        if (pos == 0) {
            if (posPai == 0) {
                substituirNo(info, no.getvInfo(pos));
            } else {
                pai.setvInfo(posPai - 1, no.getvInfo(0));
            }
        }
    }

    private void concatenarNaoFolhaComIrmaoDireito(No no, No irmaDireita, No pai, int posPai) {
        irmaDireita.remanejar(0);
        irmaDireita.setvInfo(0, pai.getvInfo(posPai));
        irmaDireita.setTl(irmaDireita.getTl() + 1);

        for (int i = no.getTl() - 1; i >= 0; i--) {
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
            redistribuicaoPai(pai);
        }
    }

    private void concatenarComIrmaoEsquerdo(No no, No irmaEsquerda, No pai, int posPai) {
        boolean isFolha = no.getvLig(0) == null;

        if (isFolha) {
            concatenarFolhaComIrmaoEsquerdo(no, irmaEsquerda, pai, posPai);
        } else {
            concatenarNaoFolhaComIrmaoEsquerdo(no, irmaEsquerda, pai, posPai);
        }
    }

    private void concatenarFolhaComIrmaoEsquerdo(No no, No irmaEsquerda, No pai, int posPai) {
        irmaEsquerda.setProx(no.getProx());
        if (irmaEsquerda.getProx() != null) {
            irmaEsquerda.getProx().setAnt(irmaEsquerda);
        }

        for (int i = 0; i < no.getTl(); i++) {
            irmaEsquerda.setvInfo(irmaEsquerda.getTl(), no.getvInfo(i));
            irmaEsquerda.setTl(irmaEsquerda.getTl() + 1);
        }

        pai.remanejarExclusao(posPai - 1);
        pai.setTl(pai.getTl() - 1);
        pai.setvLig(posPai - 1, irmaEsquerda);

        if (pai.getTl() == 0) {
            raiz = irmaEsquerda;
        } else {
            redistribuicaoPai(pai);
        }
    }

    private void concatenarNaoFolhaComIrmaoEsquerdo(No no, No irmaEsquerda, No pai, int posPai) {
        int pos = irmaEsquerda.procurarPosicao(pai.getvInfo(posPai - 1));
        irmaEsquerda.remanejar(pos);
        irmaEsquerda.setvInfo(pos, pai.getvInfo(posPai - 1));
        irmaEsquerda.setTl(irmaEsquerda.getTl() + 1);

        for (int i = 0; i < no.getTl(); i++) {
            irmaEsquerda.setvInfo(irmaEsquerda.getTl(), no.getvInfo(i));
            irmaEsquerda.setvLig(irmaEsquerda.getTl(), no.getvLig(i));
            irmaEsquerda.setTl(irmaEsquerda.getTl() + 1);
        }
        irmaEsquerda.setvLig(irmaEsquerda.getTl(), no.getvLig(no.getTl()));

        pai.remanejarExclusao(posPai - 1);
        pai.setvLig(posPai - 1, irmaEsquerda);
        pai.setTl(pai.getTl() - 1);

        if (pai.getTl() == 0) {
            raiz = irmaEsquerda;
        } else {
            redistribuicaoPai(pai);
        }
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

    private int arredondarCeil(double valor) {
        return (int) Math.ceil(valor);
    }

    private int calculaNoLig() {
        return arredondarCeil((double) No.N / 2) - 1;
    }

    private int calculaNoFolha() {
        return arredondarCeil((double) (No.N - 1) / 2);
    }
}
