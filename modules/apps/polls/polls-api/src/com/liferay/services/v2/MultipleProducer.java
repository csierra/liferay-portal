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

import java.util.List;
import com.liferay.portal.kernel.util.Function;

/**
*
*/
public interface MultipleProducer<Q, S extends CommandContext<S>> {
    /**
    **/
    public <R> Result<List<R>> map(Function<Q, R> function) ;/**
    **/
    public MultipleRenderer<Q> execute(Command<S, Q> command) ;
}