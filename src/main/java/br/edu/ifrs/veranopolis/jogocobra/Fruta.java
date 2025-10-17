/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifrs.veranopolis.jogocobra;


//Representa a fruta no jogo

public final class Fruta {

    // As coordenadas da fruta no grid (coluna x, linha y).
    private final int x, y;


//construtor da Fruta.
     
    public Fruta(int x, int y) {
        this.x = x; //x A coordenada da coluna (horizontal).
        this.y = y;//y A coordenada da linha (vertical).
    }


//voltaa a cordenada x da fruta.

    public int getX() {
        return x;
    }

   //volta a cordaneda y da fruta
    public int getY() {
        return y;
    }
}