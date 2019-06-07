package it.polito.tdp.ufo.model;

import java.time.Year;

public class TestModel {
	public static void main(String[] args) {

		TestModel test = new TestModel();
		test.run();

	}
	
	private void run() {
		Model model = new Model();
		model.creaGrafo(Year.of(2010));
	}
}
