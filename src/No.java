class No {
    public static final int N = 5;
    private int[] vInfo;
    private No[] vLig;
    private int tl;
    private No prox;
    private No ant;

    public No() {
        vInfo = new int[N];
        vLig = new No[N + 1];
        tl = 0;
        prox = null;
        ant = null;
    }

    public No(int info) {
        this();
        vInfo[0] = info;
        tl = 1;
    }

    public int getvInfo(int pos) {
        return vInfo[pos];
    }

    public void setvInfo(int pos, int info) {
        vInfo[pos] = info;
    }

    public No getvLig(int pos) {
        return vLig[pos];
    }

    public void setvLig(int pos, No no) {
        vLig[pos] = no;
    }

    public int getTl() {
        return tl;
    }

    public void setTl(int tl) {
        this.tl = tl;
    }

    public No getProx() {
        return prox;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }

    public No getAnt() {
        return ant;
    }

    public void setAnt(No ant) {
        this.ant = ant;
    }

    public int procurarPosicao(int info) {
        int pos = 0;
        while (pos < tl && vInfo[pos] <= info) {
            pos++;
        }
        return pos;
    }

    public void remanejar(int pos) {
        for (int i = tl; i > pos; i--) {
            vInfo[i] = vInfo[i - 1];
            vLig[i + 1] = vLig[i];
        }
    }

    public void remanejarExclusao(int pos) {
        for (int i = pos; i < tl - 1; i++) {
            vInfo[i] = vInfo[i + 1];
            vLig[i] = vLig[i + 1];
        }
        vLig[tl - 1] = vLig[tl];
    }
}
