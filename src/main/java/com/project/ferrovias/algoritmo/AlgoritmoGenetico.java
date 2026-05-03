package com.project.ferrovias.algoritmo;

import com.project.ferrovias.grafo.GrafoRodoviario;
import com.project.ferrovias.grafo.InicializadorGrafo.RotaCarga;
import com.project.ferrovias.model.Aresta;
import com.project.ferrovias.model.RedeFerroviaria;
import com.project.ferrovias.model.ResultadoRota;

import java.util.*;

public class AlgoritmoGenetico {

    // Parâmetros do AG
    private static final int    POPULACAO      = 100;
    private static final int    GERACOES       = 300;
    private static final double TAXA_CROSSOVER = 0.85;
    private static final double TAXA_MUTACAO   = 0.03;
    private static final double PENALIDADE     = 1e15;

    private final AlgoritmoAEstrela aEstrela = new AlgoritmoAEstrela();
    private final Random random = new Random();

    // Item e — determinar quais trechos de ferrovia construir dentro do orçamento
    // disponível (60% do custo Kruskal), minimizando o custo de transporte das cargas
    public RedeFerroviaria otimizar(GrafoRodoviario grafo, List<Aresta> arestas,
                                    List<RotaCarga> rotas, double orcamentoMax) {
        int n = arestas.size();
        boolean[][] populacao = inicializarPopulacao(n, arestas, orcamentoMax);

        boolean[] melhorCromossomo = null;
        double melhorFitness = Double.MAX_VALUE;

        for (int g = 0; g < GERACOES; g++) {
            double[] fitnesses = new double[POPULACAO];
            for (int i = 0; i < POPULACAO; i++) {
                fitnesses[i] = calcularFitness(grafo, arestas, populacao[i], rotas, orcamentoMax);
                if (fitnesses[i] < melhorFitness) {
                    melhorFitness = fitnesses[i];
                    melhorCromossomo = Arrays.copyOf(populacao[i], n);
                }
            }

            boolean[][] novaPopulacao = new boolean[POPULACAO][n];
            // Elitismo: preserva o melhor indivíduo sem alteração
            if (melhorCromossomo != null) {
                novaPopulacao[0] = Arrays.copyOf(melhorCromossomo, n);
            }
            for (int i = 1; i < POPULACAO; i += 2) {
                int pai1 = selecionar(fitnesses);
                int pai2 = selecionar(fitnesses);
                boolean[][] filhos = cruzar(populacao[pai1], populacao[pai2], n);
                novaPopulacao[i] = mutar(filhos[0]);
                if (i + 1 < POPULACAO) novaPopulacao[i + 1] = mutar(filhos[1]);
            }
            populacao = novaPopulacao;
        }

        // Reconstrói a rede ferroviária a partir do melhor cromossomo encontrado
        List<Aresta> ferrOtimizadas = new ArrayList<>();
        double custoTotal = 0;
        if (melhorCromossomo != null) {
            for (int i = 0; i < n; i++) {
                if (melhorCromossomo[i]) {
                    ferrOtimizadas.add(arestas.get(i));
                    custoTotal += arestas.get(i).distanciaKm() * AlgoritmoKruskal.CUSTO_CONSTRUCAO_KM;
                }
            }
        }
        return new RedeFerroviaria(ferrOtimizadas, custoTotal, orcamentoMax);
    }

    // Cria população inicial com diversidade: metade dos indivíduos recebe
    // arestas aleatoriamente (exploração), metade recebe todas as viáveis (explotação)
    private boolean[][] inicializarPopulacao(int n, List<Aresta> arestas, double orcamentoMax) {
        boolean[][] pop = new boolean[POPULACAO][n];
        List<Integer> indices = new ArrayList<>();
        for (int j = 0; j < n; j++) indices.add(j);

        for (int i = 0; i < POPULACAO; i++) {
            Collections.shuffle(indices, random);
            double custoAcum = 0;
            // Primeira metade: inclui aresta com 50% de chance (diversidade)
            // Segunda metade: inclui todas as arestas que cabem no orçamento
            boolean aleatorio = i < POPULACAO / 2;
            for (int idx : indices) {
                double custo = arestas.get(idx).distanciaKm() * AlgoritmoKruskal.CUSTO_CONSTRUCAO_KM;
                if (custoAcum + custo <= orcamentoMax) {
                    boolean incluir = aleatorio ? random.nextBoolean() : true;
                    pop[i][idx] = incluir;
                    if (incluir) custoAcum += custo;
                }
            }
        }
        return pop;
    }

    // Fitness = soma do custo de transporte de todas as cargas das rotas do Anexo I.
    // Cromossomos que excedem o orçamento recebem penalidade alta.
    private double calcularFitness(GrafoRodoviario grafo, List<Aresta> arestas,
                                   boolean[] cromossomo, List<RotaCarga> rotas, double orcamentoMax) {
        double custoConstr = 0;
        Set<String> ferrDisp = new HashSet<>();
        for (int i = 0; i < arestas.size(); i++) {
            if (cromossomo[i]) {
                Aresta a = arestas.get(i);
                custoConstr += a.distanciaKm() * AlgoritmoKruskal.CUSTO_CONSTRUCAO_KM;
                ferrDisp.add(AlgoritmoAEstrela.chaveFerrovia(a.origem(), a.destino()));
            }
        }
        if (custoConstr > orcamentoMax) return PENALIDADE + custoConstr;

        // Soma do custo de transporte das cargas (item e do PDF):
        // para cada rota, calcula o custo da melhor rota (rodovia ou mista) e
        // multiplica pelo número de cargas diárias registradas pelo governo
        double custoLogistico = 0;
        for (RotaCarga rota : rotas) {
            ResultadoRota resultado = aEstrela.calcularRotaMista(
                    grafo, rota.origem(), rota.destino(), ferrDisp);
            if (resultado != null) {
                custoLogistico += resultado.custoReais() * rota.cargasDia();
            }
        }
        return custoLogistico;
    }

    // Seleção por torneio binário — favorece indivíduos com menor custo logístico
    private int selecionar(double[] fitnesses) {
        int a = random.nextInt(POPULACAO);
        int b = random.nextInt(POPULACAO);
        return fitnesses[a] <= fitnesses[b] ? a : b;
    }

    // Cruzamento de ponto único entre dois pais
    private boolean[][] cruzar(boolean[] pai1, boolean[] pai2, int n) {
        boolean[][] filhos = {Arrays.copyOf(pai1, n), Arrays.copyOf(pai2, n)};
        if (random.nextDouble() < TAXA_CROSSOVER) {
            int ponto = 1 + random.nextInt(n - 1); // evita ponto 0 (cópia exata)
            for (int i = ponto; i < n; i++) {
                filhos[0][i] = pai2[i];
                filhos[1][i] = pai1[i];
            }
        }
        return filhos;
    }

    // Mutação por inversão de bit com probabilidade TAXA_MUTACAO por gene
    private boolean[] mutar(boolean[] cromossomo) {
        boolean[] mutado = Arrays.copyOf(cromossomo, cromossomo.length);
        for (int i = 0; i < mutado.length; i++) {
            if (random.nextDouble() < TAXA_MUTACAO) mutado[i] = !mutado[i];
        }
        return mutado;
    }
}
