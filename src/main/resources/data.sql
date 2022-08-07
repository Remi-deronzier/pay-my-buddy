--
-- Dumping data for table `user`
--

INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `user_name`) VALUES
("remi@gmail.com", "$2y$10$fWmW8.Q6f20Fz1M0WmnLDeBKBdeK4ZcEyr1BkwkzW00dp3aRlLPEW", "remi", "Deronzier", "remax21"),
("lucie@gmail.com", "$2y$10$h0K.LELTJ9PhRQmy7udPDejWZie8pwwg6mJeznBLl38KT86pFWNTS", "lucie", "Deronzier", "louizaine"),
("gaelle@gmail.com", "$2y$10$cOYvpsb9ViSSBUFsVb/vwO8NAUe7C3bLgyOiTvq8UQVPwck3mggLm", "gaelle", "Deronzier", "GaelleDeronzier"),
("pierre-andre@gmail.com", "$2a$12$8rRhQWxE.mgJX5.g5RSbVugxAwyPm3vU6UK4TG.u0s3U2leQQlo0e", "pierre-andre", "Crepon", "PeterAndrewX"),
("elisabeth@gmail.com", "$2a$12$ROUorJmgvfQoqgTEPG15Ge6hQ6e4H/NhX1spFTWqUxGWn8ViCkbrq", "elisabeth", "Crepon", "creponElisabeth"),
("bruno@gmail.com", "$2a$12$0tdW11OTIGA5RQ.XDly3FuqinPo55d2gest7pWxk5/LLpMd93FxR2", "bruno", "Crepon", "brundig"),
("clementine@gmail.com", "$2a$12$.lZ2riCaWocbUy5QSFre6OHGgoDyJKNsIaRlBma7aFQcPv6arLeU2", "clementine", "Crepon", "clem78-87");

INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `user_name`, `description`, `phone_number`) VALUES
("thomas@gmail.com", "$2y$10$tYLAx3bm0t1H2nyqCJMgh.XjMUGOVlD7Iy06j1of2oUnm4bBycap2", "thomas", "Deronzier", "thomsou", "Hello, my name is Thomas. I love running and hiking. I work in the field of renewable energy. I also play the piano and enjoy spending time with my friends. I find this very important!!!", "+33 6 66 66 66 66");

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`name`) VALUES
("ROLE_USER"),
("ROLE_ADMIN");

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 1),
(3, 1),
(3, 2),
(4, 2);

--
-- Dumping data for table `connection`
--

INSERT INTO `connection` (`owner_id`, `connection_id`) VALUES
(2, 3),
(1,2),
(1,3),
(1,5),
(3, 4);

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`time_stamp`, `sender_id`, `receiver_id`, `amount`) VALUES
("1971-01-01 00:00:01", 1, 2, 20),
("1974-01-01 00:00:01", 1, 3, 3),
("1923-01-01 00:00:01", 1, 2, 32),
("1980-01-01 00:00:01", 1, 3, 30),
("1990-01-01 00:00:01", 2, 1, 43.74),
("1990-01-01 00:00:01", 2, 1, 41.74),
("2000-01-01 00:00:01", 3, 4, 12.74),
("1971-01-01 00:00:03", 2, 3, 100);

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`balance`, `user_id`) VALUES
(100, 1),
(30, 2),
(500, 3),
(600, 4);

--
-- Dumping data for table `external_account`
--

INSERT INTO `external_account` (`label`, `user_id`) VALUES
("Societe generale", 1),
("LCL", 1),
("CIC", 1),
("Credit agricole", 2),
("Banque populaire", 2),
("Lydia", 3),
("Revolut", 3);


--
-- Dumping data for table `bank_transfer`
--

INSERT INTO `bank_transfer` (`user_id`, `amount`, `time_stamp`, `external_account_id`) VALUES
(1, 10,  "1970-01-01 00:00:01", 1),
(2, 30,  "1998-01-01 00:00:01", 4),
(3, 500, "2003-01-01 00:00:01", 6);