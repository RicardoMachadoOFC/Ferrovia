package com.project.ferrovias.servico;

import com.project.ferrovias.algoritmo.AlgoritmoKruskal;
import com.project.ferrovias.grafo.GrafoRodoviario;
import com.project.ferrovias.grafo.InicializadorGrafo;
import com.project.ferrovias.model.RedeFerroviaria;
import org.springframework.stereotype.Service;

@Service
public class ServicoMSTFerroviario {

    private final GrafoRodoviario grafo;
    private final AlgoritmoKruskal kruskal = new AlgoritmoKruskal();
    private RedeFerroviaria mstCache;

    public ServicoMSTFerroviario(InicializadorGrafo inicializador) {
        this.grafo = inicializador.getGrafo();
    }

    // Item c
    public RedeFerroviaria calcular() {
        if (mstCache == null) {
            mstCache = kruskal.calcularMST(
                    grafo.getTodasArestas(),
                    grafo.getCidades().stream().map(c -> c.id()).toList()
            );
        }
        return mstCache;
    }
}
