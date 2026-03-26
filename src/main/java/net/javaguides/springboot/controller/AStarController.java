package net.javaguides.springboot.controller;


import java.util.*;

import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Grafo;

public class AStarController {

    public List<Cidade> buscarRota(Grafo grafo, Cidade origem, Cidade destino) {

        PriorityQueue<Cidade> fila = new PriorityQueue<>();
        Map<Cidade, Cidade> caminho = new HashMap<>();

        fila.add(origem);

        while(!fila.isEmpty()){

            Cidade atual = fila.poll();

            if(atual.equals(destino)){
                break;
            }

            
        }

        return new ArrayList<>();
    }
}
