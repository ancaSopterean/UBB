package com.example.lab8.repo.paging;

public class Page<E> {
    private Iterable<E> elemsOnPage;
    public int totalNoOfElems;

    public Page(Iterable<E> elemsOnPage, int totalNoOfElems){
        this.elemsOnPage = elemsOnPage;
        this.totalNoOfElems=totalNoOfElems;
    }

    public Iterable<E> getElemsOnPage(){
        return elemsOnPage;
    }
    public int getTotalNoOfElems(){
        return totalNoOfElems;
    }
}
