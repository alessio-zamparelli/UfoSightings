package it.polito.tdp.ufo.model;

import java.time.Year;

public class AnnoCount implements Comparable<AnnoCount> {

	private Year year;
	private Integer count;

	public AnnoCount(Year year, int count) {
		this.year = year;
		this.count = count;
	}

	public Year getYear() {
		return year;
	}

	public Integer getCount() {
		return count;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return String.format("%d (%d)", year.getValue(), count);
	}

	@Override
	public int compareTo(AnnoCount other) {
		return this.year.compareTo(other.year);
	}

}
