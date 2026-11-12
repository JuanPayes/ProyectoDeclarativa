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
% Cada compañero agrega clausulas de regla/6 en SU archivo (basicas.pl,
% trig.pl, hiperbolicas.pl). No hace falta tocar este archivo:
%
%     regla(Id, Params, A, B, Antiderivada, Area).
% =====================================================================

:- dynamic(regla/6).

resolver(Id, Params, A, B, ok(F, Area)) :-
    regla(Id, Params, A, B, F, Area),
    !.
resolver(Id, _, _, _, no_implementado(Id)).