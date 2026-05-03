package com.project.ferrovias.grafo;

import com.project.ferrovias.model.Aresta;
import com.project.ferrovias.model.Cidade;

import java.util.*;

public class GrafoRodoviario {

    private final Map<String, Cidade> cidades = new LinkedHashMap<>();
    private final Map<String, List<Aresta>> adjacencia = new HashMap<>();
    private final List<Aresta> todasArestas = new ArrayList<>();

    public void adicionarCidade(Cidade cidade) {
        cidades.put(cidade.id(), cidade);
        adjacencia.putIfAbsent(cidade.id(), new ArrayList<>());
    }

    public void adicionarAresta(String origem, String destino, double distanciaKm) {
        adjacencia.get(origem).add(new Aresta(origem, destino, distanciaKm));
        adjacencia.get(destino).add(new Aresta(destino, origem, distanciaKm));
        todasArestas.add(new Aresta(origem, destino, distanciaKm));
    }

    public List<Aresta> vizinhos(String cidadeId) {
        return adjacencia.getOrDefault(cidadeId, Collections.emptyList());
    }

    public Collection<Cidade> getCidades() {
        return cidades.values();
    }

    public List<Aresta> getTodasArestas() {
        return todasArestas;
    }

    public Cidade getCidade(String id) {
        return cidades.get(id);
    }

    public double heuristica(String de, String para) {
        Cidade c1 = cidades.get(de);
        Cidade c2 = cidades.get(para);
        if (c1 == null || c2 == null) return 0;
        double dlat = c1.lat() - c2.lat();
        double dlon = c1.lon() - c2.lon();
        return Math.sqrt(dlat * dlat + dlon * dlon) * 111.0;
    }
}
