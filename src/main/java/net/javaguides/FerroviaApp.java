package net.javaguides;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import net.javaguides.springboot.controller.AStarController;
import net.javaguides.springboot.controller.AStarFerroviaController;
import net.javaguides.springboot.controller.FerroviaController;
import net.javaguides.springboot.controller.GrafoController;
import net.javaguides.springboot.controller.KruskalController;
import net.javaguides.springboot.controller.RodoviaController;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Ferrovia;
import net.javaguides.springboot.model.Grafo;
import net.javaguides.springboot.model.Rodovia;
import net.javaguides.springboot.controller.AlgoritimoGenericoController;



public class FerroviaApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        GrafoController grafoController = new GrafoController();
        Grafo grafo = grafoController.criarGrafo();

        System.out.println("===== ESCOLHA O ALGORITMO =====");
        System.out.println("1 - Kruskal");
        System.out.println("2 - A*");
        System.out.println("3 - A* Ferrovia");
        System.out.println("4 - Algoritmo Genetico");
        System.out.print("Opção: ");

        int opcao = scanner.nextInt();

       switch (opcao) {
    case 1:
        executarKruskal(grafo);
        break;
    case 2:
        executarAStar(grafo, scanner);
        break;
    case 3:
        executarAStarFerrovia(grafo, scanner);
        break;
    case 4:
        executarAlgoritmoGenetico(grafo);
        break;
    default:
        System.out.println("Opção inválida. Encerrando o programa.");
        break;
}
    }

    public static void executarKruskal(Grafo grafo) {
        KruskalController kruskalController = new KruskalController();
        ArrayList<Aresta> agm = new ArrayList<>(kruskalController.kruskal(grafo));
        FerroviaController ferroviaController = new FerroviaController();
        ArrayList<Ferrovia> ferrovias = ferroviaController.criarFerrovias(agm);
        ArrayList<Aresta> arestasNaoUsadas = new ArrayList<>(kruskalController.obterArestasNaoUsadas(grafo, agm));

        RodoviaController rodoviaController = new RodoviaController();
        ArrayList<Rodovia> rodovias = rodoviaController.criarRodovias(arestasNaoUsadas);

        System.out.println("\n===== FERROVIAS (AGM - KRUSKAL) =====");
        double totalFerrovias = 0;
        int contador = 1;

        for (Ferrovia ferrovia : ferrovias) {
            Aresta aresta = ferrovia.getAresta();
            System.out.println(
                contador + " - " +
                aresta.getOrigem().getNome() + " <-> " +
                aresta.getDestino().getNome() + " | " +
                aresta.getDistancia() + " km"
            );
            totalFerrovias += aresta.getDistancia();
            contador++;
        }
        System.out.println("Total de conexões ferroviárias: " + ferrovias.size());
        System.out.println("Distância total das ferrovias: " + totalFerrovias + " km");
        System.out.println("Custo total da construção das ferrovias: R$" + ferroviaController.calcularCustoTotal(ferrovias));


        System.out.println("\n===== RODOVIAS (ARESTAS NÃO USADAS) =====");
        double totalRodovias = 0;
        contador = 1;

        for (Rodovia rodovia : rodovias) {
            Aresta aresta = rodovia.getAresta();
            System.out.println(
                contador + " - " +
                aresta.getOrigem().getNome() + " <-> " +
                aresta.getDestino().getNome() + " | " +
                aresta.getDistancia() + " km"
            );
            totalRodovias += aresta.getDistancia();
            contador++;
        }

        System.out.println("Total de conexões rodoviárias: " + rodovias.size());
        System.out.println("Distância total das rodovias: " + totalRodovias + " km");
    }

    public static void executarAStar(Grafo grafo, Scanner scanner) {
        AStarController aStarController = new AStarController();

        System.out.println("===== ROTA A* =====");

        for (int i = 0; i < grafo.getCidades().size(); i++) {
            System.out.println(i + " - " + grafo.getCidades().get(i).getNome());
        }

        System.out.print("Origem: ");
        int indiceOrigem = scanner.nextInt();

        System.out.print("Destino: ");
        int indiceDestino = scanner.nextInt();

        Cidade origem = grafo.getCidades().get(indiceOrigem);
        Cidade destino = grafo.getCidades().get(indiceDestino);

        List<Cidade> rota = aStarController.buscarRota(grafo, origem, destino);

        if (rota.isEmpty()) {
            System.out.println("Rota não encontrada.");
            return;
        }

        double custo = aStarController.calcularCusto(rota, grafo);
        double km = custo / 5;

        System.out.println("Melhor rota:");

        for (Cidade cidade : rota) {
            System.out.print(cidade.getNome());

            if (!cidade.equals(rota.get(rota.size() - 1))) {
                System.out.print(" -> ");
            }
        }

        System.out.println();
        System.out.println("Km total: " + km + " km");
        System.out.println("Custo: R$ " + custo);
    }

    public static void executarAStarFerrovia(Grafo grafo, Scanner scanner){
        AStarFerroviaController aStar = new AStarFerroviaController();

        System.out.println("===== ROTA A* FERROVIA =====");

        for ( int i = 0; i< grafo.getCidades().size(); i++){
            System.out.println(i+ "-" + grafo.getCidades().get(i).getNome());
        }

        System.out.println("Origem: ");
        int indiceOrigem = scanner.nextInt();

        System.out.println("Destino: ");
        int indiceDestino = scanner.nextInt();

        Cidade origem = grafo.getCidades().get(indiceOrigem);
        Cidade destino = grafo.getCidades().get(indiceDestino);

        List<Cidade> rota = aStar.buscarRota(grafo, origem, destino);


        if (rota.isEmpty()){
            System.out.println("Rota não encontrada!");
            return;
        }

        double custo = aStar.calcularCusto(rota, grafo);

        System.out.println("Melhor rota: ");

        for (Cidade cidade: rota){
            System.out.println(cidade.getNome());
            if (!cidade.equals(rota.get(rota.size() - 1))){
                System.out.println("->");
            }
        }
    
      System.out.println();
      System.out.println("Custo total: R$ " + custo);

    }

    public static void executarAlgoritmoGenetico(Grafo grafo) {
        KruskalController kruskalController = new KruskalController();
        ArrayList<Aresta> agm = new ArrayList<>(kruskalController.kruskal(grafo));

        FerroviaController ferroviaController = new FerroviaController();
        ArrayList<Ferrovia> ferrovias = ferroviaController.criarFerrovias(agm);

        double custoTotal = ferroviaController.calcularCustoTotal(ferrovias);

        double orcamento = custoTotal * 0.6;

        System.out.println("\n===== ALGORITMO GENÉTICO =====");
        System.out.println("Custo total (Kruskal): R$ " + custoTotal);
        System.out.println("Orçamento disponível (60%): R$ " + orcamento);

        AlgoritimoGenericoController ag = new AlgoritimoGenericoController();

        List<Aresta> possiveis = grafo.getArestas();

        List<Ferrovia> resultado = ag.otimizarFerrovias(possiveis, orcamento);

        java.text.NumberFormat nf =
            java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("pt", "BR"));


        System.out.println("\n===== RESULTADO =====");
        
        double custoFinal = 0;

        for (Ferrovia f : resultado) {
            Aresta a = f.getAresta();

            System.out.println(
              f.getAresta().getOrigem().getNome() + " <-> " +
              a.getDestino().getNome() + " | " +
              a.getDistancia() + " km"
            );

            custoFinal += a.getDistancia() * 2000000;
        }

        System.out.println("Quantidade de ferrovias: " + resultado.size());
        System.out.println("Custo total da solução: " + nf.format(custoFinal));
    }

}

    

