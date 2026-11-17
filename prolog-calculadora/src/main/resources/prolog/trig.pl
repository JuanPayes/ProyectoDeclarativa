% trig.pl - sin, cos, tan y sus inversas asin, acos, atan
% Enunciado (k) a (p). Define clausulas de regla/5 (ver integrador.pl).
%
% Nota para el equipo: tuProlog es mas estricto que otros Prolog con la
% sintaxis. Evitar expresiones aritmeticas dentro de una comparacion
% (Hi - Lo >= Pi). Mejor:   Dif is Hi - Lo, Dif >= Pi.

% Juan Pablo Payes Montalvo 00005923
% m) integral de tan(x) dx = -ln|cos(x)|
%    No esta definida si [a, b] contiene una asintota: x = pi/2 + k*pi.
regla(m, _, A, B, R) :-
    (   tiene_asintota_tan(A, B)
    ->  R = error('tan x no esta definida en todo [a, b]: hay una asintota en pi/2 + k*pi')
    ;   con_area(-(ln(abs(cos(x)))), A, B, R)
    ).

% Las asintotas estan separadas por pi y cos(x) cambia de signo en cada una:
%  - si el intervalo mide pi o mas, seguro contiene una;
%  - si mide menos, contiene una solo si cos cambia de signo entre los
%    extremos, o si un extremo cae (casi) sobre ella.
tiene_asintota_tan(A, B) :-
    ordenar(A, B, Lo, Hi),
    Pi is 4 * atan(1),
    Dif is Hi - Lo,
    (   Dif >= Pi
    ->  true
    ;   extremo_o_cambio_de_signo(Lo, Hi)
    ).

extremo_o_cambio_de_signo(Lo, Hi) :-
    Eps is 1.0 / 1000000.0 / 1000000.0,
    CL is cos(Lo),
    CH is cos(Hi),
    AL is abs(CL),
    AH is abs(CH),
    Prod is CL * CH,
    (   AL < Eps -> true
    ;   AH < Eps -> true
    ;   Prod < 0
    ).

% ordenar(+A, +B, -Menor, -Mayor)
ordenar(A, B, A, B) :- A =< B, !.
ordenar(A, B, B, A).

% Roberto Eliseo Martinez Rodríguez 00044623
% k) integral de sin(x) dx = -cos(x)
%    sin(x) es continua en todos los reales: no hay restricciones de dominio.
regla(k, _, A, B, R) :-
    con_area(-(cos(x)), A, B, R).

% Alison Michelle Maltez Valencia 00100121
% l) integral de cos(x) dx = sin(x)
%    cos(x) es continua en todos los reales: no hay restricciones de dominio.
regla(l, _, A, B, R) :-
    con_area(sin(x), A, B, R).
