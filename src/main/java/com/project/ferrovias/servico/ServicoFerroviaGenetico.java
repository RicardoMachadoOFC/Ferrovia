package com.project.ferrovias.servico;

import com.project.ferrovias.algoritmo.AlgoritmoGenetico;
import com.project.ferrovias.grafo.GrafoRodoviario;
import com.project.ferrovias.grafo.InicializadorGrafo;
import com.project.ferrovias.model.RedeFerroviaria;
import org.springframework.stereotype.Service;

@Service
public class ServicoFerroviaGenetico {

    private final GrafoRodoviario grafo;
    private final InicializadorGrafo inicializador;
    private final ServicoMSTFerroviario mstServico;
    private final AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico();
    private RedeFerroviaria redeOtimizadaCache;

    public ServicoFerroviaGenetico(InicializadorGrafo inicializador, ServicoMSTFerroviario mstServico) {
        this.grafo = inicializador.getGrafo();
        this.inicializador = inicializador;
        this.mstServico = mstServico;
    }

    // Item e — orçamento = 60% do custo total da MST Kruskal (item c)
    public RedeFerroviaria calcular() {
        if (redeOtimizadaCache == null) {
            RedeFerroviaria mst = mstServico.calcular();
            double orcamentoMax = mst.custoTotalReais() * 0.60;
            redeOtimizadaCache = algoritmoGenetico.otimizar(
                    grafo,
                    grafo.getTodasArestas(),
                    inicializador.getRotasCarga(),
                    orcamentoMax
            );
        }
        return redeOtimizadaCache;
    }
}
