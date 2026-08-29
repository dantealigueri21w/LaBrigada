-- Esquema real de la base de datos local (Room/SQLite) de La Brigada.
-- Exportado desde app/schemas/pe.appmobile.labrigada.data.AppDatabase/1.json (version 1).

CREATE TABLE IF NOT EXISTS `perfil` (`id` INTEGER NOT NULL, `alias` TEXT NOT NULL, `avatarId` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `lugar` (`id` TEXT NOT NULL, `nombre` TEXT NOT NULL, `orden` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `objeto_riesgo` (`id` TEXT NOT NULL, `lugarId` TEXT NOT NULL, `nombre` TEXT NOT NULL, `orden` INTEGER NOT NULL, `distanciaMinimaDeId` TEXT, `esRiesgo` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`lugarId`) REFERENCES `lugar`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE );
CREATE INDEX IF NOT EXISTS `index_objeto_riesgo_lugarId` ON `objeto_riesgo` (`lugarId`);

CREATE TABLE IF NOT EXISTS `correccion_registrada` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lugarId` TEXT NOT NULL, `fecha` INTEGER NOT NULL, `escenaQuedoSegura` INTEGER NOT NULL, FOREIGN KEY(`lugarId`) REFERENCES `lugar`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE );
CREATE INDEX IF NOT EXISTS `index_correccion_registrada_lugarId` ON `correccion_registrada` (`lugarId`);

CREATE TABLE IF NOT EXISTS `simulacro_resultado` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lugarId` TEXT NOT NULL, `fecha` INTEGER NOT NULL, `paso` INTEGER NOT NULL, `objetosQueFallaronCsv` TEXT NOT NULL, FOREIGN KEY(`lugarId`) REFERENCES `lugar`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE );
CREATE INDEX IF NOT EXISTS `index_simulacro_resultado_lugarId` ON `simulacro_resultado` (`lugarId`);

CREATE TABLE IF NOT EXISTS `insignia` (`id` TEXT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `fechaObtenida` INTEGER, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `racha` (`id` INTEGER NOT NULL, `diasConsecutivos` INTEGER NOT NULL, `ultimaFechaActividad` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `repaso_pendiente` (`itemId` TEXT NOT NULL, `fechaUltimoFallo` INTEGER NOT NULL, `intervaloDias` INTEGER NOT NULL, `proximaRevision` INTEGER NOT NULL, PRIMARY KEY(`itemId`));
