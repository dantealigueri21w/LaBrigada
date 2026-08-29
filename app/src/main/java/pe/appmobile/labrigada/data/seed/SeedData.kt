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

    // esRiesgo = false son los distractores de la sección 5.12 del maestro: objetos que YA
    // están bien, con la misma familia visual que un riesgo real de su lugar (la olla con el
    // mango hacia ADENTRO junto a la que lo tiene hacia afuera, la ventana con el seguro puesto
    // junto a la que no lo tiene) para que el niño tenga que distinguir de verdad, no barrer
    // la pantalla entera. Sin ellos, "corregir todo" gana siempre.
    val objetos = listOf(
        // Mi Cuarto (5 riesgo + 2 distractores) — silla_con_abrigo/vela_encendida reproduce la escena de los primeros 30 segundos de la ficha
        ObjetoRiesgoEntity("cable_suelto", "mi_cuarto", "Cable suelto en el piso", 1, null, esRiesgo = true),
        ObjetoRiesgoEntity("mueble_cerca_cama", "mi_cuarto", "Mueble pesado cerca de la cama", 2, null, esRiesgo = true),
        ObjetoRiesgoEntity("ventana_sin_seguro", "mi_cuarto", "Ventana sin seguro", 3, null, esRiesgo = true),
        ObjetoRiesgoEntity("silla_con_abrigo", "mi_cuarto", "Silla con un abrigo colgado", 4, "vela_encendida", esRiesgo = true),
        ObjetoRiesgoEntity("vela_encendida", "mi_cuarto", "Vela encendida sobre el escritorio", 5, null, esRiesgo = true),
        ObjetoRiesgoEntity("cable_enrollado_guardado", "mi_cuarto", "Cable enrollado y guardado", 6, null, esRiesgo = false),
        ObjetoRiesgoEntity("ventana_con_seguro_puesto", "mi_cuarto", "Ventana con el seguro puesto", 7, null, esRiesgo = false),

        // La Cocina (5 riesgo + 2 distractores)
        ObjetoRiesgoEntity("olla_mango_afuera", "la_cocina", "Olla en la hornilla de adelante con el mango hacia afuera", 1, null, esRiesgo = true),
        ObjetoRiesgoEntity("liquido_cerca_enchufe", "la_cocina", "Recipiente con líquido junto al tomacorriente", 2, null, esRiesgo = true),
        ObjetoRiesgoEntity("sarten_borde_mesa", "la_cocina", "Sartén caliente en el borde de la mesa", 3, null, esRiesgo = true),
        ObjetoRiesgoEntity("trapo_cerca_hornilla", "la_cocina", "Trapo de cocina colgado cerca de la hornilla encendida", 4, null, esRiesgo = true),
        ObjetoRiesgoEntity("detergente_alcance_nino", "la_cocina", "Detergente al alcance de la mano en la mesa", 5, null, esRiesgo = true),
        ObjetoRiesgoEntity("olla_mango_adentro", "la_cocina", "Olla con el mango hacia adentro", 6, null, esRiesgo = false),
        ObjetoRiesgoEntity("detergente_guardado_alto", "la_cocina", "Detergente guardado en la repisa alta", 7, null, esRiesgo = false),

        // La Escuela (5 riesgo + 2 distractores)
        ObjetoRiesgoEntity("silla_en_pasillo", "la_escuela", "Silla obstruyendo el pasillo", 1, null, esRiesgo = true),
        ObjetoRiesgoEntity("mochila_en_paso", "la_escuela", "Mochila tirada en medio del paso", 2, null, esRiesgo = true),
        ObjetoRiesgoEntity("salida_bloqueada", "la_escuela", "Cajas apiladas frente a la puerta de salida", 3, null, esRiesgo = true),
        ObjetoRiesgoEntity("cable_proyector", "la_escuela", "Cable del proyector cruzando el pasillo", 4, null, esRiesgo = true),
        ObjetoRiesgoEntity("extintor_bloqueado", "la_escuela", "Extintor bloqueado por cajas", 5, null, esRiesgo = true),
        ObjetoRiesgoEntity("extintor_accesible", "la_escuela", "Extintor libre de cajas, listo para usarse", 6, null, esRiesgo = false),
        ObjetoRiesgoEntity("mochila_en_gancho", "la_escuela", "Mochila colgada en su gancho", 7, null, esRiesgo = false),

        // El Patio de Recreo (4 riesgo + 2 distractores)
        ObjetoRiesgoEntity("objeto_punzante", "patio_recreo", "Objeto punzante en el suelo del patio", 1, null, esRiesgo = true),
        ObjetoRiesgoEntity("agua_estancada", "patio_recreo", "Charco de agua estancada", 2, null, esRiesgo = true),
        ObjetoRiesgoEntity("juego_roto", "patio_recreo", "Columpio con la cadena rota", 3, null, esRiesgo = true),
        ObjetoRiesgoEntity("vidrio_roto", "patio_recreo", "Vidrio roto cerca del arenero", 4, null, esRiesgo = true),
        ObjetoRiesgoEntity("juego_en_buen_estado", "patio_recreo", "Columpio con la cadena en buen estado", 5, null, esRiesgo = false),
        ObjetoRiesgoEntity("agua_en_su_lugar", "patio_recreo", "Bebedero sin charcos alrededor", 6, null, esRiesgo = false),

        // La Calle (4 riesgo + 2 distractores)
        ObjetoRiesgoEntity("cruzar_sin_mirar", "la_calle", "Cruzar la pista sin mirar a los dos lados", 1, null, esRiesgo = true),
        ObjetoRiesgoEntity("objeto_en_vereda", "la_calle", "Caja abandonada en medio de la vereda", 2, null, esRiesgo = true),
        ObjetoRiesgoEntity("bicicleta_mal_estacionada", "la_calle", "Bicicleta tirada atravesada en la vereda", 3, null, esRiesgo = true),
        ObjetoRiesgoEntity("semaforo_ignorado", "la_calle", "Cruzar con el semáforo en rojo", 4, null, esRiesgo = true),
        ObjetoRiesgoEntity("cruzar_mirando_los_dos_lados", "la_calle", "Cruzar mirando a los dos lados antes de pasar", 5, null, esRiesgo = false),
        ObjetoRiesgoEntity("semaforo_respetado", "la_calle", "Cruzar con el semáforo en verde", 6, null, esRiesgo = false),

        // El Taller de Casa (4 riesgo + 2 distractores)
        ObjetoRiesgoEntity("herramienta_fuera_sitio", "taller_casa", "Martillo y clavos fuera de su lugar en el suelo", 1, null, esRiesgo = true),
        ObjetoRiesgoEntity("liquido_inflamable_calor", "taller_casa", "Lata de líquido inflamable junto al calentador", 2, "fuente_calor", esRiesgo = true),
        ObjetoRiesgoEntity("fuente_calor", "taller_casa", "Calentador encendido en el taller", 3, null, esRiesgo = true),
        ObjetoRiesgoEntity("cable_extension_enredado", "taller_casa", "Cable de extensión enredado en el piso", 4, null, esRiesgo = true),
        ObjetoRiesgoEntity("herramienta_en_su_sitio", "taller_casa", "Martillo y clavos guardados en su caja", 5, null, esRiesgo = false),
        ObjetoRiesgoEntity("cable_extension_bien_guardado", "taller_casa", "Cable de extensión enrollado, sin estorbar", 6, null, esRiesgo = false),

        // El Campamento (4 riesgo + 2 distractores) — fogata/carpa: mismos ids que MotorEscenaTest y MotorSimulacroTest de la Parte 1
        ObjetoRiesgoEntity("fogata", "el_campamento", "Fogata encendida", 1, "carpa", esRiesgo = true),
        ObjetoRiesgoEntity("carpa", "el_campamento", "Carpa armada", 2, null, esRiesgo = true),
        ObjetoRiesgoEntity("agua_no_a_mano", "el_campamento", "Balde de agua guardado lejos, no a mano", 3, null, esRiesgo = true),
        ObjetoRiesgoEntity("lena_amontonada", "el_campamento", "Leña amontonada muy cerca de la fogata", 4, null, esRiesgo = true),
        ObjetoRiesgoEntity("agua_a_mano", "el_campamento", "Balde de agua bien cerca, a mano", 5, null, esRiesgo = false),
        ObjetoRiesgoEntity("lena_bien_alejada", "el_campamento", "Leña guardada lejos de la fogata", 6, null, esRiesgo = false),

        // El Simulacro Final (5 riesgo + 2 distractores) — combina riesgos de varios lugares anteriores
        ObjetoRiesgoEntity("cable_suelto_final", "simulacro_final", "Cable suelto en el punto de reunión", 1, null, esRiesgo = true),
        ObjetoRiesgoEntity("salida_bloqueada_final", "simulacro_final", "Salida de emergencia bloqueada", 2, null, esRiesgo = true),
        ObjetoRiesgoEntity("objeto_punzante_final", "simulacro_final", "Objeto punzante en la zona de evacuación", 3, null, esRiesgo = true),
        ObjetoRiesgoEntity("fogata_final", "simulacro_final", "Fogata cerca de la carpa de mando", 4, "carpa_final", esRiesgo = true),
        ObjetoRiesgoEntity("carpa_final", "simulacro_final", "Carpa de mando", 5, null, esRiesgo = true),
        ObjetoRiesgoEntity("cable_final_enrollado", "simulacro_final", "Cable bien enrollado en el punto de reunión", 6, null, esRiesgo = false),
        ObjetoRiesgoEntity("extintor_final_accesible", "simulacro_final", "Extintor accesible en la zona de evacuación", 7, null, esRiesgo = false),
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
