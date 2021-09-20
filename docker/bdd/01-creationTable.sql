-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le :  mer. 28 nov. 2018 à 11:41
-- Version du serveur :  10.1.36-MariaDB
-- Version de PHP :  7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `PronoMaster3`
--

-- --------------------------------------------------------

--
-- Structure de la table `equipe`
--

CREATE TABLE `equipe` (
  `id_equipe` int(11) NOT NULL,
  `nom_equipe` varchar(100) NOT NULL,
  `logo` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `equipe`
--

INSERT INTO `equipe` (`id_equipe`, `nom_equipe`, `logo`) VALUES
(1, 'france', NULL),
(2, 'Allemagne', NULL),
(3, 'Belgique', NULL),
(4, 'Angleterre', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `match`
--

CREATE TABLE `match` (
  `id_match` int(11) NOT NULL,
  `equipe_in` int(11) NOT NULL,
  `equipe_out` int(11) NOT NULL,
  `but_in` int(11) DEFAULT NULL,
  `but_out` int(11) DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `match`
--

INSERT INTO `match` (`id_match`, `equipe_in`, `equipe_out`, `but_in`, `but_out`, `date`) VALUES
(7, 1, 2, NULL, NULL, '2020-02-20'),
(8, 2, 1, NULL, NULL, '2019-12-12'),
(10, 2, 2, NULL, NULL, '0033-07-17'),
(11, 4, 1, NULL, NULL, '0025-08-09'),
(12, 1, 3, NULL, NULL, '2018-11-16'),
(13, 1, 1, NULL, NULL, '2018-11-07'),
(14, 4, 1, NULL, NULL, '2019-01-23'),
(15, 2, 3, NULL, NULL, '2018-11-21'),
(16, 3, 2, NULL, NULL, '2018-11-05'),
(17, 2, 1, NULL, NULL, '2018-11-06'),
(18, 2, 4, NULL, NULL, '2018-11-01'),
(19, 2, 1, NULL, NULL, '2018-11-06'),
(20, 3, 1, 3, 4, '2018-11-14'),
(21, 2, 1, NULL, NULL, '0016-03-23'),
(22, 3, 1, NULL, NULL, '2018-11-21'),
(23, 2, 3, NULL, NULL, '0025-08-09'),
(24, 3, 2, NULL, NULL, '0025-08-09'),
(25, 4, 2, NULL, NULL, '0027-01-18'),
(26, 3, 4, NULL, NULL, '0026-06-09'),
(27, 3, 1, NULL, NULL, '2020-08-20'),
(28, 4, 2, NULL, NULL, '2020-08-21');

-- --------------------------------------------------------

--
-- Structure de la table `parie`
--

CREATE TABLE `parie` (
  `id_parie` int(11) NOT NULL,
  `id_match` int(11) NOT NULL,
  `but_in` int(11) NOT NULL,
  `but_out` int(11) NOT NULL,
  `id_utilisateur` varchar(100) NOT NULL,
  `resultat` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `parie`
--

INSERT INTO `parie` (`id_parie`, `id_match`, `but_in`, `but_out`, `id_utilisateur`, `resultat`) VALUES
(6, 11, 3, 2, 'maxence', NULL),
(10, 20, 3, 4, 'maxence', 1),
(12, 20, 3, 4, 'paul', 1);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `nom_utilisateur` varchar(100) NOT NULL,
  `mot_de_passe` varchar(100) NOT NULL,
  `mail` varchar(100) NOT NULL,
  `role` int(11) NOT NULL DEFAULT '0',
  `score` int(11) NOT NULL,
  `photo_profil` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`nom_utilisateur`, `mot_de_passe`, `mail`, `role`, `score`, `photo_profil`) VALUES
('alex', '$argon2i$v=19$m=65536,t=2,p=1$3hRa6lfCdh9ML/P1lz9ycQ$38I6w1RK3ZtNE14KaAQ2usJ8ROvI5Mj5sHkFzFzPxxk', 'alex', 0, 0, NULL),
('bib', '$argon2i$v=19$m=65536,t=2,p=1$E9dlxsoSOgLcC08k57qbjw$RNdFxB2D9o5tVUUrfKxyItAr6mM9HjJILRXp6CX9ZvU', '', 0, 0, NULL),
('biere', '$argon2i$v=19$m=65536,t=2,p=1$4ihibIUnXDQFTRNTZ3e3QA$B1zeo9myc2K8YkCz1zjuYmwXl57+VeiGP+J1uBeOOxU', 'biere', 0, 3, NULL),
('eeeee', '$argon2i$v=19$m=65536,t=2,p=1$n2h5Vg2n4LlUw7gyNVwihw$bWF7doqY/SuC7BkHWDiwvNulMq/yMkVczJsetkY9Vno', 'ee', 0, 3, NULL),
('maxence', '$argon2i$v=19$m=65536,t=2,p=1$Yl9LHPwV28TvMAsKg85UuA$hl9zsBGAQt6yMzvYEVPv5mvfxcEgt7QKUj4c2Ji7utk', 'maxence', 0, 8, NULL),
('ollllkkh', '$argon2i$v=19$m=65536,t=2,p=1$kC2CP4AMSyDw0VcQI6+vJg$LSoKlKBUSHsFuW7HsfFiJ0anGA8dH9hB3Ml4Z4+sgdE', 'jhgg', 0, 3, NULL),
('paul', '$argon2i$v=19$m=65536,t=2,p=1$QhGpuaM2vFag/QIvU5ouxg$XHv69DBYe4+rwFl+aaasi/vuGyA1lSm0PdtIuKmhXj4', 'paul', 1, 7, NULL),
('pierre', '$argon2i$v=19$m=65536,t=2,p=1$g6zBcXZ36yGoSYN0bY30EA$16lY8Hc5NrJxf5J1b20Svz6E1ZX6wMdxZDXJA3yBwIQ', 'hello', 1, 0, NULL),
('pierrot', '$argon2i$v=19$m=65536,t=2,p=1$nLucqbyT6lGq563sdQ5FcA$NGnLywEcs7+77aQIxhXoIrkl4eZwf1UkOzaJnEGOxw8', 'pierrot', 0, 0, NULL),
('roger', '$argon2i$v=19$m=65536,t=2,p=1$9BUJ8S/JcZ8bB88vbITFlg$HMwVHaAF53uMIK4BuzoA7u2gn1+yf2iloi+4NzdL4rE', 'roger', 0, 3, NULL),
('rrr', '$argon2i$v=19$m=65536,t=2,p=1$D0YN4tB9h1lGbMQ6xsfrxw$Z6OAiIA9uqEVzWJu20Gbrjz8yQdNkZEMAJu2Hd/1I9g', '', 0, 3, NULL),
('t', '$argon2i$v=19$m=65536,t=2,p=1$IeT8jGBfOpWVNyx5B5r93w$V0TOOvS842OZW3OFM+EY1j/eGi/9+87Oq+jXFx9qQkc', '', 0, 0, NULL);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `equipe`
--
ALTER TABLE `equipe`
  ADD PRIMARY KEY (`id_equipe`);

--
-- Index pour la table `match`
--
ALTER TABLE `match`
  ADD PRIMARY KEY (`id_match`),
  ADD KEY `match_equipe_fk` (`equipe_in`),
  ADD KEY `match_equipe_fk_1` (`equipe_out`);

--
-- Index pour la table `parie`
--
ALTER TABLE `parie`
  ADD PRIMARY KEY (`id_parie`),
  ADD KEY `parie_match_fk` (`id_match`),
  ADD KEY `parie_utilisateur_fk` (`id_utilisateur`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`nom_utilisateur`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `equipe`
--
ALTER TABLE `equipe`
  MODIFY `id_equipe` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `match`
--
ALTER TABLE `match`
  MODIFY `id_match` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT pour la table `parie`
--
ALTER TABLE `parie`
  MODIFY `id_parie` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `match`
--
ALTER TABLE `match`
  ADD CONSTRAINT `match_equipe_fk` FOREIGN KEY (`equipe_in`) REFERENCES `equipe` (`id_equipe`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `match_equipe_fk_1` FOREIGN KEY (`equipe_out`) REFERENCES `equipe` (`id_equipe`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `parie`
--
ALTER TABLE `parie`
  ADD CONSTRAINT `foreign key` FOREIGN KEY (`id_match`) REFERENCES `match` (`id_match`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `foreign key2` FOREIGN KEY (`id_utilisateur`) REFERENCES `utilisateur` (`nom_utilisateur`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
