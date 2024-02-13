;14. sa se construiasca lista nodurilor unui arbore de tipul (2) parcurs in postordine(SDR)

;        A
;		/ \
;	   B   C
;	      / \
;		 D   E      ->    l = (A (B) (C (D) (E)))    (2)  -(postordine)-> (B D E C A)

; (car l)   -> radacina 		    R l1
; (cadr l)  -> subarborele stang 	S l2
; (caddr l) -> subarborele drept 	D l3


;postordine(l1,l2,l3) = { [], 										daca n=0
;						{ postordine(l2) + postordine(l3) + l1, 	alfel


(defun postordine(l)
	(cond
		((null l) nil)
		(t (append (postordine (cadr l)) (append (postordine (caddr l)) (list (car l)))))
		;(t (append (postordine (cadr l)) (adaugFin (car l) (postordine (caddr l)))))
	)
)

;adauga elementul e la finalul listei l
(defun adaugFin(e l)
	(cond                                          ;--> daca lista e vida
		((null l) (list e))                        ;-->  cons(e nil)    <=> list e
		(t (cons ( car l) (adaugFin e (cdr e))))
	)
)


                ;             A
				;         /      \     --> (A (B (D (F (K)) (G)) (E)) (C (H (I () (J)))))
				;        B        C
				;       / \      / 
				;      D   E    H        ----(postordine)---->  (K F G D E B J I H C A)
				;    / \       /
				;   F   G     I   
				;  /          \
				; K            J