package com.libring.app.datatypes;

import java.text.DecimalFormat;
import java.util.Comparator;

import com.libring.app.util.DistanceCalculatorUtil;

public class State implements Comparable<State> {
	private String state;
	private City city1;
	private City city2;
	private String formattedStrDistance;

	public State(String state, City city1, City city2) {
		this.state = state;
		this.city1 = city1;
		this.city2 = city2;
	}

	public Double getDistance() {
		Double distance =  DistanceCalculatorUtil.distance(city1.getLatitude(), 
				city1.getLongitude(), city2.getLatitude(), city2.getLongitude());
		
		this.formattedStrDistance = new DecimalFormat("#").format(distance);
		
		return distance;
	}
	
	public String getState() {
		return state;
	}
	
	public String getFormattedStrDistance() {
		return formattedStrDistance;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(state).append(" (").append(city1.toString()).append(" and ").append(city2.toString()).append(") - ").append(formattedStrDistance).append(" Km");
		return sb.toString();
	}

	@Override
	public int compareTo(State state) {
		return Comparators.ALPHABETICAL.compare(this, state);
	}
	
	public static class Comparators {

		public static Comparator<State> DIST_BIGGEST_CITIES = new Comparator<State>() {
			@Override
			public int compare(State state1, State state2) {
				return state1.getDistance().compareTo(state2.getDistance());
			}
		};
		
		public static Comparator<State> ALPHABETICAL = new Comparator<State>() {
			@Override
			public int compare(State state1, State state2) {
				return state1.getState().compareTo(state2.getState());
			}
		};
	}
}