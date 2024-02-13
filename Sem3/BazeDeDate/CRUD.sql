use MangaStore
go

--------------------validari--------------------
--validare an
CREATE OR ALTER FUNCTION dbo.TestAn(@an INT)
RETURNS BIT
BEGIN
	DECLARE @ret BIT = 0;

	IF @an >1950 AND @an < YEAR(GETDATE()) SET @ret = 1;
	RETURN @ret;
END;
GO
--validare stirng
CREATE FUNCTION dbo.TestString(@str VARCHAR(50))
RETURNS BIT
AS
BEGIN
	DECLARE @ret BIT = 0;
	IF LEN(@str) > 0 set @ret = 1;
	RETURN @ret;
END;
GO

--validare pret
CREATE FUNCTION dbo.TestPret(@p INT)
RETURNS BIT 
AS
BEGIN
	DECLARE @ret BIT =0;
	IF @p > 0 set @ret = 1;
	RETURN @ret;
END;
GO

GO
--validare categorie
CREATE OR ALTER FUNCTION dbo.TestCatId(@c INT)
RETURNS BIT
BEGIN
	DECLARE @ret BIT = 0;
    IF EXISTS(SELECT categorie_id FROM Categorii WHERE categorie_id = @c) SET @ret = 1;
	RETURN @ret;
END;
GO
--validare carte id
CREATE OR ALTER FUNCTION dbo.TestCarteId(@id INT)
RETURNS BIT 
BEGIN
	DECLARE @ret BIT = 0;
	IF EXISTS(SELECT carte_id FROM Carti WHERE carte_id = @id) SET @ret = 1;
	RETURN @ret;
END;
GO
--valiadre autor id
CREATE OR ALTER FUNCTION dbo.TestAutorId(@id INT)
RETURNS BIT 
BEGIN
	DECLARE @ret BIT = 0;
	IF EXISTS(SELECT autor_id FROM Autori WHERE autor_id = @id) SET @ret = 1;
	RETURN @ret;
END;
GO



------------------------------------------------------------------------------proceduri
----------Carti
CREATE OR ALTER PROCEDURE CreateCartiCRUD
	@titlu VARCHAR(30),
	@pret INT,
	@categorie_id INT
AS
BEGIN
	SET NOCOUNT ON; --nu mai arata cate randuri au fost afectate

	IF(dbo.TestCatId(@categorie_id) = 1 AND dbo.TestString(@titlu) = 1 AND 
		dbo.TestPret(@pret) = 1)
	BEGIN
		INSERT INTO Carti(titlu,pret,categorie_id)
		VALUES (@titlu, @pret, @categorie_id);

		PRINT 'Inserarea a fost realizata cu succes'
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Carti'
		RETURN;
	END;
END;
GO


CREATE OR ALTER PROCEDURE ReadCartiCRUD
AS
BEGIN
	SET NOCOUNT ON; 
		SELECT * FROM Carti;
END;
GO


CREATE OR ALTER PROCEDURE UpdateCartiCRUD
	@titlu VARCHAR(100),
	@pret_nou INT
AS
BEGIN
	SET NOCOUNT ON;

	IF(dbo.TestString(@titlu) = 1 AND dbo.TestPret(@pret_nou) = 1)
	BEGIN

		UPDATE Carti SET pret = @pret_nou
		WHERE titlu = @titlu;

		PRINT 'Update realizat cu succes';
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Carti'
		RETURN;
	END;
END;
GO


CREATE OR ALTER PROCEDURE DeleteCartiCRUD
	@titlu VARCHAR(100)
AS
BEGIN
	SET NOCOUNT ON;

	IF(dbo.TestString(@titlu) = 1)
	BEGIN

		DELETE FROM Carti
		WHERE titlu = @titlu;

		PRINT 'Stergerea a fost realizata cu succes';
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Carti'
		RETURN;
	END;
END;
GO

exec ReadCartiCRUD;
exec CreateCartiCRUD 'ion',250,2456;
exec DeleteCartiCRUD 'ion';
exec UpdateCartiCRUD 'asdas',222;
	
	go



---------------------Autori------------------

CREATE OR ALTER PROCEDURE CreateAutoriCRUD
	@nume VARCHAR(50),
	@an INT
AS
BEGIN
	SET NOCOUNT ON;

	IF(dbo.TestAn(@an) = 1 AND dbo.TestString(@nume) = 1)
	BEGIN
		INSERT INTO Autori(nume,an_debut)
		VALUES (@nume, @an)

		PRINT 'Crearea efectuata cu succes'
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Autori';
		RETURN;
	END;
END;
GO


CREATE OR ALTER PROCEDURE ReadAutoriCRUD
AS
BEGIN
	SET NOCOUNT ON;
		SELECT * FROM Autori;
END;
GO


