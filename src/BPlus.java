public class BPlus {
    private No raiz;

    public BPlus() {
        this.raiz = null;
    }

    public No getRaiz() {
        return raiz;
    }

    public void setRaiz(No raiz) {
        this.raiz = raiz;
    }

    public void inserir(int info) {
        No folha = null, pai;
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

        // Copia metade dos elementos para o novo nó cx1
        for (i = 0; i < qtd; i++) {
            cx1.setvInfo(i, folha.getvInfo(i));
            cx1.setTl(cx1.getTl() + 1);
        }

        // Copia a outra metade dos elementos para o novo nó cx2
        for (i = 0; qtd < folha.getTl(); i++, qtd++) {
            cx2.setvInfo(i, folha.getvInfo(qtd));
            cx2.setTl(cx2.getTl() + 1);
        }

        // Liga os nós
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

    public int calculaNoLig() {
        return (No.N / 2) - 1;
    }

    public int calculaNoFolha() {
        return (int) Math.ceil((double) (No.N - 1) / 2);
    }

    public No navegarAteFolha(int info) {
        No aux = raiz;
        int pos;
        while (aux.getvLig(0) != null) {
            pos = aux.procurarPosicao(info);
            aux = aux.getvLig(pos);
        }
        return aux;
    }

    public No localizarPai(No folha, int info) {
        No ant = raiz;
        No aux = raiz;
        int pos;
        while (aux != folha) {
            ant = aux;
            pos = aux.procurarPosicao(info);
            aux = aux.getvLig(pos);
        }
        return ant;
    }

    public void exibeFolhas() {
        No aux = raiz;
        while (aux.getvLig(0) != null)
            aux = aux.getvLig(0);
        while (aux != null) {
            for (int i = 0; i < aux.getTl(); i++)
                System.out.println(aux.getvInfo(i));
            aux = aux.getProx();
        }
    }
}
