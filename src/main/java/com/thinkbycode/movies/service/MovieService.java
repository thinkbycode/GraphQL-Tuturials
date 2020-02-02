package com.thinkbycode.movies.service;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import com.thinkbycode.movies.model.Movie;

@Service
public class MovieService {

	private ImmutableMap<Integer, Movie> movieDB = ImmutableMap.of(
				1, new Movie("LOTR", "Peter Jackson"),
				2, new Movie("Inception", "Christopher Nolan")
			);
	
	public Movie getMovieById(int id) {
		return movieDB.get(id);
	}
}
