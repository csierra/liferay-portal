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

import com.liferay.portal.kernel.util.Function;

/**
*
*/
public interface SingleRenderer<Q> {
    /**
    **/
    public <R> Result<R> map(Function<Q, R> function);
}