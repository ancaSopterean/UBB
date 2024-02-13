%1. a)
%verifica daca X e membru al listei
membru(X,[X|_]).
membru(X,[_|T]):-membru(X,T).

%adauga lista L2 la finalul listei L1
append([],L2,L2).
append([H|T],L2,[H|L3]):-append(T,L2,L3).


%reverse(l1..ln, col) = { col, n=0
%                       { reverse(l2..ln, l1+col), altfel

reverse([],Col,Col):-!.
reverse([H|T],Col,Rez):-
    reverse(T,[H|Col],Rez).
revMain(L,R):-reverse(L,[],R).


%diferenta a doua liste
diff([],_,[]).
diff([H|T],L2,Rez):- %(i,i,o)
    membru(H,L2),
    !,
    diff(T,L2,Rez).
diff([H|T],L2,[H|Rez]):-
    diff(T,L2,Rez).


%1. b)
%adauga elemetnul 1 dupa fiecare numar par
add([],[]).

%
add([H|T],[H,1|Rez]):-  %(i,o)
    H mod 2 =:= 0,
    add(T,Rez).
%
add([H|T],[H|Rez]):-
    H mod 2 =\= 0,
    add(T,Rez).


