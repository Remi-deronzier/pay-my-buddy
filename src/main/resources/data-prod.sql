--
-- Dumping data for table `user`
--

INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `user_name`, `enabled`) VALUES
("payMyBuddy@gmail.com", "$2a$12$f85xICrTr0mW4vcBfJ541ejSquWFbYdOyZJSZL6yyZyyC0Kvfc/8S", "Pay My Buddy", "Super User", "PayMyBuddy", 1),
("lucie@gmail.com", "$2y$10$h0K.LELTJ9PhRQmy7udPDejWZie8pwwg6mJeznBLl38KT86pFWNTS", "lucie", "Dupont", "louizaine", 1),
("gaelle@gmail.com", "$2y$10$cOYvpsb9ViSSBUFsVb/vwO8NAUe7C3bLgyOiTvq8UQVPwck3mggLm", "gaelle", "Dupont", "GaelleDupont", 1),
("pierre-andre@gmail.com", "$2a$12$8rRhQWxE.mgJX5.g5RSbVugxAwyPm3vU6UK4TG.u0s3U2leQQlo0e", "pierre-andre", "Durand", "PeterAndrewX", 1),
("elisabeth@gmail.com", "$2a$12$ROUorJmgvfQoqgTEPG15Ge6hQ6e4H/NhX1spFTWqUxGWn8ViCkbrq", "elisabeth", "Durand", "creponElisabeth", 1),
("bruno@gmail.com", "$2a$12$0tdW11OTIGA5RQ.XDly3FuqinPo55d2gest7pWxk5/LLpMd93FxR2", "bruno", "Durand", "brundig", 1),
("clementine@gmail.com", "$2a$12$.lZ2riCaWocbUy5QSFre6OHGgoDyJKNsIaRlBma7aFQcPv6arLeU2", "clementine", "Durand", "clem78-87", 1);

INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `user_name`, `description`, `phone_number`, `date_of_birth`, `enabled`) VALUES
("thomas@gmail.com", "$2y$10$tYLAx3bm0t1H2nyqCJMgh.XjMUGOVlD7Iy06j1of2oUnm4bBycap2", "thomas", "Dupont", "thomsou", "Hello, my name is Thomas. I love running and hiking. I work in the field of renewable energy. I also play the piano and enjoy spending time with my friends. I find this very important!!!", "+33 6 66 66 66 66", "1997-06-12", 1);

INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `user_name`, `description`, `phone_number`, `date_of_birth`, `enabled`) VALUES
("remi@gmail.com", "$2y$10$fWmW8.Q6f20Fz1M0WmnLDeBKBdeK4ZcEyr1BkwkzW00dp3aRlLPEW", "remi", "Dupont", "remax21", "Hello, my name is Remi. I love running and hiking. I work in the field of renewable energy. I also play the piano and enjoy spending time with my friends. I find this very important!!!", "+33 6 77 77 77 77", "1998-06-12", 1);

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
(1, 2),
(2, 2),
(3, 1),
(3, 2),
(4, 2),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1);

--
-- Dumping data for table `connection`
--

INSERT INTO `connection` (`owner_id`, `connection_id`) VALUES
(2, 3),
(2, 4),
(2, 5),
(9, 3),
(9, 5),
(3, 4);

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`balance`, `user_id`) VALUES
(0, 1),
(0, 2),
(0, 3),
(0, 4),
(0, 5),
(0, 6),
(0, 7),
(0, 8),
(0, 9);

--
-- Dumping data for table `external_account`
--

INSERT INTO `external_account` (`label`, `user_id`) VALUES
("LCL", 2);
