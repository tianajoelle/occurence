package com.example.occurence.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.occurence.dto.RequestCount;
import com.example.occurence.service.AkkaService;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
@RequestMapping("/occurence")
public class MapReduceController {

    @Autowired
    private AkkaService service;

    @PostMapping("/init")
    public String initializeActors() {
        service.create();
        return "home";
    }
    

    @GetMapping("/home")
    public String home() {
        return "home"; 
    }
    
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
    		String line;
    		while ((line = reader.readLine()) != null) {
    			service.lectureLine(line);
    		} reader.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
		return "home";
    	
    }
    
    @GetMapping("/count")
    @ResponseBody
    public String getWordCount(@RequestParam("word") String word) {
        try {
            ActorRef reducer = service.partition(word);
            Future<Object> future = Patterns.ask(reducer, new RequestCount(word), 3000);
            int count = (int) Await.result(future, Duration.create(3, TimeUnit.SECONDS));
            return "Mot '" + word + "' trouvé " + count + " fois.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors du comptage.";
        }
    }
    
}
