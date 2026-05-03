package com.project.ferrovias.servico;

import com.project.ferrovias.algoritmo.AlgoritmoAEstrela;
import com.project.ferrovias.grafo.GrafoRodoviario;
import com.project.ferrovias.grafo.InicializadorGrafo;
import com.project.ferrovias.model.Aresta;
import com.project.ferrovias.model.RedeFerroviaria;
import com.project.ferrovias.model.ResultadoRota;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ServicoRotaMista {

    private final GrafoRodoviario grafo;
    private final AlgoritmoAEstrela aEstrela = new AlgoritmoAEstrela();
    private final ServicoMSTFerroviario mstServico;

    public ServicoRotaMista(InicializadorGrafo inicializador, ServicoMSTFerroviario mstServico) {
        this.grafo = inicializador.getGrafo();
        this.mstServico = mstServico;
    }

    // Item d — usa a MST como rede ferroviária
    public ResultadoRota calcularComMST(String origem, String destino) {
        Set<String> ferrovias = redeparaSet(mstServico.calcular());
        return aEstrela.calcularRotaMista(grafo, origem, destino, ferrovias);
    }

    // Item f — usa a rede gerada pelo algoritmo genético
    public ResultadoRota calcularComRedeOtimizada(String origem, String destino, RedeFerroviaria rede) {
        Set<String> ferrovias = redeparaSet(rede);
        return aEstrela.calcularRotaMista(grafo, origem, destino, ferrovias);
    }

    public Set<String> redeparaSet(RedeFerroviaria rede) {
        Set<String> set = new HashSet<>();
        for (Aresta a : rede.ferrovias()) {
            set.add(AlgoritmoAEstrela.chaveFerrovia(a.origem(), a.destino()));
        }
        return set;
    }
}
