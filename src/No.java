public class No {
    private int vInfo[];
    private No vLig[];
    private No ant;
    private No prox;
    private int tl;

    public static final int N = 4;

    public No() {
        this.vInfo = new int[N];
        this.vLig = new No[N + 1];
        this.tl = 0;
        this.ant = null;
        this.prox = null;
    }

    public No(int info) {
        this();
        this.vInfo[0] = info;
        this.tl = 1;
    }

    public int getvInfo(int pos) {
        return vInfo[pos];
    }

    public void setvInfo(int pos, int vInfo) {
        this.vInfo[pos] = vInfo;
    }

    public No getvLig(int pos) {
        return this.vLig[pos];
    }

    public void setvLig(int pos, No vLig) {
        this.vLig[pos] = vLig;
    }

    public int getTl() {
        return tl;
    }

    public void setTl(int tl) {
        this.tl = tl;
    }

    public No getAnt() {
        return this.ant;
    }

    public No getProx() {
        return this.prox;
    }

    public void setAnt(No ant) {
        this.ant = ant;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }

    public int procurarPosicao(int info) {
        int i = 0;
        while (i < tl && info > vInfo[i]) {
            i++;
        }
        return i;
    }

    public void remanejar(int pos) {
        if (tl < vLig.length - 1) {
            vLig[tl + 1] = vLig[tl];
        }
        for (int i = tl; i > pos; i--) {
            vInfo[i] = vInfo[i - 1];
            if (i < vLig.length) {
                vLig[i] = vLig[i - 1];
            }
        }
    }
}
