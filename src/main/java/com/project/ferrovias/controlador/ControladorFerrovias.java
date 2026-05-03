package com.project.ferrovias.controlador;

import com.project.ferrovias.grafo.GrafoRodoviario;
import com.project.ferrovias.grafo.InicializadorGrafo;
import com.project.ferrovias.model.Aresta;
import com.project.ferrovias.model.Cidade;
import com.project.ferrovias.model.RedeFerroviaria;
import com.project.ferrovias.model.ResultadoRota;
import com.project.ferrovias.servico.ServicoFerroviaGenetico;
import com.project.ferrovias.servico.ServicoMSTFerroviario;
import com.project.ferrovias.servico.ServicoRotaMista;
import com.project.ferrovias.servico.ServicoRotaRodoviaria;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
public class ControladorFerrovias {

    private final GrafoRodoviario grafo;
    private final ServicoRotaRodoviaria rotaRodoviaria;
    private final ServicoMSTFerroviario mstFerroviario;
    private final ServicoRotaMista rotaMista;
    private final ServicoFerroviaGenetico ferroviaGenetico;

    public ControladorFerrovias(InicializadorGrafo inicializador,
                                 ServicoRotaRodoviaria rotaRodoviaria,
                                 ServicoMSTFerroviario mstFerroviario,
                                 ServicoRotaMista rotaMista,
                                 ServicoFerroviaGenetico ferroviaGenetico) {
        this.grafo = inicializador.getGrafo();
        this.rotaRodoviaria = rotaRodoviaria;
        this.mstFerroviario = mstFerroviario;
        this.rotaMista = rotaMista;
        this.ferroviaGenetico = ferroviaGenetico;
    }

    @GetMapping("/cidades")
    public Collection<Cidade> listarCidades() {
        return grafo.getCidades();
    }

    @GetMapping("/rodovias")
    public List<Aresta> listarRodovias() {
        return grafo.getTodasArestas();
    }

    // Item b — A* rodoviário
    @PostMapping("/rota/rodoviaria")
    public ResultadoRota rotaRodoviaria(@RequestBody RequisicaoRota req) {
        return rotaRodoviaria.calcular(req.origem(), req.destino());
    }

    // Item c — Kruskal MST
    @GetMapping("/ferrovia/mst")
    public RedeFerroviaria mstFerroviario() {
        return mstFerroviario.calcular();
    }

    // Item d — A* misto usando MST como rede ferroviária
    @PostMapping("/rota/mista")
    public ResultadoRota rotaMista(@RequestBody RequisicaoRota req) {
        return rotaMista.calcularComMST(req.origem(), req.destino());
    }

    // Item e — Algoritmo Genético
    @GetMapping("/ferrovia/genetico")
    public RedeFerroviaria ferroviaGenetico() {
        return ferroviaGenetico.calcular();
    }

    // Item f — A* misto usando rede otimizada pelo AG
    @PostMapping("/rota/otimizada")
    public ResultadoRota rotaOtimizada(@RequestBody RequisicaoRota req) {
        RedeFerroviaria rede = ferroviaGenetico.calcular();
        return rotaMista.calcularComRedeOtimizada(req.origem(), req.destino(), rede);
    }

    public record RequisicaoRota(String origem, String destino) {}
}
