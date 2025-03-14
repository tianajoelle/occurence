package com.example.occurence.service;

import org.springframework.stereotype.Service;

import com.example.occurence.actor.MapActor;
import com.example.occurence.actor.ReduceActor;
import com.example.occurence.dto.Message;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@Service
public class AkkaService {
	ActorSystem system = ActorSystem.create("MapReduceSystem");
	ActorRef[] mappers;
    ActorRef[] reducers;

	    public void create() {
	    	// Création de 2 reducers
	        reducers = new ActorRef[2];
	        for (int i = 0; i < reducers.length; i++) {
	            reducers[i] = system.actorOf(Props.create(ReduceActor.class), "reducer" + i);
	            System.out.println("Reducer " + i + " créé.");
	        }

	        // Création de 3 mappers
	        mappers = new ActorRef[3];
	        for (int i = 0; i < mappers.length; i++) {
	            mappers[i] = system.actorOf(MapActor.props(reducers), "mapper" + i);
	            System.out.println("Mapper " + i + " créé.");
	        }

	        // Envoi d'un message de test à l'un des acteurs mapper
	        mappers[0].tell(new Message("Hello"), ActorRef.noSender());
	    }
}
