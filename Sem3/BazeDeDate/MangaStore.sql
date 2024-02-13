CREATE DATABASE MangaStore
GO
USE MangaStore;

CREATE TABLE Casierii
	(casierie_id INT PRIMARY KEY IDENTITY
	);

CREATE TABLE Magazin
	(magazin_id INT PRIMARY KEY IDENTITY,
	nume VARCHAR(30)
	);

CREATE TABLE Clienti
	(client_id INT PRIMARY KEY IDENTITY,
	nume VARCHAR(30),
	data_nasterii DATE
	);

CREATE TABLE Angajati
	(angajat_id INT PRIMARY KEY IDENTITY,
	nume VARCHAR(30),
	salariu INT,
	casierie_id INT FOREIGN KEY REFERENCES Casierii(casierie_id) ON UPDATE CASCADE ON DELETE CASCADE,
	magazin_id INT FOREIGN KEY REFERENCES Magazin(magazin_id) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE AngajatiClienti
	(client_id INT FOREIGN KEY REFERENCES Clienti(client_id) ON UPDATE CASCADE ON DELETE CASCADE,
	angajat_id INT FOREIGN KEY REFERENCES Angajati(angajat_id) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT pk_AngajatiClienti PRIMARY KEY (client_id, angajat_id)
	);

CREATE TABLE Carti
	(carte_id INT PRIMARY KEY IDENTITY,
	titlu VARCHAR(200),
	pret INT CHECK(pret > 0),
	categorie_id INT FOREIGN KEY REFERENCES Categorii(categorie_id) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE Autori
	(autor_id INT PRIMARY KEY IDENTITY,
	nume VARCHAR(30),
	 an_debut INT CHECK(an_debut > 1950) 
	);

CREATE TABLE CartiAutori
	(carte_id INT FOREIGN KEY REFERENCES Carti(carte_id) ON UPDATE CASCADE ON DELETE CASCADE,
	autor_id INT FOREIGN KEY REFERENCES Autori(autor_id) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT pk_CartiAutori PRIMARY KEY (carte_id,autor_id)
	);

CREATE TABLE Categorii
	(categorie_id INT PRIMARY KEY IDENTITY,
	categorie VARCHAR(30)
	);

CREATE TABLE Achizitii
	(client_id INT FOREIGN KEY REFERENCES Clienti(client_id) ON UPDATE CASCADE ON DELETE CASCADE,
	carte_id INT FOREIGN KEY REFERENCES Carti(carte_id) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT pk_Achizitii PRIMARY KEY (client_id, carte_id)
	);

ALTER TABLE Casierii
ADD material VARCHAR(30);

DROP TABLE CartiAutori;
DROP TABLE Autori;

ALTER TABLE Achizitii
ADD cantitate INT;




--=====================================================popularea bazei de date
--casierii 1
SET IDENTITY_INSERT Casierii ON;

INSERT INTO Casierii(casierie_id, material)
VALUES				(1, 'otel');

INSERT INTO Casierii(casierie_id, material)
VALUES				(2,'aluminiu');
SET IDENTITY_INSERT Casierii OFF;
INSERT INTO Casierii(material)
VALUES				('fier');



--magazin 2
SET IDENTITY_INSERT Magazin ON;

INSERT INTO Magazin(magazin_id, nume)
VALUES				(1, 'MegaMax');

SET IDENTITY_INSERT Magazin OFF;




--angajati 3
SET IDENTITY_INSERT Angajati ON;

INSERT INTO Angajati(angajat_id, nume, salariu, casierie_id, magazin_id)
VALUES				(1, 'Ion Popescu', 3500, 1, 1);
UPDATE Angajati SET salariu=1990
WHERE nume='Ion Popescu';
INSERT INTO Angajati(angajat_id, nume, salariu, casierie_id, magazin_id)
VALUES				(2, 'Ionela Andonescu', 3210, 2, 1);
INSERT INTO Angajati(angajat_id, nume, salariu, casierie_id, magazin_id)
VALUES				(3, 'Costel Bercea', 2310, 1, 1);
INSERT INTO Angajati(angajat_id, nume, salariu, casierie_id, magazin_id)
VALUES				(4, 'Anca Pop', 3500, 2, 1);
SET IDENTITY_INSERT Angajati OFF;
INSERT INTO Angajati(nume, salariu, casierie_id, magazin_id)
VALUES				( 'Ana Popescu', 4500, 2, 1);




--clienti 4
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('Adelina', '2002-06-13');
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('Ioana', '2012-01-23');
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('Adrian', '2009-05-05');
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('FLorin', '2000-12-03');
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('mihai', '2005-09-15');
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('Silviu', '2001-01-13');
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('Ana', '2001-01-13');
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('Alin', '1997-07-11');
INSERT INTO Clienti(nume, data_nasterii)
VALUES				('Mihaela', '1999-09-10');




--categorii 5
INSERT INTO Categorii(categorie)
VALUES				('horror');
INSERT INTO Categorii(categorie)
VALUES				('drama');
INSERT INTO Categorii(categorie)
VALUES				('romance');
INSERT INTO Categorii(categorie)
VALUES				('action');
INSERT INTO Categorii(categorie)
VALUES				('sport');




--carti 6
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('Horimiya', 40, 3);
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('One Punch Man', 55, 4);
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('Made in Abyss', 35, 1);
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('Monster', 50, 1);
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('Clannad', 40, 1);
UPDATE Carti SET categorie_id=4
WHERE titlu='Clannad';
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('To Your Etenity', 55, 1);
UPDATE Carti SET categorie_id=2
WHERE titlu='To Your Etenity';
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('Haikyuu', 75, 2);
UPDATE Carti SET categorie_id=5
WHERE titlu='Haikyuu';
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('Free', 50, 2);
UPDATE Carti SET categorie_id=5
WHERE titlu='Free';
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('Your Lie in April', 60, 3);
INSERT INTO Carti(titlu, pret, categorie_id)
VALUES				('Your Name', 100, 3);



--autori 7
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Akihito Tsukushi',1995); --made in abyss
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Naoki Urasawa',1975); --monster
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Jun Maeda',1980); --clannad
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Yoshitoki Ooima',2019); --to your eternity
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Haruichi Furudate',2007); --haikyuu
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Kooji Ooji',2015); --free
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Naoshi Arakawa',2011); --your lie in april
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Makoto Shinkai',2015); --your name
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Hiroki Adachi',2014); -- horimiya 1
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Daisuke Hagiwara',2014); -- horimiya 2
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Tomohiro',1986); --one punch 1
INSERT INTO Autori(nume, an_debut)
VALUES			   ('Yusuke Murata',2009); --one punch 2



--autori carti 8
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (2,1);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (3,2);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (4,3);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (5,4);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (6,5);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (7,6);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (8,7);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (9,8);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (10,9);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (10,10);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (11,11);
INSERT INTO CartiAutori(carte_id, autor_id)
VALUES                 (11,12);


--angajatiClienti 9
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(1,1);
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(3,1);
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(5,1);
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(2,3);
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(4,3);
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(6,3);
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(7,4);
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(8,4);
INSERT INTO AngajatiClienti(client_id, angajat_id)
VALUES						(9,4);



--achizitii 10
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (1, 2, 1);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (1, 4, 2);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (1, 6, 5);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (1, 8, 1);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (2, 3, 1);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (3, 2, 2);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (3, 7, 3);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (4, 5, 2);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (7, 6, 2);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (7, 3, 1);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (8, 2, 1);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (8, 6, 1);
INSERT INTO Achizitii(client_id, carte_id, cantitate)
VALUES				  (8, 8, 1);



--================================================interogari
SELECT titlu FROM Carti Where categorie_id =2;

SELECT * FROM Autori;
SELECT * FROM CartiAutori;

SELECT * FROM Casierii;
SELECT * FROM Angajati;
Select * FROM AngajatiClienti;
SELECT * FROM Clienti;
SELECT * FROM Carti;
SELECT * FROM Achizitii;


--1) afiseaza cartile si autorii lor    (mai multe tabele 1/7)
SELECT C.titlu, A.nume AS autor
FROM Carti C 
FULL JOIN CartiAutori CA 
ON C.carte_id = CA.carte_id
INNER JOIN Autori A
ON CA.autor_id = A.autor_id;


--2) afiseaza cate carti s-au cumparat din fiecare exemplu      (group by 1/3)

