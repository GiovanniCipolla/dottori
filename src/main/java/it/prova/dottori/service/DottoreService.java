package it.prova.dottori.service;

import java.util.List;

import it.prova.dottori.dto.DottoreDTO;

public interface DottoreService {
 
	public DottoreDTO inserisciNuovo(DottoreDTO dottore);

	public List<DottoreDTO> listAll();
	
	public DottoreDTO aggiorna(DottoreDTO dottore);
	
	public DottoreDTO caricaSingoloElemento(Long id);
	
	public void rimuovi(Long id);
}
