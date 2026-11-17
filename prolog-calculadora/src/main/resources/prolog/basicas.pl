% basicas.pl - Reglas basicas: constantes, potencias, linealidad, a^x, a^u

% Carlos Oswaldo Rubio Cuellar 00000822
% a) integral de k dx = k*x
%    Una constante es continua en todos los reales: no hay restricciones.
regla(a, Params, A, B, R) :-
    param(k, Params, K),
    por(K, x, F),
    con_area(F, A, B, R).

% Carlos Oswaldo Rubio Cuellar 00000822
% c) integral de (k*x) dx = k * x^2 / 2
%    k*x es continua en todos los reales: no hay restricciones.
regla(c, Params, A, B, R) :-
    param(k, Params, K),
    k_por_x2_medios(K, F),
    con_area(F, A, B, R).

% k_por_x2_medios(+K, -F): F = k*x^2/2 sin escribir 1*... ni k*(x^2/2).
k_por_x2_medios(K, x^2 / 2) :- K =:= 1, !.
k_por_x2_medios(K, -(x^2 / 2)) :- K =:= -1, !.
k_por_x2_medios(K, K * x^2 / 2).

% Carlos Oswaldo Rubio Cuellar 00000822
% d) integral de x^n dx, con n entero
%    n =  0  ->  x
%    n = -1  ->  ln|x|
%    otro n  ->  x^(n+1) / (n+1)
%    Si n < 0, x^n no esta definida en x = 0: si 0 esta en [a, b] la
%    integral es impropia y se devuelve un error.
regla(d, Params, A, B, R) :-
    param(n, Params, N),
    (   \+ integer(N)
    ->  R = error('n debe ser un numero entero.')
    ;   N < 0, contiene_cero(A, B)
    ->  R = error('x^n no esta definida en x = 0 (n < 0) y 0 esta en [a, b]: la integral es impropia.')
    ;   potencia_x(N, F),
        con_area(F, A, B, R)
    ).

potencia_x(0, x) :- !.
potencia_x(-1, ln(abs(x))) :- !.
potencia_x(-2, -(x^ -1)) :- !.
potencia_x(N, F) :-
    M is N + 1,
    (   M > 0
    ->  F = x^M / M
    ;   D is -M,
        F = -(x^M / D)
    ).

% contiene_cero(+A, +B): el intervalo [a, b] (en cualquier orden) contiene x = 0.
contiene_cero(A, B) :-
    ordenar(A, B, Lo, Hi),
    Lo =< 0,
    Hi >= 0.

% Juan Pablo Payes Montalvo 00005923
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

% Roberto Eliseo Martinez Rodríguez 00044623
% b) integral de x dx = x^2 / 2
regla(b, _, A, B, R) :-
    con_area(x^2 / 2, A, B, R).

% Roberto Eliseo Martinez Rodríguez 00044623
% g) integral de (u + v) dx = integral de u dx + integral de v dx
%    u y v se integran con integrar/2 (abajo). Antes de calcular el area se
%    revisa que u + v sea continua en [a, b]; si no, la integral es impropia.
regla(g, Params, A, B, R) :-
    param(u, Params, U0),
    param(v, Params, V0),
    normalizar(U0, U),
    normalizar(V0, V),
    integral_suma(U, V, A, B, R).

integral_suma(U, _, _, _, error('No hay una regla para integrar u(x). Se admiten sumas, restas, constante*funcion, x^n, 1/x, sin, cos, tan, exp, a^x, ln, sqrt, sinh, cosh y tanh de x o de (k*x + c).')) :-
    \+ integrar(U, _), !.
integral_suma(_, V, _, _, error('No hay una regla para integrar v(x). Se admiten sumas, restas, constante*funcion, x^n, 1/x, sin, cos, tan, exp, a^x, ln, sqrt, sinh, cosh y tanh de x o de (k*x + c).')) :-
    \+ integrar(V, _), !.
integral_suma(U, V, A, B, error('u(x) + v(x) no es continua en todo [a, b] (division entre cero, asintota o fuera de dominio): la integral es impropia.')) :-
    \+ continua(U + V, A, B), !.
integral_suma(U, V, A, B, R) :-
    integrar(U, FU),
    integrar(V, FV),
    sumar(FU, FV, F),
    con_area(F, A, B, R).

