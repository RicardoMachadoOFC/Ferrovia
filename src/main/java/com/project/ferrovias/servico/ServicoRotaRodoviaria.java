package com.project.ferrovias.servico;

import com.project.ferrovias.algoritmo.AlgoritmoAEstrela;
import com.project.ferrovias.grafo.GrafoRodoviario;
import com.project.ferrovias.grafo.InicializadorGrafo;
import com.project.ferrovias.model.ResultadoRota;
import org.springframework.stereotype.Service;

@Service
public class ServicoRotaRodoviaria {

    private final GrafoRodoviario grafo;
    private final AlgoritmoAEstrela aEstrela = new AlgoritmoAEstrela();

    public ServicoRotaRodoviaria(InicializadorGrafo inicializador) {
        this.grafo = inicializador.getGrafo();
    }

    // Item b
    public ResultadoRota calcular(String origem, String destino) {
        return aEstrela.calcularRotaRodoviaria(grafo, origem, destino);
    }
}
