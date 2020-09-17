package com.jaeheonshim.assignmentapp;

import com.jaeheonshim.assignmentapp.domain.Assignment;
import com.jaeheonshim.assignmentapp.domain.AssignmentClass;
import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.repository.AssignmentRepository;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class AssignmentAppApplication implements CommandLineRunner {
	@Autowired
	private UserRepository repository;

	@Autowired
	private AssignmentRepository assignmentRepository;

	public static void main(String[] args) {
		SpringApplication.run(AssignmentAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repository.deleteAll();
		assignmentRepository.deleteAll();

		User user = new User("Jaeheon Shim", "jaeheon287@gmail.com", "$2y$12$rnCEEI.MMZBAYy0tuUSJBubGCH4HKpyG968O8yAhT97XCYfTOBNSK");
		user.getRoles().add("ADMIN");
		user.setAccountEnabled(true);
		AssignmentClass assignmentClass = new AssignmentClass(0, "Test Class", "John Doe", "#FF0000");
		user.getAssignmentClasses().add(assignmentClass);
		repository.save(user);

		for(int i = 0; i < 5; i++) {
			LocalDate dueDate;
			if(Math.random() > 0.5) {
				dueDate = LocalDate.now();
			} else {
				dueDate = LocalDate.now().plusDays(3);
			}
			assignmentRepository.save(new Assignment(user.getId(), "Test Assignment #" + i, "Testing assignment for AssignmentApp", assignmentClass.getId(), dueDate.toEpochDay(), dueDate.toEpochDay()));
		}
	}
}