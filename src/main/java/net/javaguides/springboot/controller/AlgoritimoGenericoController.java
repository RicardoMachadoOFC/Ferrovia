package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Ferrovias;

public class AlgoritimoGenericoController {
    
    class Individuo {
    int[] genes;
    double fitness;

}

    public List<Ferrovias> otimizarFerrovias(List<Aresta> possiveis, double orcamento){
    

        List<Ferrovias> melhorSolucao = new ArrayList<>();


       
        // seleção
        // crossover
        // mutação
        // função fitness

        return melhorSolucao;
    }
    //cada individuo será uma solução 
    //Serve para termos a população incial 
    public Individuo criarIndividuo(int tamanho) {
        Individuo ind = new Individuo();
        ind.genes = new int[tamanho];
        
        //Vai preencher o array de genes com valores aleatorios entre 0 ou 1
        for (int i = 0; i < tamanho; i++){
            ind.genes[i] = Math.random() < 0.5 ? 1 : 0; //se for menor que 0,5 vai ser 1 maior 0
        }
        
        return ind;
    }
 
    //população é o conjunto de soluções
    //Serve para criar um conjunto (população) de combinações de ferrovias possiveis 
    public List<Individuo> criarPopulacao(int tamanhoPop, int tamanhoGenes){
        List<Individuo> pop = new ArrayList<>();

        for (int i = 0; i < tamanhoPop; i++){
            pop.add(criarIndividuo(tamanhoGenes));
        }
        return pop;
    }
    
    
}