SELECT C.titlu,
SUM(cantitate) vandute
FROM Carti C 
INNER JOIN Achizitii A
ON C.carte_id = A.carte_id
GROUP BY C.titlu;


--3) afiseaza angajatii care au litera a in nume si lucreaza la casa 1   (where 1/5)
SELECT *
FROM Angajati A
WHERE A.casierie_id = 1
AND A.nume LIKE '%[Aa]%';


--4) afiseaza clientii care au interactionat cu angajatul Costel Bercea si al caror nume contine litera o (where 2/5)
SELECT C.nume, A.nume                                                               --            (mai multe tabele 2/7)
FROM Clienti C																		--			  (m-n 1/2)
FULL JOIN AngajatiClienti AC
ON C.client_id = AC.client_id
INNER JOIN Angajati A
ON AC.angajat_id = A.angajat_id
WHERE C.nume LIKE '%[Oo]%'
AND A.nume = 'Costel Bercea';


--5) afiseaza categoriile din care exista doar doua titluri      (group by 2/3)
SELECT categorie,                                          --     (having 1/2)
COUNT(Ctg.categorie) AS numar_titluri
FROM Categorii Ctg
INNER JOIN Carti C
ON C.categorie_id = Ctg.categorie_id
GROUP BY Ctg.categorie
HAVING COUNT(Ctg.categorie)=2;


