% hiperbolicas.pl - sinh, cosh, tanh y sus inversas
% Enunciado (q) a (v). Define clausulas de regla/5 (ver integrador.pl).

% Juan Pablo Payes Montalvo 00005923
% r) integral de cosh(x) dx = sinh(x)
regla(r, _, A, B, R) :-
    con_area(sinh(x), A, B, R).