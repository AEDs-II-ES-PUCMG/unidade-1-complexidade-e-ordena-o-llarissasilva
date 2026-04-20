
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * MIT License
 *
 * Copyright(c) 2022-25 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

public class AppOficina {

    static final int MAX_PEDIDOS = 100;
    static Produto[] produtos;
    static Produto[] produtosPorIdentificador;
    static Produto[] produtosPorDescricao;
    static int quantProdutos = 0;
    static String nomeArquivoDados = "produtos.txt";

    // #region utilidades
    static Scanner teclado;

    static <T extends Number> T lerNumero(String mensagem, Class<T> classe) {
        System.out.print(mensagem + ": ");
        T valor;
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Tecle Enter para continuar.");
        teclado.nextLine();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("XULAMBS COMÉRCIO DE COISINHAS v0.2\n================");
    }

    static int exibirMenuPrincipal() {
        cabecalho();
        System.out.println("1 - Procurar produto");
        System.out.println("2 - Filtrar produtos por preço máximo");
        System.out.println("3 - Ordenar produtos");
        System.out.println("4 - Embaralhar produtos");
        System.out.println("5 - Listar produtos");
        System.out.println("0 - Finalizar");

        Integer op = lerNumero("Digite sua opção", Integer.class);
        return op != null ? op : -1;
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção");
        System.out.println("3 - Seleção");
        System.out.println("4 - Mergesort");
        System.out.println("0 - Voltar");

        Integer op = lerNumero("Digite sua opção", Integer.class);
        return op != null ? op : -1;
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Por código");
        System.out.println("2 - Por descrição");

        Integer op = lerNumero("Digite sua opção", Integer.class);
        return op != null ? op : -1;
    }

    // #endregion

    static Produto[] carregarProdutos(String nomeArquivo) {
        Scanner dados;
        Produto[] dadosCarregados;
        quantProdutos = 0;
        try {
            dados = new Scanner(new File(nomeArquivo));
            int tamanho = Integer.parseInt(dados.nextLine());

            dadosCarregados = new Produto[tamanho];
            while (dados.hasNextLine()) {
                Produto novoProduto = Produto.criarDoTexto(dados.nextLine());
                dadosCarregados[quantProdutos] = novoProduto;
                quantProdutos++;
            }
            dados.close();
        } catch (FileNotFoundException fex) {
            System.out.println("Arquivo não encontrado. Produtos não carregados");
            dadosCarregados = null;
        }
        return dadosCarregados;
    }

    static void prepararIndicesOrdenados() {
        if (quantProdutos == 0) {
            produtosPorIdentificador = null;
            produtosPorDescricao = null;
            return;
        }
        Produto[] base = Arrays.copyOf(produtos, quantProdutos);
        IOrdenador<Produto> mergeId = new MergeSort<>();
        produtosPorIdentificador = mergeId.ordenar(Arrays.copyOf(base, base.length), new ComparadorPorCodigo());

        IOrdenador<Produto> mergeDesc = new MergeSort<>();
        produtosPorDescricao = mergeDesc.ordenar(Arrays.copyOf(base, base.length), new ComparadorPorDescricao());
    }

    static Produto buscaBinariaPorIdentificador(int id) {
        if (produtosPorIdentificador == null || quantProdutos == 0) {
            return null;
        }
        int esq = 0;
        int dir = quantProdutos - 1;
        while (esq <= dir) {
            int meio = (esq + dir) / 2;
            int cod = produtosPorIdentificador[meio].hashCode();
            if (cod == id) {
                return produtosPorIdentificador[meio];
            }
            if (cod < id) {
                esq = meio + 1;
            } else {
                dir = meio - 1;
            }
        }
        return null;
    }

    static Produto buscaBinariaPorDescricao(String texto) {
        if (produtosPorDescricao == null || quantProdutos == 0 || texto == null) {
            return null;
        }
        String chave = texto.trim();
        int esq = 0;
        int dir = quantProdutos - 1;
        while (esq <= dir) {
            int meio = (esq + dir) / 2;
            int cmp = chave.compareTo(produtosPorDescricao[meio].getDescricao());
            if (cmp == 0) {
                return produtosPorDescricao[meio];
            }
            if (cmp < 0) {
                dir = meio - 1;
            } else {
                esq = meio + 1;
            }
        }
        return null;
    }

