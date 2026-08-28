package pe.appmobile.labrigada.data.seed

import pe.appmobile.labrigada.data.entity.InsigniaEntity
import pe.appmobile.labrigada.data.entity.LugarEntity
import pe.appmobile.labrigada.data.entity.ObjetoRiesgoEntity

object SeedData {

    val lugares = listOf(
        LugarEntity("mi_cuarto", "Mi Cuarto", 1),
        LugarEntity("la_cocina", "La Cocina", 2),
        LugarEntity("la_escuela", "La Escuela", 3),
        LugarEntity("patio_recreo", "El Patio de Recreo", 4),
        LugarEntity("la_calle", "La Calle", 5),
        LugarEntity("taller_casa", "El Taller de Casa", 6),
        LugarEntity("el_campamento", "El Campamento", 7),
        LugarEntity("simulacro_final", "El Simulacro Final", 8),
    )

    val objetos = listOf(
        // Mi Cuarto (5) — silla_con_abrigo/vela_encendida reproduce la escena de los primeros 30 segundos de la ficha
        ObjetoRiesgoEntity("cable_suelto", "mi_cuarto", "Cable suelto en el piso", 1, null),
        ObjetoRiesgoEntity("mueble_cerca_cama", "mi_cuarto", "Mueble pesado cerca de la cama", 2, null),
        ObjetoRiesgoEntity("ventana_sin_seguro", "mi_cuarto", "Ventana sin seguro", 3, null),
        ObjetoRiesgoEntity("silla_con_abrigo", "mi_cuarto", "Silla con un abrigo colgado", 4, "vela_encendida"),
        ObjetoRiesgoEntity("vela_encendida", "mi_cuarto", "Vela encendida sobre el escritorio", 5, null),

        // La Cocina (5)
        ObjetoRiesgoEntity("olla_mango_afuera", "la_cocina", "Olla en la hornilla de adelante con el mango hacia afuera", 1, null),
        ObjetoRiesgoEntity("liquido_cerca_enchufe", "la_cocina", "Recipiente con líquido junto al tomacorriente", 2, null),
        ObjetoRiesgoEntity("sarten_borde_mesa", "la_cocina", "Sartén caliente en el borde de la mesa", 3, null),
        ObjetoRiesgoEntity("trapo_cerca_hornilla", "la_cocina", "Trapo de cocina colgado cerca de la hornilla encendida", 4, null),
        ObjetoRiesgoEntity("detergente_alcance_nino", "la_cocina", "Detergente al alcance de la mano en la mesa", 5, null),

        // La Escuela (5)
        ObjetoRiesgoEntity("silla_en_pasillo", "la_escuela", "Silla obstruyendo el pasillo", 1, null),
        ObjetoRiesgoEntity("mochila_en_paso", "la_escuela", "Mochila tirada en medio del paso", 2, null),
        ObjetoRiesgoEntity("salida_bloqueada", "la_escuela", "Cajas apiladas frente a la puerta de salida", 3, null),
        ObjetoRiesgoEntity("cable_proyector", "la_escuela", "Cable del proyector cruzando el pasillo", 4, null),
        ObjetoRiesgoEntity("extintor_bloqueado", "la_escuela", "Extintor bloqueado por cajas", 5, null),

        // El Patio de Recreo (4)
        ObjetoRiesgoEntity("objeto_punzante", "patio_recreo", "Objeto punzante en el suelo del patio", 1, null),
        ObjetoRiesgoEntity("agua_estancada", "patio_recreo", "Charco de agua estancada", 2, null),
        ObjetoRiesgoEntity("juego_roto", "patio_recreo", "Columpio con la cadena rota", 3, null),
        ObjetoRiesgoEntity("vidrio_roto", "patio_recreo", "Vidrio roto cerca del arenero", 4, null),

        // La Calle (4)
        ObjetoRiesgoEntity("cruzar_sin_mirar", "la_calle", "Cruzar la pista sin mirar a los dos lados", 1, null),
        ObjetoRiesgoEntity("objeto_en_vereda", "la_calle", "Caja abandonada en medio de la vereda", 2, null),
        ObjetoRiesgoEntity("bicicleta_mal_estacionada", "la_calle", "Bicicleta tirada atravesada en la vereda", 3, null),
        ObjetoRiesgoEntity("semaforo_ignorado", "la_calle", "Cruzar con el semáforo en rojo", 4, null),

        // El Taller de Casa (4)
        ObjetoRiesgoEntity("herramienta_fuera_sitio", "taller_casa", "Martillo y clavos fuera de su lugar en el suelo", 1, null),
        ObjetoRiesgoEntity("liquido_inflamable_calor", "taller_casa", "Lata de líquido inflamable junto al calentador", 2, "fuente_calor"),
        ObjetoRiesgoEntity("fuente_calor", "taller_casa", "Calentador encendido en el taller", 3, null),
        ObjetoRiesgoEntity("cable_extension_enredado", "taller_casa", "Cable de extensión enredado en el piso", 4, null),

        // El Campamento (4) — fogata/carpa: mismos ids que MotorEscenaTest y MotorSimulacroTest de la Parte 1
        ObjetoRiesgoEntity("fogata", "el_campamento", "Fogata encendida", 1, "carpa"),
        ObjetoRiesgoEntity("carpa", "el_campamento", "Carpa armada", 2, null),
        ObjetoRiesgoEntity("agua_no_a_mano", "el_campamento", "Balde de agua guardado lejos, no a mano", 3, null),
        ObjetoRiesgoEntity("lena_amontonada", "el_campamento", "Leña amontonada muy cerca de la fogata", 4, null),

        // El Simulacro Final (5) — combina riesgos de varios lugares anteriores
        ObjetoRiesgoEntity("cable_suelto_final", "simulacro_final", "Cable suelto en el punto de reunión", 1, null),
        ObjetoRiesgoEntity("salida_bloqueada_final", "simulacro_final", "Salida de emergencia bloqueada", 2, null),
        ObjetoRiesgoEntity("objeto_punzante_final", "simulacro_final", "Objeto punzante en la zona de evacuación", 3, null),
        ObjetoRiesgoEntity("fogata_final", "simulacro_final", "Fogata cerca de la carpa de mando", 4, "carpa_final"),
        ObjetoRiesgoEntity("carpa_final", "simulacro_final", "Carpa de mando", 5, null),
    )

