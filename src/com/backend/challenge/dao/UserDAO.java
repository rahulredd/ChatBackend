package com.backend.challenge.dao;

import java.util.List;

public interface UserDAO {

	public void addUser(String username, String password);
	public List<String> getUsers();
	public void deleteUser(String username);
}
