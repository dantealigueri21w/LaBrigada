package pe.appmobile.labrigada.ui.art

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import pe.appmobile.labrigada.R

/**
 * Arte real (SVG -> VectorDrawable, secciones 4.0/4.1.5 del maestro) en `res/drawable/`,
 * generado por `documentos-fuente/_scripts-generadores/gen_labrigada_vector.py`. Estas
 * funciones son el único punto de la app que conoce el id de cada objeto/lugar: mapean el id
 * real de `SeedData` a la ilustración reutilizable de su familia (arte/60-...md sección 3),
 * sin necesidad de un dibujo por objeto -- un cable suelto se ve igual en un cuarto que en un
 * pasillo.
 */
@Composable
fun IlustracionObjetoRiesgo(objetoId: String, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = drawableDeObjeto(objetoId)),
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
fun IconoLugar(lugarId: String, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = drawableDeLugar(lugarId)),
        contentDescription = null,
        modifier = modifier,
    )
}

private fun drawableDeObjeto(objetoId: String): Int = when (objetoId) {
    "cable_suelto", "cable_proyector", "cable_extension_enredado", "cable_suelto_final",
    "cable_enrollado_guardado", "cable_extension_bien_guardado", "cable_final_enrollado" -> R.drawable.objeto_cable
    "mueble_cerca_cama" -> R.drawable.objeto_mueble
    "ventana_sin_seguro", "ventana_con_seguro_puesto" -> R.drawable.objeto_ventana
    "vela_encendida" -> R.drawable.objeto_vela
    "fogata", "fogata_final" -> R.drawable.objeto_fogata
    "olla_mango_afuera", "olla_mango_adentro" -> R.drawable.objeto_olla
    "sarten_borde_mesa" -> R.drawable.objeto_sarten
    "liquido_cerca_enchufe", "liquido_inflamable_calor", "detergente_alcance_nino",
    "detergente_guardado_alto" -> R.drawable.objeto_recipiente_liquido
    "silla_en_pasillo", "silla_con_abrigo", "mochila_en_paso", "objeto_en_vereda",
    "salida_bloqueada", "salida_bloqueada_final", "mochila_en_gancho" -> R.drawable.objeto_obstaculo
    "objeto_punzante", "objeto_punzante_final", "vidrio_roto" -> R.drawable.objeto_punzante
    "agua_estancada", "agua_en_su_lugar" -> R.drawable.objeto_agua_charco
    "agua_no_a_mano", "agua_a_mano" -> R.drawable.objeto_agua_balde
    "juego_roto", "juego_en_buen_estado" -> R.drawable.objeto_juego_roto
    "bicicleta_mal_estacionada" -> R.drawable.objeto_bicicleta
    "herramienta_fuera_sitio", "herramienta_en_su_sitio" -> R.drawable.objeto_herramienta
    "carpa", "carpa_final" -> R.drawable.objeto_carpa
    "fuente_calor" -> R.drawable.objeto_fuente_calor
    "extintor_bloqueado", "extintor_accesible", "extintor_final_accesible" -> R.drawable.objeto_extintor
    "trapo_cerca_hornilla" -> R.drawable.objeto_trapo
    "lena_amontonada", "lena_bien_alejada" -> R.drawable.objeto_lena
    "cruzar_sin_mirar", "cruzar_mirando_los_dos_lados" -> R.drawable.objeto_peaton
    "semaforo_ignorado", "semaforo_respetado" -> R.drawable.objeto_semaforo
    else -> R.drawable.objeto_obstaculo
}

private fun drawableDeLugar(lugarId: String): Int = when (lugarId) {
    "mi_cuarto" -> R.drawable.lugar_mi_cuarto
    "la_cocina" -> R.drawable.lugar_la_cocina
    "la_escuela" -> R.drawable.lugar_la_escuela
    "patio_recreo" -> R.drawable.lugar_patio_recreo
    "la_calle" -> R.drawable.lugar_la_calle
    "taller_casa" -> R.drawable.lugar_taller_casa
    "el_campamento" -> R.drawable.lugar_el_campamento
    "simulacro_final" -> R.drawable.lugar_simulacro_final
    else -> R.drawable.lugar_mi_cuarto
}

fun drawableDeInsignia(insigniaId: String): Int = when (insigniaId) {
    "primera_correccion" -> R.drawable.insignia_primera_correccion
    "ojo_completo" -> R.drawable.insignia_ojo_completo
    "cuarto_seguro" -> R.drawable.insignia_cuarto_seguro
    "cocina_sin_sustos" -> R.drawable.insignia_cocina_sin_sustos
    "recreo_tranquilo" -> R.drawable.insignia_recreo_tranquilo
    "simulacro_superado" -> R.drawable.insignia_simulacro_superado
    "brigada_completa" -> R.drawable.insignia_brigada_completa
    "reaccion_rapida" -> R.drawable.insignia_reaccion_rapida
    "todo_en_su_lugar" -> R.drawable.insignia_todo_en_su_lugar
    "bitacora_llena" -> R.drawable.insignia_bitacora_llena
    "racha_de_guardia" -> R.drawable.insignia_racha_de_guardia
    else -> R.drawable.insignia_primera_correccion
}
