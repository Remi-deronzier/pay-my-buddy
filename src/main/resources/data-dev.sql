--
-- Dumping data for table `user`
--

INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `user_name`, `enabled`) VALUES
("payMyBuddy@gmail.com", "$2a$12$f85xICrTr0mW4vcBfJ541ejSquWFbYdOyZJSZL6yyZyyC0Kvfc/8S", "Pay My Buddy", "Super User", "PayMyBuddy", 1),
("lucie@gmail.com", "$2y$10$h0K.LELTJ9PhRQmy7udPDejWZie8pwwg6mJeznBLl38KT86pFWNTS", "lucie", "Deronzier", "louizaine", 1),
("gaelle@gmail.com", "$2y$10$cOYvpsb9ViSSBUFsVb/vwO8NAUe7C3bLgyOiTvq8UQVPwck3mggLm", "gaelle", "Deronzier", "GaelleDeronzier", 1),
("pierre-andre@gmail.com", "$2a$12$8rRhQWxE.mgJX5.g5RSbVugxAwyPm3vU6UK4TG.u0s3U2leQQlo0e", "pierre-andre", "Crepon", "PeterAndrewX", 1),
("elisabeth@gmail.com", "$2a$12$ROUorJmgvfQoqgTEPG15Ge6hQ6e4H/NhX1spFTWqUxGWn8ViCkbrq", "elisabeth", "Crepon", "creponElisabeth", 1),
("bruno@gmail.com", "$2a$12$0tdW11OTIGA5RQ.XDly3FuqinPo55d2gest7pWxk5/LLpMd93FxR2", "bruno", "Crepon", "brundig", 1),
("clementine@gmail.com", "$2a$12$.lZ2riCaWocbUy5QSFre6OHGgoDyJKNsIaRlBma7aFQcPv6arLeU2", "clementine", "Crepon", "clem78-87", 1);

INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `user_name`, `description`, `phone_number`, `date_of_birth`, `enabled`) VALUES
("thomas@gmail.com", "$2y$10$tYLAx3bm0t1H2nyqCJMgh.XjMUGOVlD7Iy06j1of2oUnm4bBycap2", "thomas", "Deronzier", "thomsou", "Hello, my name is Thomas. I love running and hiking. I work in the field of renewable energy. I also play the piano and enjoy spending time with my friends. I find this very important!!!", "+33 6 66 66 66 66", "1997-06-12", 1);

INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `user_name`, `description`, `phone_number`, `date_of_birth`, `enabled`) VALUES
("remi@gmail.com", "$2y$10$fWmW8.Q6f20Fz1M0WmnLDeBKBdeK4ZcEyr1BkwkzW00dp3aRlLPEW", "remi", "Deronzier", "remax21", "Hello, my name is Remi. I love running and hiking. I work in the field of renewable energy. I also play the piano and enjoy spending time with my friends. I find this very important!!!", "+33 6 95 94 43 35", "1998-06-12", 1);

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
(2,4),
(2,5),
(9, 3),
(9, 5),
(3, 4);

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`balance`, `user_id`) VALUES
(0, 1),
(100, 2),
(500, 3),
(530, 4),
(1200, 5),
(640, 6),
(156, 7),
(356, 8),
(300, 9);

--
-- Dumping data for table `external_account`
--

INSERT INTO `external_account` (`label`, `user_id`) VALUES
("LCL", 2);

--
-- Dumping data for table `bank_flow`
--

INSERT INTO `bank_flow` (`time_stamp`, `sender_id`, `amount`, `bank_flow_type`, `receiver_id`) VALUES
("2022-08-15 00:00:01", 3, 20, "transaction", 1),
("2022-08-15 04:00:00", 2, 234, "bank_transfer", NULL),
("2022-08-15 05:00:01", 4, 34, "transaction", 5),
("2022-08-15 06:00:00", 2, 132, "transaction", 6);

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`id`, `description`) VALUES
(1, "coucou"),
(3, "test"),
(4, "hello");

--
-- Dumping data for table `bank_transfer`
--

INSERT INTO `bank_transfer` (`id`, `bank_transfer_type`, `external_account_id`) VALUES
(2, "TOP_UP", 1);

--
-- Dumping data for table `commission`
--

INSERT INTO `commission` (`amount`, `date`) VALUES
(20, "1980-03-05"),
(30, "1981-03-05"),
(10, "1981-03-06");
