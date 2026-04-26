package net.javaguides.springboot.controller;


import java.util.*;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Grafo;

public class AStarFerroviaController {
    public List<Cidade> buscarRota(Grafo grafo, Cidade origem, Cidade destino) {
        
        Map<Cidade, Double> gScore = new HashMap<>();
        Map<Cidade, Double> fScore = new HashMap<>();
        Map<Cidade, Cidade> caminho = new HashMap<>();

        PriorityQueue<Cidade> fila = new PriorityQueue<>(
            Comparator.comparingDouble(fScore::get)
        );

        for (Cidade c : grafo.getCidades()) {
           gScore.put(c, Double.MAX_VALUE);
           fScore.put(c, Double.MAX_VALUE);
        }
        
        gScore.put(origem,0.0);
        fScore.put(origem, heuristica(origem, destino));

        fila.add(origem);

        while(!fila.isEmpty()){

            Cidade atual = fila.poll();

            if(atual.equals(destino)){
                return reconstruirCaminho(caminho, atual);
            }

            for(Cidade vizinho: getVizinhos(grafo, atual)) {

                Aresta aresta = getAresta(grafo, atual, vizinho);
                double custo = 0;

                if (aresta.isFerrovia()){
                    custo = aresta.getDistancia() * 1.2;
                }else{
                    custo = aresta.getDistancia() * 5.0;
                }

                Cidade anterior = caminho.get(atual);

                if (anterior != null){
                    Aresta arestaAnterior = getAresta(grafo, anterior, atual);

                    if (arestaAnterior != null && arestaAnterior.isFerrovia() != aresta.isFerrovia()){
                        custo += 1000;
                    }
                }

                double tentativeG = gScore.get(atual) + custo;

                if(tentativeG < gScore.getOrDefault(vizinho, Double.MAX_VALUE)){
                    caminho.put(vizinho, atual);
                    gScore.put(vizinho, tentativeG);
                    fScore.put(vizinho, tentativeG + heuristica(vizinho,destino));

                    fila.remove(vizinho);
                    fila.add(vizinho);
                    
                }
            }
            
        }

        return new ArrayList<>();
    }

    private List<Cidade> getVizinhos(Grafo grafo, Cidade cidade){
        List<Cidade> vizinhos =new ArrayList<>();

        grafo.getArestas().forEach(a-> {
            if(a.getOrigem().equals(cidade)){
                vizinhos.add(a.getDestino());
            }else if(a.getDestino().equals(cidade)){
                vizinhos.add(a.getOrigem());
            }
        });
        return vizinhos;
    }

    private double getDistancia(Grafo grafo, Cidade a, Cidade b) {
        return grafo.getArestas(). stream()
            .filter(aresta ->
                (aresta.getOrigem().equals(a) && aresta.getDestino().equals(b)) ||
                (aresta.getOrigem().equals(b) && aresta.getDestino().equals(a))
            )
            .findFirst()
            .map(aresta -> aresta.getDistancia())
            .orElse(Double.MAX_VALUE);
    }

    private Aresta getAresta(Grafo grafo, Cidade a, Cidade b){
        return grafo.getArestas().stream()
            .filter(aresta -> 
                (aresta.getOrigem().equals(a) && aresta.getDestino().equals(b)) ||
                (aresta.getOrigem().equals(b) && aresta.getDestino().equals(a))
            )
            .findFirst()
            .orElse(null);
    }

    private double heuristica(Cidade a, Cidade b){
        return 0;
    }

    private List<Cidade> reconstruirCaminho(Map<Cidade, Cidade> caminho, Cidade atual){
        List<Cidade> total = new ArrayList<>();
        total.add(atual);

        while(caminho.containsKey(atual)){
            atual = caminho.get(atual);
            total.add(0, atual);
        }
        return total;
    }

    public double calcularCusto(List<Cidade> rota, Grafo grafo){
        double custoTotal = 0;

        for(int i = 0; i< rota.size() -1; i++){
            Cidade atual= rota.get(i);
            Cidade prox= rota.get(i+1);

            Aresta aresta = getAresta(grafo, atual, prox);

            if (aresta.isFerrovia()){
                custoTotal += aresta.getDistancia() * 1.2;
            }else{
                custoTotal += aresta.getDistancia() * 5.0;
            }

           if (i>0){
            Cidade anterior = rota.get(i-1);
            Aresta arestaAnterior = getAresta(grafo, anterior, atual);

            if (arestaAnterior != null && arestaAnterior.isFerrovia() != aresta.isFerrovia()){
                custoTotal += 1000;
            }
           }
        }
        return custoTotal;
    }
}
