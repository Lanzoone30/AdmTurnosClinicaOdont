package com.clinicaodontologica.service;

/**
 * {@link Mensajes} de prueba que devuelve la propia clave en lugar del texto
 * traducido. Permite a los tests asertar claves estables e independientes del
 * idioma en vez de textos traducidos.
 */
final class MensajesEco extends Mensajes {

    MensajesEco() {
        super(null);
    }

    @Override
    public String get(String clave, Object... args) {
        return clave;
    }
}
