bundleId(X, B) :- bundle(X, _, B).
bundleName(X, B) :- bundle(_, X, B).

componentName(X, C) :- component(X, _, _, _, _, _, C).
componentImplementation(X, C) :- component(_, X, _, _, _, _,C).
componentInterfaces(X, C) :- component(_, _, X, _, _, _, C).
componentReferences(X, C) :- component(_, _, _, R, _, _, C), member(X, R).
componentConfiguration(X, C) :- component(_, _, _, _, CC, _, C), member(X, CC).
componentBundle(X, C) :- component(_, _, _, _, _, X, C).

configurationId(X, configuration(X, _, _, _, C)) :- true.
configurationState(X, configuration(_, X, _, _, C)) :- true.
configurationSatisfied(X, configuration(_, _, X, _, C)) :- true.
configurationUnsatisfied(X, configuration(_, _, _, X, C)) :- true.