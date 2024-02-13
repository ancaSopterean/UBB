;;1. a)Sa se insereze intr-o lista liniara un atom a dat dupa al 2-lea, al 4-lea, al 6-lea,....element.

(defun insereazaPareAux(e l poz)
	(cond 
		((null l) nil)
		((= (mod poz 2) 0) (cons (car l) (cons e (insereazaPareAux e (cdr l) (+ poz 2)))))
		(t (cons (car l) (insereazaPareAux e (cdr l) (+ poz 1))))
	)
)

(defun insereazaPare(e l)
	(insereazaPareAux e l 1)
)

;insereazaPareAux(e,l1..ln,poz) =   { [],  											daca n=0
;									{ l1 + e + insereazaPareAux(e,l2..ln, poz+2),   daca poz % 2 == 0  
;									{ l1 + insereazaPareAux(e, l2..ln, poz+1),      altfel

;insereazaPare(e, L) = insereazaPareAux(e, L, 1)



;;b) Definiti o functie care obtine dintr-o lista data lista tuturor atomilor care apar, pe orice nivel, 
;; dar in ordine inversa. De exemplu: (((A B) C)(D E)) --> (E D C B A)

(defun listaAtomiAux(l col)
	(cond 
		((null l) col)
		((listp (car l)) (listaAtomiAux (car l) col))
		(t (listaAtomiAux (cdr l) (cons (car l) col)))
	)
)

(defun listaAtomi(l)
	(listaAtomiAux l nil)
)

;listaAtomiAux(l1..ln, col) = { col,  							   daca n=0
;							  { listaAtomiAux(l1, col),       daca l1 e lista
;							  { listaAtomiAux(l2..ln, l1 + col),   altfel

;listaAtomi(L) = listaAtomiAux(L, [])




;;c) Definiti o functie care intoarce cel mai mare divizor comun al numerelor dintr-o lista neliniara.
;(1 2 (3 4 (6))) -> 6

(defun cmmdc(a b)
	(cond
		((= b 0) a)
		(t (cmmdc b (mod a b)))
	)
)

;cmmdc(a,b) =  { a,  				daca b = 0
;              { cmmdc( b, a % b),  altfel

(defun cmmdcListaAux(l curent)
	(cond 
		((null l) curent)
		((numberp (car l)) (cmmdcListaAux (cdr l) (cmmdc (car l) curent)))
		((listp (car l)) (cmmdcListaAux (cdr l) (cmmdcListaAux (car l) curent)))
	)
)

;cmmdcListaAux(l1..ln, curent) =  { curent,                                            daca n = 0
;								  { cmmdcListaAux(l2..ln, cmmdc(l1, curent)) ,         daca l1 e numarul
;   							  { cmmdcListaAux(l2..ln, cmmdcListaAux(l1, curent)),  daca l1 e lista

(defun cmmdcLista(l)
	(cond
		((listp (car l)) (cmmdcListaAux l (car (car l))))
		((numberp (car l)) (cmmdcListaAux l (car l)))
	)
)

;cmmdcLista(l1..ln) = { cmmdcListaAux(l1..ln, l11), 	daca l1 e lista, unde l11 e primul element al listei l1
;					  { cmmdcListaAux(l1..ln, l1), 		daca l1 e numar






;;d) Sa se scrie o functie care determina numarul de aparitii ale unui atom dat intr-o lista neliniara

(defun nrAp(l e)
	(cond 
		((null l) 0)
		((numberp (car l))
			(cond	
				((= (car l) e) (+ 1 (nrAp (cdr l) e)))
				(t (nrAp (cdr l) e))
			)
		)
		((listp (car l)) (+ (nrAp (car l) e) (nrAp (cdr l) e)))
	)
)

;nrAp(L, e) = { 0, daca n = 0
;			  { 1 + nrAp(l2..ln, e),                daca l1 e numar si l1 = e
;			  { nrAp(l2..ln, e),                    daca l1 e numar si l1 != e
;             {  nrAp(l1, e) + nrAp(l2..ln, e),     daca l1 e lista