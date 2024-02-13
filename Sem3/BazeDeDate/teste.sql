USE MangaStore;
GO

INSERT INTO Tables (Name)
VALUES  ('Categorii'),
		('Carti'),
		('CartiAutori');

SELECT * FROM Tables;
GO


CREATE VIEW CartiView AS
	SELECT titlu, pret
	FROM Carti
GO

SELECT * FROM CartiView;
GO

CREATE VIEW CategoriiView AS
	SELECT titlu, categorie, pret
	FROM Categorii c INNER JOIN Carti ct ON c.categorie_id = ct.categorie_id
GO

SELECT * FROM CategoriiView;
GO

CREATE VIEW CartiAutoriView AS
	SELECT titlu, COUNT(c.carte_id) AS numarAutori
	FROM Carti c INNER JOIN CartiAutori ca ON c.carte_id = ca.carte_id
	GROUP BY c.titlu;
GO

SELECT * FROM CartiAutoriView;
GO


INSERT INTO Views (Name)
VALUES	('CategoriiView'),
		('CartiView'),
		('CartiAutoriView');

SELECT * FROM Views;
GO 


INSERT INTO Tests (Name)
VALUES	('DI_CategoriiCarti_CategoriiView'),
		('DI_Carti_CartiView'),
		('DI_CartiAutori_CartiAutoriView');
SELECT * FROM Tests;
GO


INSERT INTO TestTables (TestID, TableID, NoOfRows, Position)
VALUES	(1, 1, 100, 1),
		(2, 1, 50, 1),
		(2, 2, 50, 2),
		(3, 1, 100, 1),
		(3, 2, 100, 2),
		(3, 3, 100, 3);


SELECT * FROM TestTables;
GO

INSERT INTO TestViews (TestID, ViewID)
VALUES	(1, 1),
		(2, 2),
		(3, 3);
		
SELECT * FROM TestViews;
GO


CREATE PROCEDURE insertCategoriiCarti @NoOfRows INT
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @categorie VARCHAR(50);
	DECLARE @n INT = 1;
	DECLARE @current_id INT = 1;


	WHILE @n <= @NoOfRows
	BEGIN
		SET @categorie = 'Categorie' + CONVERT(VARCHAR(10), @current_id);
		INSERT INTO Categorii (categorie)
		VALUES (@categorie);
		SET @current_id = @current_id + 1;
		SET @n = @n + 1;
	END

	PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @NoOfRows) + ' categorii'; 
END
GO

EXEC insertCategoriiCarti 100;
GO

CREATE PROCEDURE insertCarti @NoOfRows INT
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @fk INT = 
			(SELECT MAX(c.categorie_id) FROM Categorii c);
	DECLARE @titlu VARCHAR(50);
	DECLARE @pret INT;
	DECLARE @n INT = 1;
	DECLARE @current_id INT = 1;

	WHILE @n <= @NoOfRows
	BEGIN
		SET @titlu = 'titlu' + CONVERT(VARCHAR(10), @current_id);
    	SET @pret = 40;
		INSERT INTO Carti (titlu, pret, categorie_id)
		VALUES (@titlu, @pret, @fk);
		SET @current_id = @current_id + 1;
		SET @n = @n + 1;
	END

	PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @NoOfRows) + ' carti';
END
GO


CREATE PROCEDURE insertCartiAutori @NoOfRows INT 
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @n INT = 0;
	DECLARE @fk1 INT;
	INSERT INTO Autori (nume, an_debut)
	VALUES ('test', 9999);
	DECLARE @fk2 INT = 
			(SELECT a.autor_id FROM Autori a WHERE a.an_debut = 9999);
	
	DECLARE cursorCarti CURSOR FAST_FORWARD FOR
	SELECT c.carte_id FROM Carti c WHERE c.titlu LIKE 'titlu%';
	
	OPEN cursorCarti;
	
	FETCH NEXT FROM cursorCarti INTO @fk1;
	WHILE (@n < @NoOfRows) AND (@@FETCH_STATUS = 0)
	BEGIN
		INSERT INTO CartiAutori(carte_id, autor_id)
		VALUES (@fk1, @fk2);
		SET @n = @n + 1;
		FETCH NEXT FROM cursorCarti INTO @fk1;
	END

	CLOSE cursorCarti;
	DEALLOCATE cursorCarti;

	PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @n) + ' carti-autori';
END
GO



CREATE PROCEDURE insertTable @idTest INT
AS
BEGIN
	DECLARE @numeTest NVARCHAR(50) =
			(SELECT t.Name FROM Tests t WHERE t.TestID = @idTest);
	DECLARE @numeTabel NVARCHAR(50);
	DECLARE @NoOfRows INT;
	DECLARE @procedura VARCHAR(50);

	DECLARE cursorTabele CURSOR FORWARD_ONLY FOR
	SELECT ta.Name, te.NoOfRows 
	FROM TestTables te INNER JOIN Tables ta ON te.TableID = ta.TableID
	WHERE te.TestID = @idTest
	ORDER BY te.Position;

	OPEN cursorTabele;

	FETCH NEXT FROM cursorTabele INTO @numeTabel, @NoOfRows;
	WHILE @@FETCH_STATUS = 0
	BEGIN
		SET @procedura = 'insert' + @numeTabel;
		EXEC @procedura @NoOfRows;
		FETCH NEXT FROM cursorTabele INTO @numeTabel, @NoOfRows;
	END

	CLOSE cursorTabele;
	DEALLOCATE cursorTabele;
