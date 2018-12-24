package com.example.joan.client_simple_as;

public class Notes {
    private String titol_nota, text_nota;

    public Notes(String titol_nota, String text_nota){
        this.titol_nota = titol_nota;
        this.text_nota = text_nota;
    }

    public String getTitol_nota() {
        return titol_nota;
    }

    public String getText_nota() {
        return text_nota;
    }
}
