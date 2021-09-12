package com.smsSender.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.smsSender.server.entities.ERole;
import com.smsSender.server.entities.Role;
import com.smsSender.server.repositories.RoleRepository;

@Component
@ConditionalOnProperty(name = "app.init-db", havingValue = "true")
public class DbInitializer implements CommandLineRunner {
	
	@Autowired
	private RoleRepository roleRepo;
	
	

	public DbInitializer(RoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}



	@Override
	public void run(String... args) throws Exception {
		

		Role role1 = new Role( 1L, ERole.ROLE_ADMIN);
		Role role2 = new Role(2L, ERole.ROLE_MODERATOR);
		Role role3 = new Role(3L, ERole.ROLE_USER);

		this.roleRepo.save(role1); 
		this.roleRepo.save(role2); 
		this.roleRepo.save(role3); 	
		}
	
		
	}

