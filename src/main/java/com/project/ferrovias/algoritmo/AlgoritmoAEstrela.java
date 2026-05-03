package com.project.ferrovias.algoritmo;

import com.project.ferrovias.grafo.GrafoRodoviario;
import com.project.ferrovias.model.Aresta;
import com.project.ferrovias.model.ResultadoRota;
import com.project.ferrovias.model.SegmentoRota;

import java.util.*;

public class AlgoritmoAEstrela {

    // Itens b, d, f — custos de transporte por km e por baldeação
    public static final double CUSTO_RODOVIA_KM  = 5.00;
    public static final double CUSTO_FERROVIA_KM = 1.20;
    public static final double CUSTO_BALDEACAO   = 1_000.00;

    // ---------------------------------------------------------------
    // Item b — rota puramente rodoviária (A* sobre o grafo de rodovias)
    // ---------------------------------------------------------------
    public ResultadoRota calcularRotaRodoviaria(GrafoRodoviario grafo, String origem, String destino) {
        Map<String, Double> gScore   = new HashMap<>();
        Map<String, String> anterior = new HashMap<>();
        PriorityQueue<Map.Entry<String, Double>> fila =
                new PriorityQueue<>(Map.Entry.comparingByValue());
        Set<String> visitado = new HashSet<>();

        gScore.put(origem, 0.0);
        fila.add(Map.entry(origem, grafo.heuristica(origem, destino) * CUSTO_RODOVIA_KM));

        while (!fila.isEmpty()) {
            String atual = fila.poll().getKey();
            if (visitado.contains(atual)) continue;
            visitado.add(atual);

            if (atual.equals(destino)) {
                return reconstruirRotaRodoviaria(grafo, anterior, destino, gScore.get(destino));
            }

            for (Aresta aresta : grafo.vizinhos(atual)) {
                String viz   = aresta.destino();
                double novoG = gScore.get(atual) + aresta.distanciaKm() * CUSTO_RODOVIA_KM;
                if (novoG < gScore.getOrDefault(viz, Double.MAX_VALUE)) {
                    gScore.put(viz, novoG);
                    anterior.put(viz, atual);
                    fila.add(Map.entry(viz, novoG + grafo.heuristica(viz, destino) * CUSTO_RODOVIA_KM));
                }
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Itens d e f — rota mista (rodovia + ferrovia)
    //
    // Estado: "cidadeId|R" (rodovia) ou "cidadeId|F" (ferrovia)
    //
    // A origem é inicializada em AMBOS os modos com custo zero:
    // embarcar no trem na cidade de partida não é baldeação.
    // Baldeação (R$ 1.000) só ocorre quando há troca de modo
    // entre duas cidades ao longo do percurso.
    // ---------------------------------------------------------------
    public ResultadoRota calcularRotaMista(GrafoRodoviario grafo, String origem, String destino,
                                           Set<String> ferroviasDisponiveis) {
        Map<String, Double> gScore   = new HashMap<>();
        Map<String, String> anterior = new HashMap<>();
        PriorityQueue<Map.Entry<String, Double>> fila =
                new PriorityQueue<>(Map.Entry.comparingByValue());
        Set<String> visitado = new HashSet<>();

        // Inicia em modo rodoviário e ferroviário sem custo de baldeação:
        // a carga pode ser embarcada diretamente no modal escolhido na origem.
        double h0 = grafo.heuristica(origem, destino) * CUSTO_FERROVIA_KM;
        gScore.put(origem + "|R", 0.0);
        fila.add(Map.entry(origem + "|R", h0));
        gScore.put(origem + "|F", 0.0);
        fila.add(Map.entry(origem + "|F", h0));

        while (!fila.isEmpty()) {
            String atualEstado = fila.poll().getKey();
            if (visitado.contains(atualEstado)) continue;
            visitado.add(atualEstado);

            String[] partes    = atualEstado.split("\\|");
            String atualCidade = partes[0];
            String modo        = partes[1];

            if (atualCidade.equals(destino)) {
                return reconstruirRotaMista(grafo, anterior, atualEstado, gScore.get(atualEstado));
            }

            double gAtual = gScore.get(atualEstado);

            if ("R".equals(modo)) {
                // Avança por rodovia
                for (Aresta a : grafo.vizinhos(atualCidade)) {
                    String prox  = a.destino() + "|R";
                    double novoG = gAtual + a.distanciaKm() * CUSTO_RODOVIA_KM;
                    if (novoG < gScore.getOrDefault(prox, Double.MAX_VALUE)) {
                        gScore.put(prox, novoG);
                        anterior.put(prox, atualEstado);
                        fila.add(Map.entry(prox,
                                novoG + grafo.heuristica(a.destino(), destino) * CUSTO_FERROVIA_KM));
                    }
                }
                // Baldeação: troca para ferrovia na mesma cidade (custo R$ 1.000)
                String transship = atualCidade + "|F";
                double novoG     = gAtual + CUSTO_BALDEACAO;
                if (novoG < gScore.getOrDefault(transship, Double.MAX_VALUE)) {
                    gScore.put(transship, novoG);
                    anterior.put(transship, atualEstado);
                    fila.add(Map.entry(transship,
                            novoG + grafo.heuristica(atualCidade, destino) * CUSTO_FERROVIA_KM));
                }
            } else {
                // Avança por ferrovia (somente trechos da rede disponível)
                for (Aresta a : grafo.vizinhos(atualCidade)) {
                    if (!ferroviasDisponiveis.contains(chaveFerrovia(atualCidade, a.destino()))) continue;
                    String prox  = a.destino() + "|F";
                    double novoG = gAtual + a.distanciaKm() * CUSTO_FERROVIA_KM;
                    if (novoG < gScore.getOrDefault(prox, Double.MAX_VALUE)) {
                        gScore.put(prox, novoG);
                        anterior.put(prox, atualEstado);
                        fila.add(Map.entry(prox,
                                novoG + grafo.heuristica(a.destino(), destino) * CUSTO_FERROVIA_KM));
                    }
                }
                // Baldeação: troca para rodovia na mesma cidade (custo R$ 1.000)
                String transship = atualCidade + "|R";
                double novoG     = gAtual + CUSTO_BALDEACAO;
                if (novoG < gScore.getOrDefault(transship, Double.MAX_VALUE)) {
                    gScore.put(transship, novoG);
                    anterior.put(transship, atualEstado);
                    fila.add(Map.entry(transship,
                            novoG + grafo.heuristica(atualCidade, destino) * CUSTO_FERROVIA_KM));
                }
            }
        }
        // Fallback: sem ferrovias utilizáveis, usa rota puramente rodoviária
        return calcularRotaRodoviaria(grafo, origem, destino);
    }

    // ---------------------------------------------------------------
    // Utilitários
    // ---------------------------------------------------------------
    public static String chaveFerrovia(String a, String b) {
        return a.compareTo(b) <= 0 ? a + "-" + b : b + "-" + a;
    }

    private ResultadoRota reconstruirRotaRodoviaria(GrafoRodoviario grafo,
                                                    Map<String, String> anterior,
                                                    String destino, double custoTotal) {
        List<String> caminho = new ArrayList<>();
        String atual = destino;
        while (atual != null) {
            caminho.add(0, atual);
            atual = anterior.get(atual);
        }

        List<SegmentoRota> segmentos = new ArrayList<>();
        double distancia = 0;
        for (int i = 0; i < caminho.size() - 1; i++) {
            String de   = caminho.get(i);
            String para = caminho.get(i + 1);
            for (Aresta a : grafo.vizinhos(de)) {
                if (a.destino().equals(para)) {
                    double custo = a.distanciaKm() * CUSTO_RODOVIA_KM;
                    segmentos.add(new SegmentoRota(de, para, a.distanciaKm(), custo, "rodoviária"));
                    distancia += a.distanciaKm();
                    break;
                }
            }
        }
        return new ResultadoRota(caminho, distancia, custoTotal, "rodoviária", segmentos);
    }

    // Reconstrói o caminho misto a partir do backtracking de estados "cidade|modo".
    // Baldeação é detectada quando cidade atual == cidade próxima (troca de modo).
    private ResultadoRota reconstruirRotaMista(GrafoRodoviario grafo,
                                               Map<String, String> anterior,
                                               String fim, double custoTotal) {
        List<String> estados = new ArrayList<>();
        String est = fim;
        while (est != null) {
            estados.add(0, est);
            est = anterior.get(est);
        }

        List<String> cidadesCaminho = new ArrayList<>();
        List<SegmentoRota> segmentos = new ArrayList<>();
        String cidadeAnterior = null;

        for (int i = 0; i < estados.size() - 1; i++) {
            String[] aAtual  = estados.get(i).split("\\|");
            String[] aProx   = estados.get(i + 1).split("\\|");
            String cidAtual  = aAtual[0];
            String modoAtual = aAtual[1];
            String cidProx   = aProx[0];

            if (!cidAtual.equals(cidadeAnterior)) {
                cidadesCaminho.add(cidAtual);
                cidadeAnterior = cidAtual;
            }

            if (cidAtual.equals(cidProx)) {
                // Mesma cidade, modo diferente → baldeação
                segmentos.add(new SegmentoRota(cidAtual, cidAtual, 0, CUSTO_BALDEACAO, "baldeação"));
            } else {
                // Cidades diferentes → trecho de transporte
                double dist = 0;
                for (Aresta a : grafo.vizinhos(cidAtual)) {
                    if (a.destino().equals(cidProx)) { dist = a.distanciaKm(); break; }
                }
                double custo   = dist * ("R".equals(modoAtual) ? CUSTO_RODOVIA_KM : CUSTO_FERROVIA_KM);
                String modoSeg = "R".equals(modoAtual) ? "rodoviária" : "ferroviária";
                segmentos.add(new SegmentoRota(cidAtual, cidProx, dist, custo, modoSeg));
            }
        }

        String ultimaCidade = estados.get(estados.size() - 1).split("\\|")[0];
        if (!ultimaCidade.equals(cidadeAnterior)) cidadesCaminho.add(ultimaCidade);

        double distTotal = segmentos.stream().mapToDouble(SegmentoRota::distanciaKm).sum();
        return new ResultadoRota(cidadesCaminho, distTotal, custoTotal, "mista", segmentos);
    }
}
