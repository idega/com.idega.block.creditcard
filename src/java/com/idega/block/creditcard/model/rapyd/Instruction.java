package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;
import java.util.ArrayList;

public class Instruction implements Serializable {

	private static final long serialVersionUID = -1967729966111712626L;

	private String name;

    private ArrayList<Step> steps;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Step> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}

}