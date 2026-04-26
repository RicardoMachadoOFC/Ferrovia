package net.javaguides.springboot.controller;

import java.util.*;

import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Grafo;

public class AStarController {

    public List<Cidade> buscarRota(Grafo grafo, Cidade origem, Cidade destino) {
        Map<Cidade, Double> gScore = new HashMap<>();
        Map<Cidade, Double> fScore = new HashMap<>();
        Map<Cidade, Cidade> caminho = new HashMap<>();

        PriorityQueue<Cidade> fila = new PriorityQueue<>(
                Comparator.comparingDouble(fScore::get));

        for (Cidade c : grafo.getCidades()) {
            gScore.put(c, Double.MAX_VALUE);
            fScore.put(c, Double.MAX_VALUE);
        }

        gScore.put(origem, 0.0);
        fScore.put(origem, heuristica(origem, destino));

        fila.add(origem);

        while (!fila.isEmpty()) {

            Cidade atual = fila.poll();

            if (atual.equals(destino)) {
                return reconstruirCaminho(caminho, atual);
            }

            for (Cidade vizinho : getVizinhos(grafo, atual)) {

                double custo = getDistancia(grafo, atual, vizinho) * 5.0;
                double tentativeG = gScore.get(atual) + custo;

                if (tentativeG < gScore.getOrDefault(vizinho, Double.MAX_VALUE)) {
                    caminho.put(vizinho, atual);
                    gScore.put(vizinho, tentativeG);
                    fScore.put(vizinho, tentativeG + heuristica(vizinho, destino));

                    fila.remove(vizinho);
                    fila.add(vizinho);

                }
            }

        }

        return new ArrayList<>();
    }

    private List<Cidade> getVizinhos(Grafo grafo, Cidade cidade) {
        List<Cidade> vizinhos = new ArrayList<>();

        grafo.getArestas().forEach(a -> {
            if (a.getOrigem().equals(cidade)) {
                vizinhos.add(a.getDestino());
            } else if (a.getDestino().equals(cidade)) {
                vizinhos.add(a.getOrigem());
            }
        });
        return vizinhos;
    }

    private double getDistancia(Grafo grafo, Cidade a, Cidade b) {
        return grafo.getArestas().stream()
                .filter(aresta -> (aresta.getOrigem().equals(a) && aresta.getDestino().equals(b)) ||
                        (aresta.getOrigem().equals(b) && aresta.getDestino().equals(a)))
                .findFirst()
                .map(aresta -> aresta.getDistancia())
                .orElse(Double.MAX_VALUE);
    }

    private double heuristica(Cidade a, Cidade b) {
        return 0;
    }

    private List<Cidade> reconstruirCaminho(Map<Cidade, Cidade> caminho, Cidade atual) {
        List<Cidade> total = new ArrayList<>();
        total.add(atual);

        while (caminho.containsKey(atual)) {
            atual = caminho.get(atual);
            total.add(0, atual);
        }
        return total;
    }

    public double calcularCusto(List<Cidade> rota, Grafo grafo) {
        double custoTotal = 0;

        for (int i = 0; i < rota.size() - 1; i++) {
            Cidade atual = rota.get(i);
            Cidade prox = rota.get(i + 1);

            double distancia = getDistancia(grafo, atual, prox);
            custoTotal += distancia * 5.0;
        }
        return custoTotal;
    }
}
