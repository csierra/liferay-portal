/*
*   _________________________________
*   ))                              (( 
*  ((   __    o     ___        _     ))
*   ))  ))\   _   __  ))   __  ))   (( 
*  ((  ((_/  ((  ((- ((__ ((- ((     ))
*   ))        )) ((__     ((__ ))__  (( 
*  ((                                ))
*   ))______________________________(( 
*        Diezel 2.0.0 Generated.
*
*/
package com.liferay.services.v2;


/**
*
*/
public interface SingleProducer<Q, S extends ServiceContext<S>> extends SingleRenderer<Q> {
    /**
    **/
    public SingleRenderer<Q> execute(Command<S, Q> consumer) ;

}