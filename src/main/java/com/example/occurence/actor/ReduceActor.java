package com.example.occurence.actor;


import java.util.HashMap;
import java.util.Map;

import com.example.occurence.dto.Message;
import com.example.occurence.dto.RequestCount;

import akka.actor.UntypedActor;

public class ReduceActor extends UntypedActor{
	private Map<String, Integer> wordCounts = new HashMap<>();
	
	public ReduceActor() {
		this.wordCounts = new HashMap<>();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Message msg) { 	
            String cleanWord = msg.word().toLowerCase();
            wordCounts.put(cleanWord, wordCounts.getOrDefault(cleanWord, 0) + 1);
            System.out.println("Reducer " + getSelf().path().name() + " - Compte de '" + cleanWord + "' : " + wordCounts.get(cleanWord));
        
        } else if (message instanceof RequestCount request) {
        	String cleanRequest = request.word().toLowerCase();
            int count = wordCounts.getOrDefault(cleanRequest, 0);
            System.out.println("Reducer " + getSelf().path().name() + " - Occurrences de '" + cleanRequest + "' : " + count);
            getSender().tell(count, getSelf());  
        
        } else {
            unhandled(message);
        }
    }
		
}
	
