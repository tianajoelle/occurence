package com.example.occurence.service;

import org.springframework.stereotype.Service;
import com.example.occurence.actor.MapActor;
import com.example.occurence.actor.ReduceActor;
import com.example.occurence.dto.Message;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

@Service
public class AkkaService {
    private ActorSystem system1;  
    private ActorSystem system2;  
    private ActorRef[] mappers;
    private ActorRef[] reducers;
    private int mapperIndex = 0;

    public void create() {
        // Créer le premier système (pour les mappers)
        system1 = ActorSystem.create("System1", ConfigFactory.load("application.conf"));
        // Créer le deuxième système (pour les reducers)
        system2 = ActorSystem.create("System2", ConfigFactory.load("application2.conf"));

        // Création de 2 reducers dans le système 2
        reducers = new ActorRef[2];
        for (int i = 0; i < reducers.length; i++) {
            reducers[i] = system2.actorOf(Props.create(ReduceActor.class), "reducer" + i);
            System.out.println("Reducer " + i + " créé dans le système 2.");
        }

        // Création de 3 mappers dans le système 1
        mappers = new ActorRef[3];
        for (int i = 0; i < mappers.length; i++) {
            mappers[i] = system1.actorOf(MapActor.props(reducers), "mapper" + i);
            System.out.println("Mapper " + i + " créé dans le système 1.");
        }

        // Envoi d'un message de test à l'un des acteurs mapper
        mappers[0].tell(new Message("Hello"), ActorRef.noSender());
    }

    public void lectureLine(String line) {
        mappers[mapperIndex].tell(line, ActorRef.noSender());
        mapperIndex = (mapperIndex + 1) % mappers.length;
    }

    public ActorRef partition(String word) {
        String cleanWord = word.toLowerCase();
        int index = Math.abs(cleanWord.hashCode() % reducers.length);
        return reducers[index];
    }
}
