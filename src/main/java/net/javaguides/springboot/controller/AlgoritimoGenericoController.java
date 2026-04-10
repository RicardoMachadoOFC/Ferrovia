package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Ferrovia;
import java.util.Set;
import java.util.HashSet;

public class AlgoritimoGenericoController {

    class Individuo {
        int[] genes;
        double fitness;
    }

    public List<Ferrovia> otimizarFerrovias(List<Aresta> possiveis, double orcamento) {

        List<Individuo> populacao = criarPopulacao(10, possiveis.size());

         //cerebro do algoritimo
        int geracoes = 50;

        for (int g = 0; g < geracoes; g++) {

        for (Individuo ind : populacao) {

            ind.fitness = calcularFitness(ind, possiveis, orcamento);

            System.out.println("Fitness: " + ind.fitness);
            System.out.println("Genes: " + java.util.Arrays.toString(ind.genes));

            List<Aresta> selecionadas = obterArestasSelecionadas(ind, possiveis);

            System.out.println("Selecionadas: " + selecionadas.size());
        }

        // Seleção
        List<Individuo> melhores = selecionarMelhores(populacao);

        // Nova população crossover + mutação
        List<Individuo> novaPopulacao = new ArrayList<>();

        while (novaPopulacao.size() < populacao.size()) {

            Individuo pai1 = melhores.get((int) (Math.random() * melhores.size()));
            Individuo pai2 = melhores.get((int) (Math.random() * melhores.size()));

            Individuo filho = crossover(pai1, pai2);

            mutar(filho);

            novaPopulacao.add(filho);

        }
    

        populacao = novaPopulacao;

         if (g % 10 == 0) {
        System.out.println("Geração " + g + " concluída");
}

        }

        ordenarPopulacao(populacao);
        Individuo melhor = populacao.get(0);

        List<Aresta> selecionadas = obterArestasSelecionadas(melhor, possiveis);
        FerroviaController ferroviaController = new FerroviaController();
        return ferroviaController.criarFerrovias(new ArrayList<>(selecionadas));
        
    }

    // cada individuo será uma solução
    // Serve para termos a população incial
    public Individuo criarIndividuo(int tamanho) {
        Individuo ind = new Individuo();
        ind.genes = new int[tamanho];

        // Vai preencher o array de genes com valores aleatorios entre 0 ou 1
        for (int i = 0; i < tamanho; i++) {
            ind.genes[i] = Math.random() < 0.03 ? 1 : 0;
        }

        return ind;
    }

    public List<Individuo> selecionarMelhores(List<Individuo> populacao) {
        ordenarPopulacao(populacao);

        // pega metade melhor
        return new ArrayList<>(populacao.subList(0, Math.max(1, populacao.size() / 2)));
    }

    public Individuo crossover(Individuo pai1, Individuo pai2){
        Individuo filho = new Individuo();
        filho.genes = new int [pai1.genes.length];

        int pontoCorte = (int) (Math.random() * pai1.genes.length);

        for (int i = 0; i < pai1.genes.length; i++) {
            if (i < pontoCorte) {
                filho.genes[i] = pai1.genes[i];
            }else{
                filho.genes[i] = pai2.genes[i];
            }
        }

        return filho;
        
    }

    public void mutar(Individuo ind) {

        double taxaMutacao = 0.05; //5%

        for (int i = 0; i < ind.genes.length; i++){
            if (Math.random() < taxaMutacao){
                ind.genes[i] = (ind.genes[i] == 1) ? 0 : 1;
            }
        }
    }

    // população é o conjunto de soluções
    // Serve para criar um conjunto (população) de combinações de ferrovias
    // possiveis
    public List<Individuo> criarPopulacao(int tamanhoPop, int tamanhoGenes) {
        List<Individuo> pop = new ArrayList<>();

        for (int i = 0; i < tamanhoPop; i++) {
            pop.add(criarIndividuo(tamanhoGenes));
        }

        return pop;
    }

    public void ordenarPopulacao(List<Individuo> populacao) {
        populacao.sort((a, b) -> Double.compare(b.fitness, a.fitness));
    }

    // Pega os individuos e trasforma nas ferrovias que serão contruidas
    public List<Aresta> obterArestasSelecionadas(Individuo ind, List<Aresta> possiveis) {
        List<Aresta> selecionadas = new ArrayList<>();
        Set<String> jaAdicionadas = new HashSet<>();

        for (int i = 0; i < ind.genes.length; i++) {

            if (ind.genes[i] == 1) {

            Aresta a = possiveis.get(i);

            String origem = a.getOrigem().getNome();
            String destino = a.getDestino().getNome();

            String chave = origem.compareTo(destino) < 0
                    ? origem + "-" + destino
                    : destino + "-" + origem;

            if (!jaAdicionadas.contains(chave)){
                jaAdicionadas.add(chave);
                selecionadas.add(a);
            }

            }
        }

        return selecionadas;
    }

    public double calcularFitness(Individuo ind, List<Aresta> possiveis, double orcamento) {

        double custoConstrucao = 0;
        double economia = 0;

        for (int i = 0; i < ind.genes.length; i++) {

            Aresta a = possiveis.get(i);
            double distancia = a.getDistancia();

            if (ind.genes[i] == 1) {
                // ferrovia contruida

                // custo de contrução
                custoConstrucao += distancia * 2000000;

                // economia no transporte
                double custoRodovia = distancia * 5.0;
                double custoFerrovia = distancia * 1.2;

                economia += (custoRodovia - custoFerrovia);

            }
        }

        // respeita orçamento
        if (custoConstrucao > orcamento) {
            return Double.NEGATIVE_INFINITY;
        }

        // fitness final
        return economia - custoConstrucao;

    }

}