package net.javaguides.springboot.controller;

import java.util.*;
import net.javaguides.springboot.model.Cidade;

public class UnionFindController {
    private Map<Cidade, Cidade> representante;

    public UnionFindController(List<Cidade> cidades) {
        representante = new HashMap<>();
        for (Cidade cidade : cidades) {
            representante.put(cidade, cidade);
        }
    }

    public Cidade find(Cidade cidade) {
        if (!representante.get(cidade).equals(cidade)) {
            representante.put(cidade, find(representante.get(cidade)));
        }
        return representante.get(cidade);
    }

    public void union(Cidade cidade1, Cidade cidade2) {
        Cidade representanteA = find(cidade1);
        Cidade representanteB = find(cidade2);
        if (!representanteA.equals(representanteB)) {
            representante.put(representanteA, representanteB);
        }
    }
}