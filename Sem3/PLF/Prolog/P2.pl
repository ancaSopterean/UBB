% 4. a) sa se nterclaseze fara pastrarea dublurilor doua liste sortate

%interclaseaza(L1:list, L2:list, R:rez), (i,i,o), determinist
%L1: prima lista sortata
%L2: a doua lsita sortata
%R: lista rezultata din interclasarea celor doua

interclaseaza([],L,L).
interclaseaza(L,[],L).
interclaseaza([H1|T1],[H2|T2],[H1|Rez]):-
    H1<H2,
    interclaseaza(T1,[H2|T2],Rez).
interclaseaza([H|T1],[H|T2],[H|Rez]):-
    interclaseaza(T1,T2,Rez).
interclaseaza([H1|T1],[H2|T2],[H2|Rez]):-
    H2<H1,
    interclaseaza([H1|T1],T2,Rez).



%b) sa se interclaseze fara pastrarea dubllurilor toate sublistele

%elimina_duplicat(L:list, R:list) (i,o) determinist
%L:lista din care vrem sa eliminam duplicatele
%R:lista dupa eliminarea duplicatelor

elimina_duplicat([],[]).
elimina_duplicat([H|T],Rez):-
    %daca H se regaseste in sublista atunci va fi omis din lst finala
    member(H,T),!,
    elimina_duplicat(T,Rez).
   % member(H,Rez).
elimina_duplicat([H|T], [H|Rez]):-
    elimina_duplicat(T,Rez).


%interclaseaza_subliste(L1:list, L2:list) (i,o) determinist
%L1: lista cu elemente pe care dorim sa o prelucram
%L2: lista dupa eliminarea duplicatelor

interclaseaza_subliste([],[]).
interclaseaza_subliste([H|T],RezFin):-
    is_list(H),!,
    interclaseaza_subliste(T,Rez1),
    append(H,Rez1,Rez2),
    elimina_duplicat(Rez2, RezFin).
interclaseaza_subliste([H|T],[H|RezFin]):-
    interclaseaza_subliste(T,RezFin).





