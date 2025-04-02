package com.voteU.election.java.services;

import com.voteU.election.java.model.Party;

public interface Readable<T> {

    T[] getAll();
}
