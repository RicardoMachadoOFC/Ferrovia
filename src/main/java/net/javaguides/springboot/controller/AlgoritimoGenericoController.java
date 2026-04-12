package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Ferrovia;
import net.javaguides.springboot.model.Grafo;
import net.javaguides.springboot.model.RotaCarga;

public class AlgoritimoGenericoController {

    //Representa uma solução
    class Individuo {
        int[] genes;
        double fitness;
    }

    public List<Ferrovia> otimizarFerrovias(List<Aresta> possiveis, double orcamento) {
        GrafoController grafoController = new GrafoController();
        Grafo grafo = grafoController.criarGrafo();

        AStarController aStar = new AStarController();

        List<RotaCarga> rotas = criarRotas(grafoController);

        List<Individuo> populacao = criarPopulacao(10, possiveis.size());
        int geracoes = 50;

        //Loop evolutivo
        for (int g = 0; g < geracoes; g++) {

            //1. Calcula fitness (Avaliação)
            for (Individuo ind : populacao) {
                ind.fitness = calcularFitness(ind, possiveis, orcamento, grafo, aStar, rotas);;
        }

            //2. Ordenar (melhores primeiro)
            ordenarPopulacao(populacao);
            Individuo melhor = populacao.get(0);

            System.out.println("Geração " + g + " - Melhor fitness: " + melhor.fitness);

            //3.Seleção
            List<Individuo> melhores = selecionarMelhores(populacao);

            //4. Reprodução, nova população (crossover + mutação)
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

        // Avaliação final (recalcula fitness da última geração)
        for (Individuo ind : populacao) {
             ind.fitness = calcularFitness(ind, possiveis, orcamento, grafo, aStar, rotas);
        }

        ordenarPopulacao(populacao);
        Individuo melhor = populacao.get(0);

        //Converter para ferrovias
        List<Aresta> selecionadas = obterArestasSelecionadas(melhor, possiveis);

        FerroviaController ferroviaController = new FerroviaController();
        return ferroviaController.criarFerrovias(new ArrayList<>(selecionadas));
    }

    //--Criação da população--

    // população é o conjunto de soluções, serve para criar um conjunto (população) de combinações de ferrovias possiveis
    public List<Individuo> criarPopulacao(int tamanhoPop, int tamanhoGenes) {
        List<Individuo> pop = new ArrayList<>();

        for (int i = 0; i < tamanhoPop; i++) {
            pop.add(criarIndividuo(tamanhoGenes));
        }

        return pop;
    }

    // cada individuo será uma solução
    // Serve para termos a população incial
    public Individuo criarIndividuo(int tamanho) {
        Individuo ind = new Individuo();
        ind.genes = new int[tamanho];

        // Vai preencher o array de genes com valores aleatorios entre 0 ou 1
        for (int i = 0; i < tamanho; i++) {
            ind.genes[i] = Math.random() < 0.02 ? 1 : 0;
        }

        return ind;
    }

    //--Seleção--

    public List<Individuo> selecionarMelhores(List<Individuo> populacao) {
        ordenarPopulacao(populacao);
        return new ArrayList<>(populacao.subList(0, Math.max(1, populacao.size() / 2)));
    }

    public void ordenarPopulacao(List<Individuo> populacao) {
        populacao.sort((a, b) -> Double.compare(b.fitness, a.fitness));
    }

    //--Reprodução--

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

    //--Conversão para Arestas--

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

    //--Fitiness--

    public double calcularFitness(Individuo ind, List<Aresta> possiveis, double orcamento, Grafo grafo, AStarController aStar, List<RotaCarga> rotas) {

        double custoConstrucao = 0;

        //custo de contrução
        for (int i = 0; i < ind.genes.length; i++) {
            if (ind.genes[i] == 1) { // ferrovia contruida
                custoConstrucao += possiveis.get(i).getDistancia() * 2000000;
            }
        }

        //penalização de orçamento
        if (custoConstrucao > orcamento) {
            double excesso = custoConstrucao - orcamento;
            return -(excesso * 1000);      
        }

        List<Aresta> ferrovias = obterArestasSelecionadas(ind, possiveis);

        double custoTotalTransporte = 0;

        for (RotaCarga rota : rotas) {

            List<Cidade> caminho = aStar.buscarRota(grafo, rota.getOrigem(), rota.getDestino());

            double custo = aStar.calcularCusto(caminho, grafo); 
            
            //simulação provisoria, até o A* estar adaptado
            if (!ferrovias.isEmpty()) {
                custo *= (1 - (ferrovias.size() * 0.01));
            }

            custoTotalTransporte += custo * rota.getQuantidade();
        }

        //retorna negativo
        return -(custoTotalTransporte + custoConstrucao);    
  
    }


    //--Anexo 1--
    public List<RotaCarga> criarRotas(GrafoController grafoController){
        List<RotaCarga> rotas = new ArrayList<>();

        rotas.add(new RotaCarga(grafoController.saoPaulo, grafoController.rioDeJaneiro, 150));
        rotas.add(new RotaCarga(grafoController.brasilia, grafoController.goiania, 140));
        rotas.add(new RotaCarga(grafoController.rioDeJaneiro, grafoController.beloHorizonte, 130));
        rotas.add(new RotaCarga(grafoController.saoPaulo, grafoController.recife, 120));
        rotas.add(new RotaCarga(grafoController.manaus, grafoController.saoPaulo, 110));


    /*  rotas.add(new RotaCarga(grafoController.fortaleza, grafoController.saoPaulo, 100));
        rotas.add(new RotaCarga(grafoController.portoAlegre, grafoController.brasilia, 90));
        rotas.add(new RotaCarga(grafoController.saoPaulo, grafoController.salvador, 90));
        rotas.add(new RotaCarga(grafoController.rioDeJaneiro, grafoController.salvador, 85));
        rotas.add(new RotaCarga(grafoController.belem, grafoController.manaus, 80));
        rotas.add(new RotaCarga(grafoController.beloHorizonte, grafoController.brasilia, 80 ));            
        rotas.add(new RotaCarga(grafoController.belem, grafoController.rioDeJaneiro, 75)); 
        rotas.add(new RotaCarga(grafoController.florianopolis, grafoController.portoAlegre, 75 )); 
        rotas.add(new RotaCarga(grafoController.recife, grafoController.salvador, 75 )); 
        rotas.add(new RotaCarga(grafoController.curitiba, grafoController.natal, 70 )); 
        rotas.add(new RotaCarga(grafoController.curitiba, grafoController.florianopolis, 70 )); 
        rotas.add(new RotaCarga(grafoController.portoAlegre, grafoController.rioDeJaneiro, 70 )); 
        rotas.add(new RotaCarga(grafoController.saoLuis, grafoController.belem, 65 )); 
        rotas.add(new RotaCarga(grafoController.saoPaulo, grafoController.florianopolis, 65 )); 
        rotas.add(new RotaCarga(grafoController.salvador, grafoController.brasilia, 65 )); 
        rotas.add(new RotaCarga(grafoController.beloHorizonte, grafoController.saoLuis, 60 )); 
        rotas.add(new RotaCarga(grafoController.natal, grafoController.fortaleza, 60 )); 
        rotas.add(new RotaCarga(grafoController.cuiaba, grafoController.goiania, 60 )); 
        rotas.add(new RotaCarga(grafoController.cuiaba, grafoController.vitoria, 55 )); 
        rotas.add(new RotaCarga(grafoController.fortaleza, grafoController.teresina, 55 )); 
        rotas.add(new RotaCarga(grafoController.vitoria, grafoController.rioDeJaneiro, 55 )); 
        rotas.add(new RotaCarga(grafoController.campoGrande, grafoController.curitiba, 55 )); 
        rotas.add(new RotaCarga(grafoController.brasilia, grafoController.cuiaba, 55 )); 
        rotas.add(new RotaCarga(grafoController.campoGrande, grafoController.recife, 50 )); 
        rotas.add(new RotaCarga(grafoController.recife, grafoController.joaoPessoa, 50 )); 
        rotas.add(new RotaCarga(grafoController.teresina, grafoController.saoLuis, 50 )); 
        rotas.add(new RotaCarga(grafoController.manaus, grafoController.portoVelho, 50 )); 
        rotas.add(new RotaCarga(grafoController.rioDeJaneiro, grafoController.vitoria, 50 )); 
        rotas.add(new RotaCarga(grafoController.maceio, grafoController.recife, 45 ));
        rotas.add(new RotaCarga(grafoController.joaoPessoa, grafoController.natal, 45 )); 
        rotas.add(new RotaCarga(grafoController.portoVelho, grafoController.cuiaba, 45 )); 
        rotas.add(new RotaCarga(grafoController.teresina, grafoController.brasilia, 45 )); 
        rotas.add(new RotaCarga(grafoController.salvador, grafoController.aracaju, 40 )); 
        rotas.add(new RotaCarga(grafoController.manaus, grafoController.boaVista, 40 )); 
        rotas.add(new RotaCarga(grafoController.palmas, grafoController.brasilia, 40 )); 
        rotas.add(new RotaCarga(grafoController.joaoPessoa, grafoController.salvador, 40 )); 
        rotas.add(new RotaCarga(grafoController.aracaju, grafoController.maceio, 35 )); 
        rotas.add(new RotaCarga(grafoController.portoVelho, grafoController.rioBranco, 35 ));
        rotas.add(new RotaCarga(grafoController.palmas, grafoController.belem, 35 )); 
        rotas.add(new RotaCarga(grafoController.maceio, grafoController.beloHorizonte, 35 )); 
        rotas.add(new RotaCarga(grafoController.belem, grafoController.macapa, 30 )); 
        rotas.add(new RotaCarga(grafoController.macapa, grafoController.fortaleza, 30 )); 
        rotas.add(new RotaCarga(grafoController.aracaju, grafoController.rioDeJaneiro, 30 )); 
        rotas.add(new RotaCarga(grafoController.rioBranco, grafoController.saoPaulo, 25 )); 
        rotas.add(new RotaCarga(grafoController.boaVista, grafoController.brasilia, 20 )); 
        /* */

        return rotas;
    }

}