    static Produto localizarProduto() {
        cabecalho();
        System.out.println("Localizando um produto");
        System.out.println("1 - Por identificador");
        System.out.println("2 - Por descrição");
        Integer modo = lerNumero("Opção", Integer.class);
        if (modo == null) {
            return null;
        }
        if (modo == 1) {
            Integer id = lerNumero("Identificador do produto", Integer.class);
            if (id == null) {
                return null;
            }
            return buscaBinariaPorIdentificador(id);
        }
        if (modo == 2) {
            System.out.print("Descrição: ");
            String desc = teclado.nextLine();
            return buscaBinariaPorDescricao(desc);
        }
        return null;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        String mensagem = "Dados inválidos";

        if (produto != null) {
            mensagem = String.format("Dados do produto:\n%s", produto);
        }

        System.out.println(mensagem);
    }

    private static void filtrarPorPrecoMaximo() {
        cabecalho();
        System.out.println("Filtrando por valor máximo:");
        Double valor = lerNumero("valor", Double.class);
        if (valor == null) {
            return;
        }
        StringBuilder relatorio = new StringBuilder();
        for (int i = 0; i < quantProdutos; i++) {
            if (produtos[i].valorDeVenda() < valor) {
                relatorio.append(produtos[i]).append("\n");
            }
        }
        System.out.println(relatorio.toString());
    }

    static Comparator<Produto> criarComparador(int opcaoCriterio) {
        switch (opcaoCriterio) {
            case 1:
                return new ComparadorPorCodigo();
            case 2:
                return new ComparadorPorDescricao();
            default:
                return null;
        }
    }

    static IOrdenador<Produto> criarOrdenador(int opcaoAlgoritmo) {
        switch (opcaoAlgoritmo) {
            case 1:
                return new BubbleSort<>();
            case 2:
                return new InsertionSort<>();
            case 3:
                return new SelectionSort<>();
            case 4:
                return new MergeSort<>();
            default:
                return null;
        }
    }

    static void executarOrdenacao(int opcaoAlgoritmo, Comparator<Produto> comparador) {
        if (comparador == null || quantProdutos == 0) {
            return;
        }
        IOrdenador<Produto> ordenador = criarOrdenador(opcaoAlgoritmo);
        if (ordenador == null) {
            return;
        }
        Produto[] copia = Arrays.copyOf(produtos, quantProdutos);
        ordenador.ordenar(copia, comparador);
        System.out.println("\nComparações: " + ordenador.getComparacoes());
        System.out.println("Movimentações: " + ordenador.getMovimentacoes());
        System.out.println("Tempo de ordenação (ms): " + ordenador.getTempoOrdenacao());
    }

    static void ordenarProdutos() {
        int menu = -1;
        while (menu != 0) {
            menu = exibirMenuOrdenadores();
            if (menu == 0) {
                break;
            }
            if (menu < 1 || menu > 4) {
                System.err.println("Valor inválido para o método de ordenação.");
                continue;
            }
            int crit = exibirMenuComparadores();
            if (crit != 1 && crit != 2) {
                System.err.println("Critério inválido.");
                continue;
            }
            Comparator<Produto> comp = criarComparador(crit);
            executarOrdenacao(menu, comp);
        }
    }

    static void embaralharProdutos() {
        Collections.shuffle(Arrays.asList(produtos));
    }

    static void verificarSubstituicao(Produto[] dadosOriginais, Produto[] copiaDados) {
        cabecalho();
        System.out.print("Deseja sobrescrever os dados originais pelos ordenados (S/N)?");
        String resposta = teclado.nextLine().toUpperCase();
        if (resposta.equals("S")) {
            dadosOriginais = Arrays.copyOf(copiaDados, copiaDados.length);
        }
    }

    static void listarProdutos() {
        cabecalho();
        for (int i = 0; i < quantProdutos; i++) {
            System.out.println(produtos[i]);
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in);

        produtos = carregarProdutos(nomeArquivoDados);
        prepararIndicesOrdenados();
        embaralharProdutos();

        int opcao = -1;

        do {
            opcao = exibirMenuPrincipal();
            switch (opcao) {
                case 1:
                    mostrarProduto(localizarProduto());
                    break;
                case 2:
                    filtrarPorPrecoMaximo();
                    break;
                case 3:
                    ordenarProdutos();
                    break;
                case 4:
                    embaralharProdutos();
                    break;
                case 5:
                    listarProdutos();
                    break;
                case 0:
                    System.out.println("FLW VLW OBG VLT SMP.");
                    break;
                default:
                    break;
            }
            if (opcao != 0) {
                pausa();
            }
        } while (opcao != 0);
        teclado.close();
    }
}
