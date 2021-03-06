package com.company.repository.contracts;

public interface Database extends Requester, FilteredTaker, OrderedTaker {

    void loadData(String fileName);

    void unloadData();
}