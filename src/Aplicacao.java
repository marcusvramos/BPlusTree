public class Aplicacao {
    public static void main(String[] args) {
        BPlusTree b = new BPlusTree();

        for (int i = 1; i <= 5000; i++) {
            b.inserir(i);
        }

        System.out.println("Antes da exclusão:");
        b.exibeFolhas();

        for(int i = 2000; i <= 4000; i += 1) {
            b.excluir(i);
        }

        System.out.println("\nDepois da exclusão:");
        b.exibeFolhas();
    }
}
