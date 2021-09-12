package com.smsSender.server.services;

import java.util.List;

import com.smsSender.server.entities.Journal;

public interface IJournal {

	public Journal add(Journal journal);

	public void deleteById(long id);

	public void deleteAll();

	public List<Journal> getAll();

	public Journal getById(long id);

	public List<Journal> getByStatus(boolean status);

	

}
