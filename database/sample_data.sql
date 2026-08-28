-- Datos semilla reales de La Brigada, transcritos de app/src/main/java/pe/appmobile/labrigada/data/seed/SeedData.kt.
-- Se insertan en el primer arranque de la app (BrigadaRepository.sembrarSiEsPrimerLanzamiento()).

-- lugar (8)
INSERT INTO `lugar` (`id`, `nombre`, `orden`) VALUES ('mi_cuarto', 'Mi Cuarto', 1);
INSERT INTO `lugar` (`id`, `nombre`, `orden`) VALUES ('la_cocina', 'La Cocina', 2);
INSERT INTO `lugar` (`id`, `nombre`, `orden`) VALUES ('la_escuela', 'La Escuela', 3);
INSERT INTO `lugar` (`id`, `nombre`, `orden`) VALUES ('patio_recreo', 'El Patio de Recreo', 4);
INSERT INTO `lugar` (`id`, `nombre`, `orden`) VALUES ('la_calle', 'La Calle', 5);
INSERT INTO `lugar` (`id`, `nombre`, `orden`) VALUES ('taller_casa', 'El Taller de Casa', 6);
INSERT INTO `lugar` (`id`, `nombre`, `orden`) VALUES ('el_campamento', 'El Campamento', 7);
INSERT INTO `lugar` (`id`, `nombre`, `orden`) VALUES ('simulacro_final', 'El Simulacro Final', 8);

-- objeto_riesgo (36)
-- Mi Cuarto (5)
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('cable_suelto', 'mi_cuarto', 'Cable suelto en el piso', 1, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('mueble_cerca_cama', 'mi_cuarto', 'Mueble pesado cerca de la cama', 2, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('ventana_sin_seguro', 'mi_cuarto', 'Ventana sin seguro', 3, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('silla_con_abrigo', 'mi_cuarto', 'Silla con un abrigo colgado', 4, 'vela_encendida');
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('vela_encendida', 'mi_cuarto', 'Vela encendida sobre el escritorio', 5, NULL);

-- La Cocina (5)
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('olla_mango_afuera', 'la_cocina', 'Olla en la hornilla de adelante con el mango hacia afuera', 1, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('liquido_cerca_enchufe', 'la_cocina', 'Recipiente con líquido junto al tomacorriente', 2, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('sarten_borde_mesa', 'la_cocina', 'Sartén caliente en el borde de la mesa', 3, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('trapo_cerca_hornilla', 'la_cocina', 'Trapo de cocina colgado cerca de la hornilla encendida', 4, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('detergente_alcance_nino', 'la_cocina', 'Detergente al alcance de la mano en la mesa', 5, NULL);

-- La Escuela (5)
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('silla_en_pasillo', 'la_escuela', 'Silla obstruyendo el pasillo', 1, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('mochila_en_paso', 'la_escuela', 'Mochila tirada en medio del paso', 2, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('salida_bloqueada', 'la_escuela', 'Cajas apiladas frente a la puerta de salida', 3, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('cable_proyector', 'la_escuela', 'Cable del proyector cruzando el pasillo', 4, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('extintor_bloqueado', 'la_escuela', 'Extintor bloqueado por cajas', 5, NULL);

-- El Patio de Recreo (4)
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('objeto_punzante', 'patio_recreo', 'Objeto punzante en el suelo del patio', 1, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('agua_estancada', 'patio_recreo', 'Charco de agua estancada', 2, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('juego_roto', 'patio_recreo', 'Columpio con la cadena rota', 3, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('vidrio_roto', 'patio_recreo', 'Vidrio roto cerca del arenero', 4, NULL);

-- La Calle (4)
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('cruzar_sin_mirar', 'la_calle', 'Cruzar la pista sin mirar a los dos lados', 1, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('objeto_en_vereda', 'la_calle', 'Caja abandonada en medio de la vereda', 2, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('bicicleta_mal_estacionada', 'la_calle', 'Bicicleta tirada atravesada en la vereda', 3, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('semaforo_ignorado', 'la_calle', 'Cruzar con el semáforo en rojo', 4, NULL);

-- El Taller de Casa (4)
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('herramienta_fuera_sitio', 'taller_casa', 'Martillo y clavos fuera de su lugar en el suelo', 1, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('liquido_inflamable_calor', 'taller_casa', 'Lata de líquido inflamable junto al calentador', 2, 'fuente_calor');
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('fuente_calor', 'taller_casa', 'Calentador encendido en el taller', 3, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('cable_extension_enredado', 'taller_casa', 'Cable de extensión enredado en el piso', 4, NULL);

-- El Campamento (4)
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('fogata', 'el_campamento', 'Fogata encendida', 1, 'carpa');
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('carpa', 'el_campamento', 'Carpa armada', 2, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('agua_no_a_mano', 'el_campamento', 'Balde de agua guardado lejos, no a mano', 3, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('lena_amontonada', 'el_campamento', 'Leña amontonada muy cerca de la fogata', 4, NULL);

-- El Simulacro Final (5)
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('cable_suelto_final', 'simulacro_final', 'Cable suelto en el punto de reunión', 1, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('salida_bloqueada_final', 'simulacro_final', 'Salida de emergencia bloqueada', 2, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('objeto_punzante_final', 'simulacro_final', 'Objeto punzante en la zona de evacuación', 3, NULL);
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('fogata_final', 'simulacro_final', 'Fogata cerca de la carpa de mando', 4, 'carpa_final');
INSERT INTO `objeto_riesgo` (`id`, `lugarId`, `nombre`, `orden`, `distanciaMinimaDeId`) VALUES ('carpa_final', 'simulacro_final', 'Carpa de mando', 5, NULL);

-- insignia (11)
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('primera_correccion', 'Primera Corrección', 'Corregir la primera escena', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('ojo_completo', 'Ojo Completo', 'Revisar una escena entera antes de tocar el primer objeto', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('cuarto_seguro', 'Cuarto Seguro', 'Completar el lugar Mi Cuarto', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('cocina_sin_sustos', 'Cocina Sin Sustos', 'Completar el lugar La Cocina', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('recreo_tranquilo', 'Recreo Tranquilo', 'Completar el lugar El Patio de Recreo', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('simulacro_superado', 'Simulacro Superado', 'Pasar un simulacro sin ningún objeto mal corregido', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('brigada_completa', 'Brigada Completa', 'Corregir los 8 lugares', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('reaccion_rapida', 'Reacción Rápida', 'Corregir una escena completa en menos del tiempo de referencia', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('todo_en_su_lugar', 'Todo en su Lugar', 'Corregir 5 escenas sin ningún intento fallido', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('bitacora_llena', 'Bitácora Llena', '20 correcciones registradas', NULL);
INSERT INTO `insignia` (`id`, `nombre`, `descripcion`, `fechaObtenida`) VALUES ('racha_de_guardia', 'Racha de Guardia', '5 días seguidos con al menos una corrección nueva', NULL);
