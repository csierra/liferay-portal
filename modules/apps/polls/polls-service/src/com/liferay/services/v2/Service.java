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
public interface Service<B, Q, S extends ServiceContext<S>> {
    /**
    **/
    public SingleProducer<Q, S> create(ModelAction<B> consumer) ;/**
    **/
    public SingleProducer<Q, S> with(String id) ;/**
    **/
    public MultipleProducer<Q, S> find(Filter<S> filter) ;/**
    **/
    public MultipleProducer<Q, S> all() ;
}