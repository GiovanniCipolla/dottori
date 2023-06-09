package it.prova.dottori.service;

import java.util.List;

import org.springframework.data.domain.Page;

import it.prova.dottori.model.Dottore;

public interface DottoreService {
 
	List<Dottore> listAllElements();

	Dottore caricaSingoloElemento(Long id);

	Dottore aggiorna(Dottore dottoreInstance);

	void inserisciNuovo(Dottore dottoreInstance);

	void rimuovi(Long idToRemove);

	Dottore findByCodFiscalePazienteAttualmenteInVisita(String codFiscalePazienteAttualmenteInVisitaInstance);

	Dottore findByCodiceDottore(String codiceDottoreInstance);

	Dottore verificaDisponibilita(String codiceDottoreInstance);
	
	Dottore impostaDottore(Dottore dottoreInstance);
	
	Dottore ricovera(Dottore dottoreInstance);
	
	Dottore cambiaServizio(Long id);
	
	public Page<Dottore> findByExampleWithPagination(Dottore example, Integer pageNo, Integer pageSize, String sortBy);
}
