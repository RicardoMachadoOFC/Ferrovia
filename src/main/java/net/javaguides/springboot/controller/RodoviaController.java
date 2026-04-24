package net.javaguides.springboot.controller;
import java.util.ArrayList;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Rodovia;

public class RodoviaController {
    public ArrayList<Rodovia> criarRodovias(ArrayList<Aresta> arestas) {
        ArrayList<Rodovia> rodovias = new ArrayList<>();
        for (Aresta aresta : arestas) {
            Rodovia rodovia = new Rodovia(aresta);
            rodovias.add(rodovia);
        }
        return rodovias;
    }
}