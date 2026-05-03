package com.project.ferrovias.algoritmo;

import com.project.ferrovias.model.Aresta;
import com.project.ferrovias.model.RedeFerroviaria;

import java.util.*;

public class AlgoritmoKruskal {

    // Item c — custo de construção de ferrovia por km (R$ 2.000.000,00/km)
    public static final double CUSTO_CONSTRUCAO_KM = 2_000_000.00;

    // Item c — MST via Kruskal com Union-Find
    public RedeFerroviaria calcularMST(List<Aresta> arestas, List<String> cidades) {
        List<Aresta> ordenadas = new ArrayList<>(arestas);
        ordenadas.sort(Comparator.comparingDouble(Aresta::distanciaKm));

        Map<String, String> pai = new HashMap<>();
        Map<String, Integer> rank = new HashMap<>();
        for (String c : cidades) {
            pai.put(c, c);
            rank.put(c, 0);
        }

        List<Aresta> mst = new ArrayList<>();
        double distanciaTotal = 0;

        for (Aresta aresta : ordenadas) {
            String raizA = encontrar(pai, aresta.origem());
            String raizB = encontrar(pai, aresta.destino());
            if (!raizA.equals(raizB)) {
                unir(pai, rank, raizA, raizB);
                mst.add(aresta);
                distanciaTotal += aresta.distanciaKm();
                if (mst.size() == cidades.size() - 1) break;
            }
        }

        double custoTotal = distanciaTotal * CUSTO_CONSTRUCAO_KM;
        return new RedeFerroviaria(mst, custoTotal, custoTotal);
    }

    private String encontrar(Map<String, String> pai, String x) {
        if (!pai.get(x).equals(x)) {
            pai.put(x, encontrar(pai, pai.get(x)));
        }
        return pai.get(x);
    }

    private void unir(Map<String, String> pai, Map<String, Integer> rank, String a, String b) {
        if (rank.get(a) < rank.get(b)) {
            pai.put(a, b);
        } else if (rank.get(a) > rank.get(b)) {
            pai.put(b, a);
        } else {
            pai.put(b, a);
            rank.put(a, rank.get(a) + 1);
        }
    }
}
