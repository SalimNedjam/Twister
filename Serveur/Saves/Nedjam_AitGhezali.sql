-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Client :  localhost:3306
-- Généré le :  Mar 26 Février 2019 à 11:33
-- Version du serveur :  5.7.23
-- Version de PHP :  7.0.33-0+deb9u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `Nedjam_AitGhezali`
--

-- --------------------------------------------------------

--
-- Structure de la table `Friends`
--

CREATE TABLE `Friends` (
  `id_user1` int(11) NOT NULL,
  `id_user2` int(11) NOT NULL,
  `date_accept` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `FriendsRequest`
--

CREATE TABLE `FriendsRequest` (
  `id_sender` int(11) NOT NULL,
  `id_receiver` int(11) NOT NULL,
  `date_request` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Session`
--

CREATE TABLE `Session` (
  `id_user` int(11) DEFAULT NULL,
  `key_session` varchar(64) NOT NULL,
  `date_session` timestamp NULL DEFAULT NULL,
  `root` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `UserInfos`
--

CREATE TABLE `UserInfos` (
  `id_user` int(11) NOT NULL,
  `nom` varchar(32) DEFAULT NULL,
  `prenom` varchar(32) DEFAULT NULL,
  `date_naiss` date DEFAULT NULL,
  `sexe` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Users`
--

CREATE TABLE `Users` (
  `id_user` int(11) NOT NULL,
  `Username` varchar(32) DEFAULT NULL,
  `Mail` varchar(32) DEFAULT NULL,
  `Password` blob,
  `date_create` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `Friends`
--
ALTER TABLE `Friends`
  ADD PRIMARY KEY (`id_user2`,`id_user1`),
  ADD KEY `id_user1` (`id_user1`);

--
-- Index pour la table `FriendsRequest`
--
ALTER TABLE `FriendsRequest`
  ADD PRIMARY KEY (`id_sender`,`id_receiver`),
  ADD KEY `id_receiver` (`id_receiver`);

--
-- Index pour la table `Session`
--
ALTER TABLE `Session`
  ADD PRIMARY KEY (`key_session`),
  ADD KEY `id_user_session` (`id_user`);

--
-- Index pour la table `UserInfos`
--
ALTER TABLE `UserInfos`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `id_user` (`id_user`);

--
-- Index pour la table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `Username` (`Username`),
  ADD UNIQUE KEY `Mail` (`Mail`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `Users`
--
ALTER TABLE `Users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `Friends`
--
ALTER TABLE `Friends`
  ADD CONSTRAINT `id_user1` FOREIGN KEY (`id_user1`) REFERENCES `Users` (`id_user`),
  ADD CONSTRAINT `id_user2` FOREIGN KEY (`id_user2`) REFERENCES `Users` (`id_user`);

--
-- Contraintes pour la table `FriendsRequest`
--
ALTER TABLE `FriendsRequest`
  ADD CONSTRAINT `id_receiver` FOREIGN KEY (`id_receiver`) REFERENCES `Users` (`id_user`),
  ADD CONSTRAINT `id_sender` FOREIGN KEY (`id_sender`) REFERENCES `Users` (`id_user`);

--
-- Contraintes pour la table `Session`
--
ALTER TABLE `Session`
  ADD CONSTRAINT `id_user_session` FOREIGN KEY (`id_user`) REFERENCES `Users` (`id_user`);

--
-- Contraintes pour la table `UserInfos`
--
ALTER TABLE `UserInfos`
  ADD CONSTRAINT `id_user_info` FOREIGN KEY (`id_user`) REFERENCES `Users` (`id_user`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