--6) afiseaza cartile si autorii care au categoria sport                               (where 3/5)
SELECT DISTINCT C.titlu, A.nume, Ctg.categorie FROM Autori A                  --   (mai multe tabele 3/7)
FULL JOIN CartiAutori CA											 --	     			(m-n 2/2)
ON CA.autor_id = A.autor_id                                            --             (distinct 1/2)
INNER JOIN Carti C
ON C.carte_id = CA.carte_id
INNER JOIN Categorii Ctg
ON C.categorie_id = Ctg.categorie_id
WHERE Ctg.categorie = 'sport';


--7) afiseaza angajatii care au salariul mai mare de 2500					(where 4/5)
SELECT * FROM Angajati                                       --(distinct 2/2)
WHERE salariu > 2500;



--8) afiseaza angajatii care au vandut mai mult de 5 carti	   			 (group by 3/3)
SELECT A.nume AS angajat,                                      --         (having 2/2)
SUM(Achz.cantitate) as vandute                             --         (mai multe tabele 4/7)
FROM Angajati A
FULL JOIN AngajatiClienti AC
ON A.angajat_id = AC.angajat_id
LEFT JOIN Clienti C
ON C.client_id = AC.client_id
LEFT JOIN Achizitii Achz
ON C.client_id = Achz.client_id
GROUP BY A.nume
HAVING SUM(Achz.cantitate) >5;


--9) afiseaza clientii si cartile din care au cumparat un singur exemplar      (mai multe tabele 5/7)
SELECT C.nume, Carti.titlu FROM Clienti C
FULL JOIN Achizitii A
ON C.client_id = A.client_id
LEFT JOIN Carti
ON A.carte_id = Carti.carte_id
WHERE A.cantitate = 1;


--10) afiseaza toate categoriile din care s-au cumparat minim doua carti     (mai multe tabele 6/7)
select distinct categorie from Categorii
full join Carti 
on Carti.categorie_id = Categorii.categorie_id
left join Achizitii
on Carti.carte_id = Achizitii.carte_id
where Achizitii.cantitate > 1;

select casierie_id from Casierii;
--11)afiseaza casieriile care au avut clienti 
select distinct Casierii.casierie_id from Casierii
full join Angajati
on Casierii.casierie_id = Angajati.casierie_id
left join AngajatiClienti AC
on Angajati.angajat_id = AC.angajat_id
right join Clienti C
on C.client_id = AC.client_id;







SELECT * FROM Carti;
SELECT * FROM Categorii;


SELECT * FROM Angajati;
Select * FROM AngajatiClienti;
SELECT * FROM Clienti;






