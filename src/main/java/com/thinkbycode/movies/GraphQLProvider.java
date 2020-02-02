package com.thinkbycode.movies;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.thinkbycode.movies.model.Actor;
import com.thinkbycode.movies.model.Stats;
import com.thinkbycode.movies.service.MovieService;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * 
 * @author thinkbycode
 *
 */
@Component
public class GraphQLProvider {

    private GraphQL graphQL;
    @Autowired
    private MovieService movieService;

    @Bean
    public GraphQL graphQL() { 
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
		String sdl = Resources.toString(Resources.getResource("movie-schema.graphqls"), Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
    	TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring); 
    }
    
    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("stats", (env) -> {
                        	Stats stats = new Stats();
                        	stats.setMovies(5);
                        	stats.setDocumentaries(3);
                        	stats.setSeries(2);
                        	
                        	return stats;
                        })
                        .dataFetcher("topActorsOfWeek", (env) -> {
                        	Actor a = new Actor();
                        	a.setName("Mr. X");
                        	a.setAge(35);
                        	a.setRank(1);
                        	
                        	return a;
                        })
                        .dataFetcher("movieById", (env) -> {
                        	return movieService.getMovieById(env.getArgument("id"));
                        }))
                .build();
    }
}
