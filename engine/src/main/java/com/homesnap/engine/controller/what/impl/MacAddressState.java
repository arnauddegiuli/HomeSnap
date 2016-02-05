package com.homesnap.engine.controller.what.impl;


import com.homesnap.engine.controller.what.State;

public class MacAddressState implements State<Byte[]> {

	private Byte[] macAdress = new Byte[] {0, 0, 0, 0, 0, 0};
	
	public MacAddressState() {
	}

	@Override
	public Byte[] getValue() {
		return macAdress;
	}

	@Override
	public void setValue(Byte[] address) {
		if (address == null || address.length != 6) {
			throw new IncorrectMacAddressException();
		} else {
			macAdress = address;
		}
	}

	@Override
	public void fromString(String value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return "".concat(String.valueOf(macAdress[0]))
				.concat(".").concat(String.valueOf(macAdress[1]))
				.concat(".").concat(String.valueOf(macAdress[2]))
				.concat(".").concat(String.valueOf(macAdress[3]))
				.concat(".").concat(String.valueOf(macAdress[4]))
				.concat(".").concat(String.valueOf(macAdress[5]));
	}
}
