% basicas.pl - Reglas basicas: constantes, potencias, linealidad, a^x, a^u
% Enunciado (a) a (j). Define clausulas de regla/5 (ver integrador.pl).

% i) integral de a^x dx = a^x / ln(a)      (a > 0)
%    Si a = 1 el integrando es 1 y la antiderivada es x.
regla(i, Params, A, B, R) :-
    param(base, Params, Base),
    (   Base =< 0
    ->  R = error('a debe ser mayor que 0')
    ;   Base =:= 1
    ->  con_area(x, A, B, R)
    ;   con_area(Base^x / ln(Base), A, B, R)
    ).