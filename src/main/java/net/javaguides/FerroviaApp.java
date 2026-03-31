package net.javaguides;
import java.util.ArrayList;
import net.javaguides.springboot.controller.FerroviaController;
import net.javaguides.springboot.controller.GrafoController;
import net.javaguides.springboot.controller.KruskalController;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Ferrovia;
import net.javaguides.springboot.model.Grafo;
import net.javaguides.springboot.controller.AlgoritimoGenericoController;



public class FerroviaApp {
    public static void main(String[] args) {
        GrafoController grafoController = new GrafoController();
        Grafo grafo = grafoController.criarGrafo();
        KruskalController kruskalController = new KruskalController();
        ArrayList<Aresta> arvoreGeradoraMinima = new ArrayList<>(kruskalController.kruskal(grafo));
        FerroviaController ferroviaController = new FerroviaController();
        ArrayList<Ferrovia> ferrovias = ferroviaController.criarFerrovias(arvoreGeradoraMinima);
        for(int i = 0; i < ferrovias.size(); i++){
            System.out.println(ferrovias.get(i).getAresta().toString());
        }
       System.out.println("Custo total da construção das ferrovias: R$" + ferroviaController.calcularCustoTotal(ferrovias));

       //Teste Genetico
        //ArrayList<Aresta> arestas = new ArrayList<>(grafo.getArestas());
        //AlgoritimoGenericoController ag = new AlgoritimoGenericoController();
       // ag.otimizarFerrovias(arestas, 1000000);

    }

}