% ---------------------------------------------------------------------
% integrar(+Expr, -F): antiderivada simbolica de Expr respecto de x.
% Falla si no hay regla. Reutilizable para e), f), h).
% L representa un argumento lineal k*x + c (ver lineal/2); el resultado se
% divide entre k (regla de la cadena para argumentos lineales).
% ---------------------------------------------------------------------
integrar(C, F) :- constante(C), !, por(C, x, F).
integrar(x, x^2 / 2) :- !.
integrar(-(U), F) :- !, integrar(U, FU), negar(FU, F).
integrar(U + V, F) :- !, integrar(U, FU), integrar(V, FV), sumar(FU, FV, F).
integrar(U - V, F) :- !, integrar(U, FU), integrar(V, FV), restar(FU, FV, F).
integrar(C * U, F) :- constante(C), !, integrar(U, FU), por(C, FU, F).
integrar(U * C, F) :- constante(C), !, integrar(U, FU), por(C, FU, F).
integrar(U / C, F) :- constante(C), !, valor(C, CV), CV =\= 0, integrar(U, FU), entre(FU, CV, F).
integrar(C / D, F) :- constante(C), reciproco(D, R1), !, integrar(R1, F1), por(C, F1, F).
integrar(L ^ N, F) :- constante(N), lineal(L, K), !,
    valor(N, NV),
    (   NV =:= -1
    ->  entre(ln(abs(L)), K, F)
    ;   M is NV + 1,
        D is M * K,
        entre(L ^ M, D, F)
    ).
integrar(Base ^ L, F) :- constante(Base), lineal(L, K), !,
    valor(Base, BV),
    BV > 0,
    (   BV =:= 1
    ->  F = x
    ;   LnB is log(BV),
        D is LnB * K,
        entre(Base ^ L, D, F)
    ).
integrar(sqrt(L), F) :- lineal(L, _), !, integrar(L ^ 0.5, F).
integrar(exp(L), F) :- lineal(L, K), !, entre(exp(L), K, F).
integrar(sin(L), F) :- lineal(L, K), !, entre(cos(L), K, G), negar(G, F).
integrar(cos(L), F) :- lineal(L, K), !, entre(sin(L), K, F).
integrar(tan(L), F) :- lineal(L, K), !, entre(ln(abs(cos(L))), K, G), negar(G, F).
integrar(ln(L), F) :- lineal(L, K), !, entre(L * ln(L) - L, K, F).
integrar(sinh(L), F) :- lineal(L, K), !, entre(cosh(L), K, F).
integrar(cosh(L), F) :- lineal(L, K), !, entre(sinh(L), K, F).
integrar(tanh(L), F) :- lineal(L, K), !, entre(ln(cosh(L)), K, F).

% reciproco(+D, -E): E = 1/D cuando se sabe integrar (1/L, 1/L^n).
reciproco(L ^ N, L ^ M) :- constante(N), lineal(L, _), !, valor(N, NV), M is -NV.
reciproco(L, L ^ (-1)) :- lineal(L, _).

% lineal(+L, -K): L es k*x + c con k =/= 0; K es el coeficiente de x.
lineal(L, K) :- coef_lineal(L, K), K =\= 0.

coef_lineal(x, 1) :- !.
coef_lineal(-(U), K) :- !, coef_lineal(U, K0), K is -K0.
coef_lineal(C * U, K) :- constante(C), !, coef_lineal(U, K0), valor(C, CV), K is CV * K0.
coef_lineal(U * C, K) :- constante(C), !, coef_lineal(U, K0), valor(C, CV), K is CV * K0.
coef_lineal(U / C, K) :- constante(C), !, coef_lineal(U, K0), valor(C, CV), CV =\= 0, K is (1.0 * K0) / CV.
coef_lineal(U + C, K) :- constante(C), !, coef_lineal(U, K).
coef_lineal(C + U, K) :- constante(C), !, coef_lineal(U, K).
coef_lineal(U - C, K) :- constante(C), !, coef_lineal(U, K).
coef_lineal(C - U, K) :- constante(C), !, coef_lineal(U, K0), K is -K0.

% constante(+E): E no depende de x y se puede evaluar a un numero.
constante(E) :- \+ contiene_x(E), valor(E, _).

% valor(+C, -V): valor numerico de una expresion constante.
valor(C, V) :- evaluar(C, 0, V), finito(V).

contiene_x(x) :- !.
contiene_x(E) :- compound(E), E =.. [_ | Args], alguno_contiene_x(Args).

alguno_contiene_x([A | _]) :- contiene_x(A), !.
alguno_contiene_x([_ | As]) :- alguno_contiene_x(As).

% Constructores que evitan resultados como 1*x, x/1 o x + -(y).
por(C, G, G) :- valor(C, V), V =:= 1, !.
por(C, G, F) :- valor(C, V), V =:= -1, !, negar(G, F).
por(C, G, C * G).