    val insignias = listOf(
        InsigniaEntity("primera_correccion", "Primera Corrección", "Corregir la primera escena", null),
        InsigniaEntity("ojo_completo", "Ojo Completo", "Revisar una escena entera antes de tocar el primer objeto", null),
        InsigniaEntity("cuarto_seguro", "Cuarto Seguro", "Completar el lugar Mi Cuarto", null),
        InsigniaEntity("cocina_sin_sustos", "Cocina Sin Sustos", "Completar el lugar La Cocina", null),
        InsigniaEntity("recreo_tranquilo", "Recreo Tranquilo", "Completar el lugar El Patio de Recreo", null),
        InsigniaEntity("simulacro_superado", "Simulacro Superado", "Pasar un simulacro sin ningún objeto mal corregido", null),
        InsigniaEntity("brigada_completa", "Brigada Completa", "Corregir los 8 lugares", null),
        InsigniaEntity("reaccion_rapida", "Reacción Rápida", "Corregir una escena completa en menos del tiempo de referencia", null),
        InsigniaEntity("todo_en_su_lugar", "Todo en su Lugar", "Corregir 5 escenas sin ningún intento fallido", null),
        InsigniaEntity("bitacora_llena", "Bitácora Llena", "20 correcciones registradas", null),
        InsigniaEntity("racha_de_guardia", "Racha de Guardia", "5 días seguidos con al menos una corrección nueva", null),
    )
}
