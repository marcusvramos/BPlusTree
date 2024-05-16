public class Aplicacao {
    public static void main(String[] args) {
        BPlus b = new BPlus();
        for (int i = 1; i < 10; i++) {
            b.inserir(i);
        }

        b.exibeFolhas();
    }
}
