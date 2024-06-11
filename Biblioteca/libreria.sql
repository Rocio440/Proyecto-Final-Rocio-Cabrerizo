-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 10-06-2024 a las 22:18:14
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `libreria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dispositivo`
--

CREATE TABLE `dispositivo` (
  `ID` int(11) NOT NULL,
  `DEVICEID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `dispositivo`
--

INSERT INTO `dispositivo` (`ID`, `DEVICEID`) VALUES
(1, '1'),
(2, '12345678z'),
(3, '1'),
(4, '2'),
(5, '1'),
(6, '1'),
(7, '1'),
(8, '1'),
(9, '1'),
(10, '2'),
(11, '1'),
(12, '1'),
(13, '3'),
(14, '12345678Z'),
(15, '12345678Z'),
(16, '12345678Z'),
(17, '12345678Z'),
(18, '12345678Z'),
(19, '12345678Z'),
(20, '12345678Z'),
(21, '12345678Z'),
(22, '12345678Z'),
(23, '12345678Z'),
(24, '2'),
(25, '2'),
(26, '3'),
(27, '2'),
(28, '2'),
(29, '3'),
(30, '12345678Z'),
(31, '12345678Z'),
(32, '2'),
(33, '3'),
(34, '12345678Z'),
(35, '1'),
(36, '1'),
(37, '12345678Z'),
(38, '1'),
(39, '1'),
(40, '1'),
(41, '1'),
(42, '1'),
(43, '1'),
(44, '1'),
(45, '1'),
(46, '1'),
(47, '1'),
(48, '1'),
(49, '1'),
(50, '1'),
(51, '1'),
(52, '1'),
(53, '1'),
(54, '1'),
(55, '3'),
(56, '3'),
(57, '2'),
(58, '4'),
(59, '2'),
(60, '1'),
(61, '3'),
(62, '1'),
(63, '3'),
(64, '2'),
(65, '1'),
(66, '3'),
(67, '5'),
(68, '5'),
(69, '2'),
(70, '1'),
(71, '3'),
(72, '2'),
(73, '76441976G'),
(74, '3'),
(75, '2'),
(76, '3'),
(77, '4'),
(78, '3'),
(79, '11111111H'),
(80, '11111111H'),
(81, '11111111H'),
(82, '2'),
(83, '4'),
(84, '3'),
(85, '2'),
(86, '4');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libros`
--

CREATE TABLE `libros` (
  `isbn` bigint(20) NOT NULL,
  `titulo` varchar(50) NOT NULL,
  `fechapublicacion` date NOT NULL,
  `autor` varchar(50) NOT NULL,
  `tipo` varchar(15) DEFAULT NULL,
  `genero` varchar(30) NOT NULL,
  `idioma` varchar(10) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `ejemplares` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `libros`
--

INSERT INTO `libros` (`isbn`, `titulo`, `fechapublicacion`, `autor`, `tipo`, `genero`, `idioma`, `descripcion`, `ejemplares`) VALUES
(1, 'El señor de los anillos', '2024-05-14', 'Marianito', 'Físico', 'Fantasía', 'Inglés', 'Para jóvenes adultos (18-25 años)', 1),
(3, 'El hobbit', '2016-05-14', 'J.r.r.tolkien', 'Audiolibro', 'Fantasía', 'Español', 'Para niños en edad preadolescente (10-12 años)', 3),
(5, 'Matar un ruiseñor', '1960-07-11', 'Harper lee', 'Suspense', 'Suspense', 'Inglés', 'Para jóvenes adultos (18-25 años)', 5),
(6, 'Crónica de una muerte anunciada', '1981-05-01', 'Gabriel garcía márquez', 'Físico', 'Fantasía', 'Español', 'Para adolescentes (13-18 años)', 2),
(8, 'El nombre del viento', '2007-03-27', 'Patrick rothfuss', 'Fantasía', 'Fantasía', 'Alemán', 'Para niños en edad preadolescente (10-12 años)', 0),
(9, 'Los juegos del hambre', '2008-09-14', 'Suzanne collins', 'Ciencia Ficción', 'Ciencia Ficción', 'Español', 'Para niños en edad preadolescente (10-12 años)', 2),
(11, 'El niño con el pijama de rayas', '2006-05-05', 'John boyne', 'Histórica', 'Histórica', 'Inglés', 'Para adolescentes (13-18 años)', 0),
(14, 'El hobbit', '1977-06-14', 'J.r.r.tolkien', 'Físico', 'Fantasía', 'Español', 'Para niños en edad preadolescente (10-12 años)', 4),
(15, 'El Ocho ', '1988-06-27', 'Katherine Neville', 'Electrónico', 'Suspense', 'Español', 'Para adolescentes (13-18 años)', 2),
(123, 'Harry Potter y la piedra filosofal ', '2004-06-14', 'j.k.rowling', 'Fisico', 'Fantasía', 'Español', 'Para niños en edad preadolescente (10-12 años)', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prestamos`
--

CREATE TABLE `prestamos` (
  `dniusuario` varchar(20) DEFAULT NULL,
  `isbn` bigint(20) DEFAULT NULL,
  `titulo` varchar(100) DEFAULT NULL,
  `fechasalida` date DEFAULT NULL,
  `fechadevo` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `prestamos`
--

INSERT INTO `prestamos` (`dniusuario`, `isbn`, `titulo`, `fechasalida`, `fechadevo`) VALUES
('1', 3, 'El hobbit', '2024-05-23', '2024-05-23'),
('1', 5, 'Matar un ruiseñor', '2024-05-23', '2024-06-03'),
('1', 8, 'El nombre del viento', '2024-05-23', '2024-06-03'),
('1', 3, 'El hobbit', '2024-05-21', '2024-05-23'),
('3', 5, 'Matar un ruiseñor', '2024-06-01', '2024-06-03'),
('3', 3, 'El hobbit', '2024-06-03', '2024-06-03'),
('2', 6, 'Crónica de una muerte anunciada', '2024-06-01', '2024-06-03'),
('4', 6, 'Crónica de una muerte anunciada', '2024-06-01', '2024-06-03'),
('2', 9, 'Los juegos del hambre', '2024-06-01', '2024-06-03'),
('1', 11, 'El niño con el pijama de rayas', '2024-06-01', '2024-06-03'),
('3', 5, 'Matar un ruiseñor', '2024-06-01', '2024-06-03'),
('1', 6, 'Crónica de una muerte anunciada', '2024-06-03', NULL),
('3', 5, 'Matar un ruiseñor', '2024-06-01', '2024-06-07'),
('2', 8, 'El nombre del viento', '2024-06-03', '2024-06-03'),
('1', 9, 'Los juegos del hambre', '2024-06-03', '2024-06-03'),
('3', 8, 'El nombre del viento', '2024-06-03', '2024-06-07'),
('5', 11, 'El niño con el pijama de rayas', '2024-06-03', NULL),
('5', 8, 'El nombre del viento', '2024-06-03', '2024-06-07'),
('2', 8, 'El nombre del viento', '2024-06-07', '2024-06-07'),
('1', 3, 'El hobbit', '2024-06-05', '2024-06-07'),
('3', 15, 'El Ocho ', '2024-06-05', NULL),
('2', 6, 'Crónica de una muerte anunciada', '2024-06-07', '2024-06-07'),
('3', 14, 'El hobbit', '2024-06-07', '2024-06-09'),
('2', 8, 'El nombre del viento', '2024-06-07', NULL),
('3', 8, 'El nombre del viento', '2024-06-07', NULL),
('4', 123, 'Harry potter y la piedra filosofal ', '2024-06-07', '2024-06-10'),
('3', 123, 'Harry potter y la piedra filosofal ', '2024-06-07', '2024-06-07'),
('2', 3, 'El hobbit', '2024-06-10', '2024-06-10'),
('4', 1, 'El señor de los anillos', '2024-06-10', '2024-06-10'),
('3', 6, 'Crónica de una muerte anunciada', '2024-06-10', '2024-06-10'),
('2', 6, 'Crónica de una muerte anunciada', '2024-06-10', '2024-06-10'),
('4', 6, 'Crónica de una muerte anunciada', '2024-06-10', '2024-06-10');

--
-- Disparadores `prestamos`
--
DELIMITER $$
CREATE TRIGGER `actualizar_DEVICEID_despues_insert` AFTER INSERT ON `prestamos` FOR EACH ROW BEGIN

    INSERT INTO dispositivo (DEVICEID) VALUES (NEW.dniusuario);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `dni` varchar(10) NOT NULL,
  `nombre` varchar(30) DEFAULT NULL,
  `primerapellido` varchar(30) DEFAULT NULL,
  `segundoapellido` varchar(30) DEFAULT NULL,
  `domicilio` varchar(100) DEFAULT NULL,
  `telefono` int(9) DEFAULT NULL,
  `sancion` tinyint(1) DEFAULT NULL,
  `cuanto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`dni`, `nombre`, `primerapellido`, `segundoapellido`, `domicilio`, `telefono`, `sancion`, `cuanto`) VALUES
('1', 'Ana', 'Lopez', 'Martinéz', 'Calle as', 123232323, NULL, NULL),
('2', 'Lara', 'Casa', 'Casa', 'Placeta silla A', 345545454, NULL, NULL),
('3', 'Sam', 'Sam', 'Dams', 'Calle ordenador', 344434343, NULL, NULL),
('4', 'Juan', 'Pérez', 'García', 'Calle mayor', 123344343, 127, ' '),
('5', 'Maria', 'Gonzales', 'Lopez', 'Avenida', 987787878, NULL, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `dispositivo`
--
ALTER TABLE `dispositivo`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `libros`
--
ALTER TABLE `libros`
  ADD PRIMARY KEY (`isbn`);

--
-- Indices de la tabla `prestamos`
--
ALTER TABLE `prestamos`
  ADD KEY `dniusuario` (`dniusuario`),
  ADD KEY `isbn` (`isbn`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `dispositivo`
--
ALTER TABLE `dispositivo`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
