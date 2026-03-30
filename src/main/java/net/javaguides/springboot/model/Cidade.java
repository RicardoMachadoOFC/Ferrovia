package net.javaguides.springboot.model;

public class Cidade {

    private String nome;

    public Cidade(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    // Implementação de equals e hashCode para comparação de objetos Cidade
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cidade)) {
            return false;
        }
        Cidade cidade = (Cidade) o;
        return nome.equalsIgnoreCase(cidade.getNome());
    }

    @Override
    public int hashCode() {
        return nome.toLowerCase().hashCode();
    }

}
