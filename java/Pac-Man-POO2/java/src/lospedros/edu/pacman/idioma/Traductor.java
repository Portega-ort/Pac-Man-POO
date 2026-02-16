package lospedros.edu.pacman.idioma;

public class Traductor {
    private Lenguaje idiomaActual;

    public Traductor() {
        this.idiomaActual = new Espanol(); // Empieza en español por defecto
    }

    public void setIdioma(Lenguaje nuevoIdioma) {
        this.idiomaActual = nuevoIdioma;
    }

    public Lenguaje getIdioma() {
        return idiomaActual;
    }
}