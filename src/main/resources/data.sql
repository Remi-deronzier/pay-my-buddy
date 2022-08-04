--
-- Dumping data for table `user`
--

INSERT INTO `user` (`email`, `password`) VALUES
("remi", "$2y$10$fWmW8.Q6f20Fz1M0WmnLDeBKBdeK4ZcEyr1BkwkzW00dp3aRlLPEW"),
("thomas", "$2y$10$tYLAx3bm0t1H2nyqCJMgh.XjMUGOVlD7Iy06j1of2oUnm4bBycap2"),
("lucie", "$2y$10$h0K.LELTJ9PhRQmy7udPDejWZie8pwwg6mJeznBLl38KT86pFWNTS"),
("gaelle", "$2y$10$cOYvpsb9ViSSBUFsVb/vwO8NAUe7C3bLgyOiTvq8UQVPwck3mggLm");

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
(3, 4);

--
-- Dumping data for table `bank_transfer`
--

INSERT INTO `bank_transfer` (`user_id`, `amount`, `time_stamp`) VALUES
(1, 10,  "1970-01-01 00:00:01"),
(2, 30,  "1998-01-01 00:00:01"),
(3, 500, "2003-01-01 00:00:01");

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
(10, 1),
(30, 2),
(500, 3),
(600, 4);
