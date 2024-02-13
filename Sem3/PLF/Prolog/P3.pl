%10. Se da sirul a1,..., an cu elemente numere intregi distincte. Se
% cere sa se determine toate submultimile avand suma divizibila cu n.

%candidat(L-list,E-int), model de flux (i,o)
%L-lista din care se iau candidati
%E-elementul ce va primi pe rand cate o valoare din L
candidat([E|_],E).
candidat([_|T],E):-candidat(T,E).


%sumList(L-list, ,Sum-int), model de flux (i,o)
%L-lista a caror elemente se va face suma
%Sum-suma elementelor listei
sumList([],0).
sumList([H|T],Sum):-
    sumList(T,Sum1),
    Sum is H+Sum1.

%sub(L-list, N-int, Col-list, R-list), model de flux (i,i,i,o)
%L-lista pe care se vor efectua operatiile
%N- numarul cu care trebuie sa fie divizibila suma elementelor
% Col- colectoarea in care punem elem din lista si le verificam suma
sub(L,N,Col,Rez):-
    candidat(L,E),
 \+ candidat(Col,E),
    sub(L,N,[E|Col],Rez).

sub(_,N,Col,Rez):-
    sumList(Col, Sum),
    Sum mod N =:= 0,
    Rez = Col.

%subMain(L-list,N-int,Rez-list), model de flux (i,i,o)
%L-lista pe care se vor efectua operatiile
%N- numarul cu care trebuie sa fie divizibila suma elementelor
subMain(L,N,Rez):-sub(L,N,[],Rez).

subLista(L,N,R):-findall(Rez,subMain(L,N,Rez),R).


