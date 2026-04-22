package net.javaguides;

import java.util.List;
import java.util.Scanner;

import net.javaguides.springboot.controller.AStarController;
import net.javaguides.springboot.controller.GrafoController;
import net.javaguides.springboot.controller.KruskalController;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Grafo;

public class FerroviaApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        GrafoController grafoController = new GrafoController();
        Grafo grafo = grafoController.criarGrafo();

        System.out.println("===== ESCOLHA O ALGORITMO =====");
        System.out.println("1 - Kruskal");
        System.out.println("2 - A*");
        System.out.print("Opção: ");

        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                executarKruskal(grafo);
                break;
            case 2:
                executarAStar(grafo, scanner);
                break;
            default:
                System.out.println("Opção inválida. Encerrando o programa.");
                break;
        }
    }

    public static void executarKruskal(Grafo grafo) {
        KruskalController kruskalController = new KruskalController();
        List<Aresta> agm = kruskalController.kruskal(grafo);

        int total = 0;
        int contador = 1;

        System.out.println("\n===== KRUSKAL =====");

        for (Aresta aresta : agm) {
            System.out.println(
                    contador + " - " +
                            aresta.getOrigem().getNome() + " -> " +
                            aresta.getDestino().getNome() + " | " +
                            aresta.getDistancia() + " km");

            total += aresta.getDistancia();
            contador++;
        }

        System.out.println("Total de conexões: " + agm.size());
        System.out.println("Distância total: " + total + " km");
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
}