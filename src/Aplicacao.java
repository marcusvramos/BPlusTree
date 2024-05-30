public class Aplicacao {
    public static void main(String[] args) {
        BPlusTree b = new BPlusTree();

        for (int i = 1; i <= 500; i++) {
            b.inserir(i);
        }

        System.out.println("Antes da exclusão:");
        b.exibeFolhas();

        for(int i = 1; i <= 400; i++) {
            b.excluir(i);
        }

        System.out.println("\n\nDepois da exclusão:");
        b.exibeFolhas();

        System.out.println("\n\nInOrdem:");
        b.inOrdem(b.getRaiz());
    }
}
