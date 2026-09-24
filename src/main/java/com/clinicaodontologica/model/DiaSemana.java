package com.clinicaodontologica.model;

import java.time.DayOfWeek;

/**
 * Weekday for an {@link Odontologo} availability slot.
 */
public enum DiaSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO;

    /** Maps a date's {@link DayOfWeek} to this domain enum. */
    public static DiaSemana from(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> LUNES;
            case TUESDAY -> MARTES;
            case WEDNESDAY -> MIERCOLES;
            case THURSDAY -> JUEVES;
            case FRIDAY -> VIERNES;
            case SATURDAY -> SABADO;
            case SUNDAY -> DOMINGO;
        };
    }
}
