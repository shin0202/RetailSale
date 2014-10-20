package com.example.retailsale.manager.addcustomer;

import org.json.JSONObject;

public interface AddCustomerListener
{
	void onResult(Boolean isSuccess, JSONObject information);
}
