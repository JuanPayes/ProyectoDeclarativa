% evaluar.pl - Evaluacion numerica de expresiones en x y calculo de F(b) - F(a)
%
% evaluar(+Expr, +X0, -Valor): valor de Expr cuando x = X0.
% FALLA (no lanza excepcion) si Expr no esta definida en X0: division entre
% cero, ln de un numero no positivo, raiz de un negativo.
% Las funciones hiperbolicas se definen con exp/1 para no depender de que
% el motor las traiga. La division multiplica por 1.0 para que siempre sea real.

evaluar(x, X0, X0) :- !.
evaluar(N, _, N) :- number(N), !.
evaluar(-(A), X0, V) :- !, evaluar(A, X0, VA), V is -VA.
evaluar(A+B, X0, V) :- !, evaluar(A, X0, VA), evaluar(B, X0, VB), V is VA + VB.
evaluar(A-B, X0, V) :- !, evaluar(A, X0, VA), evaluar(B, X0, VB), V is VA - VB.
evaluar(A*B, X0, V) :- !, evaluar(A, X0, VA), evaluar(B, X0, VB), V is VA * VB.
evaluar(A/B, X0, V) :- !,
    evaluar(A, X0, VA), evaluar(B, X0, VB),
    VB =\= 0,
    V is (1.0 * VA) / VB.
evaluar(A^B, X0, V) :- !, evaluar(A, X0, VA), evaluar(B, X0, VB), V is VA ** VB.
evaluar(sin(A), X0, V) :- !, evaluar(A, X0, VA), V is sin(VA).
evaluar(cos(A), X0, V) :- !, evaluar(A, X0, VA), V is cos(VA).
evaluar(tan(A), X0, V) :- !, evaluar(A, X0, VA), V is tan(VA).
evaluar(exp(A), X0, V) :- !, evaluar(A, X0, VA), V is exp(VA).
evaluar(abs(A), X0, V) :- !, evaluar(A, X0, VA), V is abs(VA).
evaluar(sqrt(A), X0, V) :- !, evaluar(A, X0, VA), VA >= 0, V is sqrt(VA).
evaluar(ln(A), X0, V) :- !, evaluar(A, X0, VA), VA > 0, V is log(VA).
evaluar(sinh(A), X0, V) :- !, evaluar(A, X0, VA), V is (exp(VA) - exp(-VA)) / 2.
evaluar(cosh(A), X0, V) :- !, evaluar(A, X0, VA), V is (exp(VA) + exp(-VA)) / 2.
evaluar(tanh(A), X0, V) :- !,
    evaluar(A, X0, VA), V is (exp(VA) - exp(-VA)) / (exp(VA) + exp(-VA)).

% finito(+X): X no es infinito ni NaN (la comparacion es falsa para ambos).
% Los reales de Java/Prolog solo llegan a ~1.8e308; exp(710) ya se desborda.
finito(X) :- Max is 10.0 ** 308, AX is abs(X), AX < Max.

% area(+F, +A, +B, -Area): Area = F(B) - F(A). Falla si F no esta definida
% en alguno de los extremos o si algun valor se desborda.
area(F, A, B, Area) :-
    evaluar(F, B, FB), finito(FB),
    evaluar(F, A, FA), finito(FA),
    Area is FB - FA,
    finito(Area).

% con_area(+F, +A, +B, -Resultado): ok(F, Area) o error(Mensaje).
con_area(F, A, B, ok(F, Area)) :-
    area(F, A, B, Area),
    !.
con_area(_, _, _, error('El resultado no esta definido o es demasiado grande para representarse en [a, b].')).