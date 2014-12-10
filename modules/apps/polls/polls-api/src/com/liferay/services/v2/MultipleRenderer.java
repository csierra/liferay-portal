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
public interface MultipleRenderer<Q> {
    /**
    **/
    public <R> Result<List<R>> materialize(Function<Q, R> function) ;
}