package com.auth.dbOperation;


import com.auth.model.UserModel;


public interface DbOperationService {

	public UserModel getUserObj(String email);

}
