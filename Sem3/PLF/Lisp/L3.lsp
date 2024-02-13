; L3. 1) sa se construiasca o functie care intoarce adancimea unei liste

;depth(l) = { 0 , daca l e atom
;			{ 1 + max({depth(l1),depth(l2),...,depth(ln)}) , altfel

(defun depth(l)
	(cond
		((atom l) 0)
		(t (+ 1 (apply #'max (mapcar #'depth l))))
)	)
							   
				