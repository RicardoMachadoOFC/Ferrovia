package net.javaguides;
import java.util.ArrayList;
import net.javaguides.springboot.controller.GrafoController;
import net.javaguides.springboot.controller.KruskalController;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Grafo;


public class FerroviaApp {
    public static void main(String[] args) {
        GrafoController grafoController = new GrafoController();
        Grafo grafo = grafoController.criarGrafo();
        KruskalController kruskalController = new KruskalController();
        ArrayList<Aresta> arvoreGeradoraMinima = new ArrayList<>(kruskalController.kruskal(grafo));
        for(int i = 0; i < arvoreGeradoraMinima.size(); i++){
            System.out.println(arvoreGeradoraMinima.get(i).toString());
        }
    }
}