% =====================================================================
% integrador.pl  -  Punto de entrada de la base de conocimiento
% =====================================================================
% Java llama siempre a:
%
%     resolver(Id, Params, A, B, Resultado)
%
%   Id         atomo con la letra del enunciado: a, b, c, ..., v
%   Params     lista de pares Nombre=Valor. Nombres posibles:
%                k=3        constante numerica
%                n=2        exponente entero
%                base=2     constante a de a^x, a^u
%                u=x^2+1    funcion de x (termino Prolog)
%                v=sin(x)   funcion de x (termino Prolog)
%              Solo vienen los que la integral elegida necesita.
%   A, B       limites de integracion (numeros)
%   Resultado  uno de:
%                ok(Antiderivada, Area)
%                no_implementado(Id)
%                error(Motivo)          % p. ej. fuera de dominio
%
% Cada compañero agrega clausulas de regla/5 en SU archivo (basicas.pl,
% trig.pl, hiperbolicas.pl). No hace falta tocar este archivo:
%
%     regla(Id, Params, A, B, Resultado).
%
% Resultado de una regla: ok(F, Area)  o  error('mensaje para el usuario').
% Casi siempre basta con terminar la regla llamando a:
%
%     con_area(F, A, B, Resultado)     % (esta en evaluar.pl)
%
% que calcula F(B) - F(A) y devuelve error(...) si F no esta definida
% en el intervalo o el valor es demasiado grande.
% NO se usa catch/throw: los errores se devuelven como valores.
% =====================================================================

:- dynamic(regla/5).

resolver(Id, Params, A, B, R) :-
    regla(Id, Params, A, B, R0),
    !,
    R = R0.
resolver(Id, _, _, _, no_implementado(Id)).

% param(+Nombre, +Params, -Valor): busca Nombre=Valor en la lista de parametros.
param(Nombre, [Nombre=Valor | _], Valor) :- !.
param(Nombre, [_ | Resto], Valor) :- param(Nombre, Resto, Valor).