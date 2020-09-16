package com.jaeheonshim.assignmentapp;

import com.jaeheonshim.assignmentapp.domain.Assignment;
import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.repository.AssignmentRepository;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

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

		User user = new User("Jaeheon Shim", "jaeheon287@gmail.com", "$2y$12$rnCEEI.MMZBAYy0tuUSJBubGCH4HKpyG968O8yAhT97XCYfTOBNSK");
		user.getRoles().add("ADMIN");
		user.setAccountEnabled(true);

		repository.save(user);

		assignmentRepository.save(new Assignment(user.getId(), "Test Assignment", "Testing assignment for AssignmentApp", LocalDate.now().toEpochDay(), LocalDate.now().plusWeeks(1).toEpochDay()));
	}
}
