package net.javaguides.springboot.controller;
import java.util.ArrayList;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Ferrovia;

public class FerroviaController {
    public ArrayList<Ferrovia> criarFerrovias(ArrayList<Aresta> arestas) {
        ArrayList<Ferrovia> ferrovias = new ArrayList<>();
        for(Aresta aresta : arestas){
            Ferrovia ferrovia = new Ferrovia(aresta);
            ferrovias.add(ferrovia);
        }
        return ferrovias;
    }

    public long calcularCustoTotal(ArrayList<Ferrovia> ferrovias) {
        long somaDistancias = 0;
        for(Ferrovia ferrovia : ferrovias){
            somaDistancias += ferrovia.getAresta().getDistancia();
        }
        return somaDistancias * 2000000;
    }
}