CREATE OR ALTER PROCEDURE UpdateAutoriCRUD
	@nume VARCHAR(50),
	@an_nou INT
AS
BEGIN
	SET NOCOUNT ON;

	IF(dbo.TestAn(@an_nou) = 1 AND dbo.TestString(@nume) = 1)
	BEGIN
		
		UPDATE Autori SET an_debut = @an_nou
		WHERE nume = @nume;

		PRINT 'Update-ul efectuat cu succes'
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Autori';
		RETURN;
	END;
END;
GO

CREATE OR ALTER PROCEDURE DeleteAutoriCRUD
	@nume VARCHAR(50)
AS
BEGIN
	SET NOCOUNT ON;

	IF(dbo.TestString(@nume) = 1)
	BEGIN
		
		DELETE FROM Autori
		WHERE nume = @nume;

		PRINT 'Stergere efectuata cu succes'
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Autori';
		RETURN;
	END;
END;
GO

exec ReadAutoriCRUD;
exec CreateAutoriCRUD 'Ion B', 1992;
exec DeleteAutoriCRUD 'Ion B';
exec UpdateAutoriCRUD 'Ion B',2011;

	
	go



--------------------CartiAutori--------------------

CREATE OR ALTER PROCEDURE ReadCartiAutoriCRUD
AS
BEGIN
	SET NOCOUNT ON;
		SELECT * FROM CartiAutori;
END;
GO

CREATE OR ALTER PROCEDURE CreateCartiAutoriCRUD
	@carte_id INT,
	@autor_id INT
AS
BEGIN
	SET NOCOUNT ON;

	IF(dbo.TestCarteId(@carte_id) = 1 AND dbo.TestAutorId(@autor_id)  = 1)
	BEGIN
		INSERT INTO CartiAutori(carte_id,autor_id)
		VALUES (@carte_id,@autor_id);

		PRINT 'inserare pentru tabelul CarteAutor';
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul CarteAutor';
		RETURN;
	END;
END;
GO

CREATE OR ALTER PROCEDURE DeleteCartiAutoriCRUD
	@carte_id INT,
	@autor_id INT
AS
BEGIN
	SET NOCOUNT ON;

	IF(dbo.TestCarteId(@carte_id) = 1 AND dbo.TestAutorId(@autor_id)  = 1)
	BEGIN
		DELETE FROM CartiAutori
		WHERE carte_id = @carte_id;

		PRINT 'stergere pentru tabelul CarteAutor';
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul CarteAutor';
		RETURN;
	END;
END;
GO

CREATE OR ALTER PROCEDURE UpdateCartiAutoriCRUD
	@carte_id INT,
	@autor_id INT,
	@id_nou INT
AS
BEGIN
	SET NOCOUNT ON;

	IF(dbo.TestCarteId(@carte_id) = 1 AND dbo.TestAutorId(@autor_id)  = 1)
	BEGIN
		UPDATE CartiAutori SET autor_id = @id_nou
		WHERE carte_id = @carte_id AND autor_id = @autor_id;

		PRINT 'update pentru tabelul CarteAutor';
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul CarteAutor';
		RETURN;
	END;
END;
GO

select * from CartiAutori;
select * from Autori;

exec ReadCartiAutoriCRUD;
exec CreateCartiAutoriCRUD 2765,1;
exec DeleteCartiAutoriCRUD 2765,1;
exec UpdateCartiAutoriCRUD 2762,2,1;

go


----------------------------------Views---------------------

CREATE OR ALTER VIEW CartiView
AS
	SELECT carte_id, pret FROM Carti 
	WHERE pret > 40;
GO

select * from CartiView

IF EXISTS (SELECT NAME FROM sys.indexes WHERE name = 'N_idx_carti_pret')
DROP INDEX N_idx_carti_pret ON Carti
CREATE NONCLUSTERED INDEX N_idx_carti_pret ON Carti(pret);

GO

CREATE OR ALTER VIEW CartiAutoriView
AS
	SELECT c.carte_id, a.autor_id FROM Carti c
	INNER JOIN CartiAutori ca ON ca.carte_id = c.carte_id
	INNER JOIN Autori a ON a.autor_id = ca.autor_id
	WHERE c.carte_id >2800;
GO

select * from CartiAutoriView

IF EXISTS (SELECT NAME FROM sys.indexes	WHERE name = 'N_idx_cartiautori_carteId')
DROP INDEX N_idx_cartiautori_carteId ON CartiAutori
CREATE NONCLUSTERED INDEX N_idx_cartiautori_carteId ON CartiAutori(carte_id);


IF EXISTS (SELECT NAME FROM sys.indexes	WHERE name = 'N_idx_cartiautori_autorId')
DROP INDEX N_idx_cartiautori_autorId ON CartiAutori
CREATE NONCLUSTERED INDEX N_idx_cartiautori_autorId ON CartiAutori(autor_id);