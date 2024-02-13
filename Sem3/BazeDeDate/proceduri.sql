use MangaStore;
go

create procedure Modifica
as
begin
alter table Carti
alter column titlu varchar(300)
print 'valoare modificata cu succes'
end;
go

create procedure ModificaINV
as
begin
alter table Carti
alter column titlu varchar(200)
print 'modificare inversata cu succes'
end;
go



create procedure ValoareImplicitaConstraint
as
begin
alter table Achizitii
add constraint df_cantitate default 0 for cantitate
print 'constrangere adaugata cu succes'
end;
go

create procedure ValoareImplicitaConstraintINV
as
begin
alter table Achizitii
drop constraint df_cantitate
print 'constrangere stearsa cu succes'
end;
go



create procedure AdaugaTabela
as
begin
create table Reduceri(
	reducere_id int primary key identity,
	procent int
	);
print 'tabel creat cu succes'
end;
go

create procedure AdaugaTabelaINV
as
begin
drop table Reduceri
print 'tabel sters cu succes'
end;
go



create procedure AdaugaCamp
as
begin
alter table Carti
add reducere_id int
print 'coloana adaugata cu succes'
end;
go

create procedure AdaugaCampINV
as
begin
alter table Carti
drop column reducere_id
print 'coloana stearsa cu succes'
end;
go



create procedure ConstrangereCheieStraina
as
begin
alter table Carti
add constraint fk_reducere_id foreign key (reducere_id) references Reduceri(reducere_id)
print 'cheie straina adaugata cu succes'
end;
go

create procedure ConstrangereCheieStrainaINV
as
begin
alter table Carti
drop constraint fk_reducere_id
print 'cheie straina stearsa cu succes'
end;
go




create table Versiune(
	vers INT);

insert into Versiune(vers)
values (0);
select * from Versiune;
go




create procedure ModificaVersiune @versiune varchar(2)
as
begin

if(@versiune <> '0' and @versiune <> '1' and @versiune <> '2' and @versiune <> '3' and @versiune <> '4' and @versiune <> '5')
begin 
	raiserror('versiunea trebuie sa fie intre 0 si 5!',16,1);
	return
end;

declare @versiuneNoua as int;
set @versiuneNoua = cast(@versiune as int);

if(@versiuneNoua <0 or @versiuneNoua > 5)
begin
	raiserror('versiunea trebuie sa fie intre 0 si 5!',16,1);
	return
end;

declare @versiuneCurenta as int;
set @versiuneCurenta = (select vers from Versiune);

if(@versiuneCurenta = @versiuneNoua)
begin	
	print 'baza de date se afla deja in aceasta versiune';
	return 
end;

while(@versiuneCurenta <> @versiuneNoua)
begin

	if(@versiuneCurenta < @versiuneNoua)
	begin
		
		if(@versiuneCurenta = 0)
			exec Modifica;
		if(@versiuneCurenta = 1)
			exec ValoareImplicitaConstraint;
		if(@versiuneCurenta=2)
			exec AdaugaTabela;
		if(@versiuneCurenta=3)
			exec AdaugaCamp;
		if(@versiuneCurenta=4)
			exec ConstrangereCheieStraina;
		set @versiuneCurenta = @versiuneCurenta +1;
	end;

	if(@versiuneCurenta > @versiuneNoua)
	begin

		if(@versiuneCurenta = 1)
			exec ModificaINV;
		if(@versiuneCurenta = 2)
			exec ValoareImplicitaConstraintINV;
		if(@versiuneCurenta = 3)
			exec AdaugaTabelaINV;
		if(@versiuneCurenta = 4)
			exec AdaugaCampINV;
		if(@versiuneCurenta = 5)
			exec ConstrangereCheieStrainaINV;
		set @versiuneCurenta =@versiuneCurenta-1;
	end;
end;

update Versiune
set vers = @versiuneNoua;

print 'baza de date a fost actualizata la versiunea ' + cast(@versiuneNoua as varchar);

end;
go

exec ModificaVersiune 0;

select * from Versiune;