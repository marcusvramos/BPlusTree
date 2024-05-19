public class Aplicacao {
    public static void main(String[] args) {
        BPlusTree b = new BPlusTree();

        for (int i = 1; i <= 20; i++) {
            b.inserir(i);
        }

        System.out.println("Antes da exclusão:");
        b.exibeFolhas();


        b.excluir(5);
        b.excluir(10);
        b.excluir(15);
        b.excluir(20);
        b.excluir(19);

        System.out.println("Depois da exclusão:");
        b.exibeFolhas();
    }
}
