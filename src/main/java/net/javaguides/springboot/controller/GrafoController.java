package net.javaguides.springboot.controller;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Grafo;

public class GrafoController {

    Cidade brasilia = new Cidade("Brasília");
    Cidade salvador = new Cidade("Salvador");   
    Cidade fortaleza = new Cidade("Fortaleza");
    Cidade saoPaulo = new Cidade("São Paulo");
    Cidade rioDeJaneiro = new Cidade("Rio de Janeiro");
    Cidade beloHorizonte = new Cidade("Belo Horizonte");
    Cidade curitiba = new Cidade("Curitiba");
    Cidade portoAlegre = new Cidade("Porto Alegre");
    Cidade goiania = new Cidade("Goiânia");
    Cidade cuiaba = new Cidade("Cuiabá");
    Cidade campoGrande = new Cidade("Campo Grande");
    Cidade palmas = new Cidade("Palmas");
    Cidade aracaju = new Cidade("Aracaju");
    Cidade maceio = new Cidade("Maceió");
    Cidade recife = new Cidade("Recife");
    Cidade teresina = new Cidade("Teresina");
    Cidade saoLuis = new Cidade("São Luís");
    Cidade belem = new Cidade("Belém");
    Cidade macapa = new Cidade("Macapá");
    Cidade boaVista = new Cidade("Boa Vista");
    Cidade rioBranco = new Cidade("Rio Branco");
    Cidade natal = new Cidade("Natal");
    Cidade joaoPessoa = new Cidade("João Pessoa");
    Cidade florianopolis = new Cidade("Florianópolis");
    Cidade vitoria = new Cidade("Vitória");
    Cidade portoVelho = new Cidade("Porto Velho");
    Cidade manaus = new Cidade("Manaus");

    Cidade[] capitais = {
        brasilia, salvador, fortaleza, saoPaulo, rioDeJaneiro,
        beloHorizonte, curitiba, portoAlegre, goiania, cuiaba,
        campoGrande, palmas, aracaju, maceio, recife, teresina,
        saoLuis, belem, macapa, boaVista, rioBranco, natal,
        joaoPessoa, florianopolis, vitoria, portoVelho, manaus
    };

    Aresta[] conexoes = {
            new Aresta(portoAlegre, florianopolis, 463),
            new Aresta(florianopolis, curitiba, 306),
            new Aresta(curitiba, saoPaulo, 417),
            new Aresta(curitiba, campoGrande, 974),
            new Aresta(saoPaulo, rioDeJaneiro, 445),
            new Aresta(saoPaulo, beloHorizonte, 586),
            new Aresta(saoPaulo, campoGrande, 985),
            new Aresta(campoGrande, goiania, 829),
            new Aresta(campoGrande, cuiaba, 704),
            new Aresta(rioDeJaneiro, beloHorizonte, 441),
            new Aresta(rioDeJaneiro, vitoria, 517),
            new Aresta(beloHorizonte, vitoria, 518),
            new Aresta(beloHorizonte, campoGrande, 1260),
            new Aresta(beloHorizonte, salvador, 1429),
            new Aresta(beloHorizonte, goiania, 862),
            new Aresta(beloHorizonte, brasilia, 734),
            new Aresta(goiania, salvador, 1646),
            new Aresta(goiania, palmas, 825),
            new Aresta(goiania, cuiaba, 887),
            new Aresta(goiania, brasilia, 208),
            new Aresta(cuiaba, palmas, 1487),
            new Aresta(cuiaba, belem, 2629),
            new Aresta(cuiaba, manaus, 2348),
            new Aresta(cuiaba, portoVelho, 1461),
            new Aresta(vitoria, salvador, 1053),
            new Aresta(salvador, aracaju, 340),
            new Aresta(salvador, recife, 825),
            new Aresta(salvador, maceio, 597),
            new Aresta(salvador, teresina, 1152),
            new Aresta(salvador, palmas, 1438),
            new Aresta(palmas, saoLuis, 1250),
            new Aresta(palmas, belem, 1200),
            new Aresta(belem, manaus, 3047),
            new Aresta(belem, saoLuis, 576),
            new Aresta(belem, macapa, 529),
            new Aresta(belem, boaVista, 2806),
            new Aresta(manaus, boaVista, 747),
            new Aresta(manaus, portoVelho, 889),
            new Aresta(manaus, rioBranco, 1397),
            new Aresta(portoVelho, rioBranco, 509),
            new Aresta(aracaju, maceio, 274),
            new Aresta(maceio, recife, 287),
            new Aresta(recife, joaoPessoa, 116),
            new Aresta(recife, fortaleza, 777),
            new Aresta(recife, teresina, 1127),
            new Aresta(joaoPessoa, teresina, 1173),
            new Aresta(joaoPessoa, natal, 178),
            new Aresta(natal, fortaleza, 518),
            new Aresta(teresina, fortaleza, 605),
            new Aresta(teresina, saoLuis, 436)
    };

    public Grafo criarGrafo() {
        Grafo grafo = new Grafo();
        for (Cidade capital : capitais) {
            grafo.adicionarCidade(capital);
        }
        for (Aresta conexao : conexoes) {
            grafo.adicionarAresta(conexao);
        }
        return grafo;
    }     
}