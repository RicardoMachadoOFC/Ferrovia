package net.javaguides;
import net.javaguides.springboot.controller.GrafoController;
import net.javaguides.springboot.controller.KruskalController;
import net.javaguides.springboot.model.Grafo;


public class FerroviaApp {
    public static void main(String[] args) {
        GrafoController grafoController = new GrafoController();
        Grafo grafo = grafoController.criarGrafo();
        KruskalController kruskalController = new KruskalController();
        kruskalController.ordenarArestas(grafo);

    }
}