END
GO




CREATE PROCEDURE deleteCategoriiCarti
AS
BEGIN
	SET NOCOUNT ON;

	DELETE FROM Categorii;

	PRINT 'S-au sters ' + CONVERT(VARCHAR(10), @@ROWCOUNT) + ' categorii';
END
GO


EXEC deleteCategoriiCarti;
GO

CREATE PROCEDURE deleteCarti
AS
BEGIN
	SET NOCOUNT ON;

	DELETE FROM Carti;

	PRINT 'S-au sters ' + CONVERT(VARCHAR(10), @@ROWCOUNT) + ' carti';
END
GO

EXEC deleteCarti;
GO

CREATE PROCEDURE deleteCartiAutori
AS
BEGIN
	SET NOCOUNT ON;
	
	DELETE FROM CartiAutori;
	DELETE FROM Autori
	WHERE an_debut = 9999;

	PRINT 'S-au sters ' + CONVERT(VARCHAR(10), @@ROWCOUNT) + ' carti-autori';
END
GO

EXEC deleteCartiAutori;
GO


CREATE PROCEDURE deleteTable @idTest INT
AS
BEGIN
	DECLARE @numeTest NVARCHAR(50) =
			(SELECT t.Name FROM Tests t WHERE t.TestID = @idTest);
	DECLARE @numeTabel NVARCHAR(50);
	DECLARE @procedura VARCHAR(50);

	DECLARE cursorTabele CURSOR FORWARD_ONLY FOR
	SELECT ta.Name 
	FROM TestTables te INNER JOIN Tables ta ON te.TableID = ta.TableID
	WHERE te.TestID = @idTest
	ORDER BY te.Position DESC;

	OPEN cursorTabele;

	FETCH NEXT FROM cursorTabele INTO @numeTabel;
	WHILE @@FETCH_STATUS = 0
	BEGIN
		SET @procedura = 'delete' + @numeTabel;
		EXEC @procedura;
		FETCH NEXT FROM cursorTabele INTO @numeTabel;
	END

	CLOSE cursorTabele;
	DEALLOCATE cursorTabele;
END
GO


CREATE PROCEDURE selectView @idTest INT
AS
BEGIN
	DECLARE @viewName NVARCHAR(50) =
			(SELECT v.Name FROM Views v INNER JOIN TestViews tv on tv.ViewID = v.ViewID
			 WHERE tv.TestID = @idTest);
	DECLARE @select VARCHAR(50) = 'SELECT * FROM ' + @viewName;

	EXEC (@select);
END
GO


DROP PROCEDURE selectView;
GO




CREATE PROCEDURE runTest
AS
BEGIN
	DECLARE @ds DATETIME;
	DECLARE @de DATETIME;
	DECLARE @dsAll DATETIME = NULL;
	DECLARE @idTest INT = 1;
	DECLARE @tableID INT;
	DECLARE @viewID INT;

	INSERT INTO TestRuns (Description, StartAt, EndAt)
	VALUES ('Test Database', null, null);

	DECLARE @testRunID INT =
			(SELECT MAX(tr.TestRunID) FROM TestRuns tr);

	WHILE @idTest < 4
	BEGIN
		SET @ds = GETDATE();
		IF(@dsAll is NULL)
		BEGIN
			SET @dsAll = @ds;
		END;

		EXEC deleteTable @idTest;
		EXEC insertTable @idTest;

		SET @de = GETDATE();

		SET @tableID =
			(SELECT ta.TableID FROM Tests te
			INNER JOIN TestTables tt ON tt.TestID = te.TestID
			INNER JOIN Tables ta ON ta.TableID = tt.TableID
			WHERE tt.TestID = @idTest AND
			te.Name LIKE 'DI_' + ta.Name + '[_]%');
	
		INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt)
		VALUES (@testRunID, @tableID, @ds, @de);

		SET @idTest = @idTest + 1;
	END

	UPDATE TestRuns SET StartAt = @dsAll WHERE TestRunID = @testRunID;

	SET @idTest = 1;
	WHILE @idTest < 4
	BEGIN
		SET @ds = GETDATE();

		EXEC selectView @idTest;

		SET @de = GETDATE();

		SET @viewID =
			(SELECT v.ViewID FROM Views v
			INNER JOIN TestViews tv ON tv.ViewID = v.ViewID
			WHERE tv.TestID = @idTest);
	
		INSERT INTO TestRunViews (TestRunID, ViewID, StartAt, EndAt)
		VALUES (@testRunID, @viewID, @ds, @de);

		SET @idTest = @idTest + 1;
	END

	UPDATE TestRuns SET EndAt = @de WHERE TestRunID = @testRunID;	

	PRINT 'Testul a rulat pentru ' + CONVERT(VARCHAR(10), DATEDIFF(millisecond, @de, @dsAll)) + ' milisecunde';
END
GO



EXEC runTest;
GO

SELECT * FROM TestRuns;
select * from TestRunTables;
select * from TestRunViews;

delete from TestRuns;

SELECT * FROM Tables;
