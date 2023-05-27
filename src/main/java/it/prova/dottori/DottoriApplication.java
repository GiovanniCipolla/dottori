package it.prova.dottori;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.dottori.model.Dottore;
import it.prova.dottori.service.DottoreService;

@SpringBootApplication
public class DottoriApplication implements CommandLineRunner  {
	
	@Autowired
	private DottoreService dottoreService;
	
	public static void main(String[] args) {
		SpringApplication.run(DottoriApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Dottore doc1 = Dottore.builder().nome("LeBron").cognome("James").codiceDottore("lb6").build();
		Dottore doc2 = Dottore.builder().nome("Steph").cognome("Curry").codiceDottore("s30").build();
		dottoreService.inserisciNuovo(doc1);
		dottoreService.inserisciNuovo(doc2);
	}

}
