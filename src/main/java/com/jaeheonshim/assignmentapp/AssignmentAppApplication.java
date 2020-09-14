package com.jaeheonshim.assignmentapp;

import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssignmentAppApplication implements CommandLineRunner {
	@Autowired
	private UserRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(AssignmentAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repository.deleteAll();

		repository.save(new User("Jaeheon Shim", "jaeheon287@gmail.com", "{noop}jaeheonshim"));
	}
}