entre(G, K, G) :- K =:= 1, !.
entre(G, K, F) :- K =:= -1, !, negar(G, F).
entre(G, K, G / K).

negar(-(G), G) :- !.
negar(G, -(G)).

sumar(F, -(G), F - G) :- !.
sumar(F, G, F + G).

restar(F, -(G), F + G) :- !.
restar(F, G, F - G).

% normalizar(+E, -E2): acepta e, pi, e^u y log(u) en lo que escribe el usuario.
normalizar(e, exp(1)) :- !.
normalizar(pi, P) :- !, P is 4 * atan(1).
normalizar(E ^ U, exp(U1)) :- E == e, !, normalizar(U, U1).
normalizar(log(U), ln(U1)) :- !, normalizar(U, U1).
normalizar(E, E2) :-
    compound(E), !,
    E =.. [Op | Args],
    normalizar_lista(Args, Args2),
    E2 =.. [Op | Args2].
normalizar(E, E).

normalizar_lista([], []).
normalizar_lista([A | As], [A2 | As2]) :- normalizar(A, A2), normalizar_lista(As, As2).

% ---------------------------------------------------------------------
% continua(+E, +A, +B): E esta definida en [a, b] y no tiene asintotas.
% Se muestrea E en 401 puntos y, ademas, cada expresion que puede anularse
% (denominadores, bases con exponente negativo y cos(L) de tan(L)) no debe
% valer 0 ni cambiar de signo entre las muestras.
% ---------------------------------------------------------------------
continua(E, A, B) :-
    singulares(E, [], Ds),
    Paso is (B - A) / 400.0,
    signos(Ds, A, S0),
    muestrear(0, 400, A, Paso, E, Ds, S0).

muestrear(I, N, _, _, _, _, _) :- I > N, !.
muestrear(I, N, A, Paso, E, Ds, S0) :-
    X is A + I * Paso,
    evaluar(E, X, V),
    finito(V),
    signos(Ds, X, S),
    S == S0,
    I1 is I + 1,
    muestrear(I1, N, A, Paso, E, Ds, S0).

signos([], _, []).
signos([D | Ds], X, [S | Ss]) :-
    evaluar(D, X, V),
    finito(V),
    Eps is 1.0 / 1000000.0 / 1000000.0,
    AV is abs(V),
    AV > Eps,
    (   V > 0 -> S = 1 ; S = -1 ),
    signos(Ds, X, Ss).

singulares(E, Ac, Ac) :- \+ contiene_x(E), !.
singulares(x, Ac, Ac) :- !.
singulares(U / D, Ac, Ds) :- !, singulares(U, [D | Ac], Ac1), singulares(D, Ac1, Ds).
singulares(L ^ N, Ac, Ds) :- constante(N), valor(N, NV), NV < 0, !, singulares(L, [L | Ac], Ds).
singulares(tan(L), Ac, Ds) :- !, singulares(L, [cos(L) | Ac], Ds).
singulares(E, Ac, Ds) :- E =.. [_ | Args], singulares_lista(Args, Ac, Ds).

singulares_lista([], Ac, Ac).
singulares_lista([A | As], Ac, Ds) :- singulares(A, Ac, Ac1), singulares_lista(As, Ac1, Ds).

% Alison Michelle Maltez Valencia 00100121
% e) integral de u dx
regla(e, Params, A, B, R) :-
    param(u, Params, U0),
    normalizar(U0, U),
    integral_u(U, A, B, R).

% Alison Michelle Maltez Valencia 00100121
% h) integral de (u - v) dx = integral de u dx - integral de v dx
regla(h, Params, A, B, R) :-
    param(u, Params, U0),
    param(v, Params, V0),
    normalizar(U0, U),
    normalizar(V0, V),
    integral_u(U - V, A, B, R).

% integral_u(+E, +A, +B, -R): integra E con integrar/2 y revisa continuidad.
integral_u(E, _, _, error('No hay una regla para integrar esa funcion. Se admiten sumas, restas, constante*funcion, x^n, 1/x, sin, cos, tan, exp, a^x, ln, sqrt, sinh, cosh y tanh de x o de (k*x + c).')) :-
    \+ integrar(E, _), !.
integral_u(E, A, B, error('La funcion no es continua en todo [a, b] (division entre cero, asintota o fuera de dominio): la integral es impropia.')) :-
    \+ continua(E, A, B), !.
integral_u(E, A, B, R) :-
    integrar(E, F),
    con_area(F, A, B, R).
