import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class App {
    static final int[] tamanhosTesteGrande =  { 31_250_000, 62_500_000, 125_000_000, 250_000_000, 500_000_000 };
    static final int[] tamanhosTesteMedio =   {     12_500,     25_000,      50_000,     100_000,     200_000 };
    static final int[] tamanhosTestePequeno = {          3,          6,          12,          24,          48 };
    static Random aleatorio = new Random();
    static long operacoes;
    static double nanoToMilli = 1.0/1_000_000;
    

    /**
     * Gerador de vetores aleatórios de tamanho pré-definido. 
     * @param tamanho Tamanho do vetor a ser criado.
     * @return Vetor com dados aleatórios, com valores entre 1 e (tamanho/2), desordenado.
     */
    static int[] gerarVetor(int tamanho){
        int[] vetor = new int[tamanho];
        int max = Math.max(1, tamanho / 2);
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = 1 + aleatorio.nextInt(max);
        }
        return vetor;        
    }

    /**
     * Gerador de vetores de objetos do tipo Integer aleatórios de tamanho pré-definido. 
     * @param tamanho Tamanho do vetor a ser criado.
     * @return Vetor de Objetos Integer com dados aleatórios, com valores entre 1 e (tamanho/2), desordenado.
     */
    static Integer[] gerarVetorObjetos(int tamanho) {
        Integer[] vetor = new Integer[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = 1 + aleatorio.nextInt(10 * tamanho);
        }
        return vetor;
    }


    public static void main(String[] args) {
    Scanner teclado = new Scanner(System.in);
    int[] tam = tamanhosTesteMedio;
    menu(teclado, tam);
    }

    public static void menu(Scanner teclado, int[] tam){
        System.out.println("Bem vindo ao sistema, nele você poderá testar diferentes métodos de organização para um mesmo grupo de dados aleatório");
        int menu = -1;
        while (menu != 0) {
            System.out.println("Digite o numero correspondente ao metodo desesjado:");
            System.out.println("Bubble = 1\nInsertion = 2\nSelection = 3\nMerge = 4\nSair = 0");
            menu = teclado.nextInt();
            switch (menu) {
                case 1:
                    fazBubble(tam);
                    break;

                case 2:
                    fazInsertion(tam);
                    break;
                case 3:
                    fazSelection(tam);
                    break;

                case 4:
                    fazMerge(tam);
                    break;
                case 0:
                    System.out.println("Finalizando sistema...");
                    break;

                default:
                    System.err.println("Valor inválido");
                    break;
            }
        }
    }

    public static void fazBubble(int[] tam){
        for (int i = 0; i < tam.length; i++) {
            Integer[] vetor = gerarVetorObjetos(tam[i]);
            BubbleSort<Integer> bolha = new BubbleSort<>();
            bolha.ordenar(vetor);
            System.out.println("\nVetor ordenado método Bolha:");
            System.out.println("Comparações: " + bolha.getComparacoes());
            System.out.println("Movimentações: " + bolha.getMovimentacoes());
            System.out.println("Tempo de ordenação (ms): " + bolha.getTempoOrdenacao());
        }
    }

    public static void fazInsertion(int tam[]){
        for (int i = 0; i<tam.length; i++) {
            Integer[] vetor = gerarVetorObjetos(tam[i]);   
            InsertionSort<Integer> insertion = new InsertionSort<>();
            Integer[] vetorOrdenadoInsertion = insertion.ordenar(vetor);
            System.out.println("\nVetor ordenado método insertion:");
            System.out.println("Comparações: " + insertion.getComparacoes());
            System.out.println("Movimentações: " + insertion.getMovimentacoes());
            System.out.println("Tempo de ordenação (ms): " + insertion.getTempoOrdenacao());
        }
    }

    public static void fazSelection(int tam[]){
        for (int i = 0; i<tam.length; i++) {
            Integer[] vetor = gerarVetorObjetos(tam[i]);   
            SelectionSort<Integer> Selection = new SelectionSort<>();
            Integer[] vetorOrdenadoSelection = Selection.ordenar(vetor);
            System.out.println("\nVetor ordenado método Selection:");
            System.out.println("Comparações: " + Selection.getComparacoes());
            System.out.println("Movimentações: " + Selection.getMovimentacoes());
            System.out.println("Tempo de ordenação (ms): " + Selection.getTempoOrdenacao());
        }
    }

    public static void fazMerge(int tam[]){
        for (int i = 0; i<tam.length; i++) {
            Integer[] vetor = gerarVetorObjetos(tam[i]);   
            MergeSort<Integer> Merge = new MergeSort<>();
            Integer[] vetorOrdenadoSelection = Merge.ordenar(vetor);
            System.out.println("\nVetor ordenado método Merge:");
            System.out.println("Comparações: " + Merge.getComparacoes());
            System.out.println("Movimentações: " + Merge.getMovimentacoes());
            System.out.println("Tempo de ordenação (ms): " + Merge.getTempoOrdenacao());
        }
    }
}
