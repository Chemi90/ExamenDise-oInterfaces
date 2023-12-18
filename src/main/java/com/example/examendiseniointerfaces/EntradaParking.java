package com.example.examendiseniointerfaces;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EntradaParking {
    private String matricula;
    private String modelo;
    private String cliente;
    private String tarifa;
    private LocalDate entrada;
    private LocalDate salida;
    private double coste;

    // Constructor
    public EntradaParking(String matricula, String modelo, String cliente, String tarifa, LocalDate entrada, LocalDate salida, double coste) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.cliente = cliente;
        this.tarifa = tarifa;
        this.entrada = entrada;
        this.salida = salida;
        this.coste = coste;
    }
}