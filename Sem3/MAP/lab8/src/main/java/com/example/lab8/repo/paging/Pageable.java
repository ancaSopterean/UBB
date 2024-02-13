package com.example.lab8.repo.paging;

public class Pageable {
    private int pageNo;
    private int pageSize;

    public Pageable(int pageNo, int pageSize){
        this.pageNo =pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo(){return pageNo;}
    public int getPageSize(){return pageSize;}
}
