package com.bridgeLabz.addressBookProgram;

import com.opencsv.bean.CsvBindByName;

public class CSVStates {

	@CsvBindByName(column = "state name", required = true)
	public String stateName;

	@CsvBindByName(column = "state code", required = true)
	public String stateCode;

	@Override
	public String toString() {
		return "CSVStates [stateName=" + stateName + ", stateCode=" + stateCode + "]";
	}

}
