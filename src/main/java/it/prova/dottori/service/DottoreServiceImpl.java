package it.prova.dottori.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.dottori.model.Dottore;
import it.prova.dottori.repository.DottoreRepository;

@Service
@Transactional(readOnly = true)
public class DottoreServiceImpl implements DottoreService {


	@Autowired
	private DottoreRepository repository;

	@Override
	public List<Dottore> listAllElements() {
		return (List<Dottore>) repository.findAll();
	}

	@Override
	public Dottore caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Dottore aggiorna(Dottore dottoreInstance) {
		Dottore dottore = repository.findById(dottoreInstance.getId()).orElse(null);
		
		if(dottore.isInServizio() == true)
			dottoreInstance.setInServizio(true);
		else
			dottoreInstance.setInServizio(false);
		if(dottore.isInVisita() == true)
			dottoreInstance.setInServizio(true);
		else
			dottoreInstance.setInServizio(false);
		
		return repository.save(dottoreInstance);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Dottore dottoreInstance) {
		
		dottoreInstance.setInServizio(true);
		dottoreInstance.setInVisita(false);
		
		repository.save(dottoreInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		
		Dottore dottoreInstance = repository.findById(idToRemove).orElse(null);
		if(dottoreInstance.isInServizio() == true || dottoreInstance.isInVisita() == true)
			throw new RuntimeException();
		
		repository.deleteById(idToRemove);
	}

	@Override
	public Dottore findByCodFiscalePazienteAttualmenteInVisita(String codFiscalePazienteAttualmenteInVisitaInstance) {
		return repository
				.findDottoreByCodFiscalePazienteAttualmenteInVisita(codFiscalePazienteAttualmenteInVisitaInstance);
	}

	@Override
	public Dottore findByCodiceDottore(String codiceDottoreInstance) {
		return repository.caricaDottoreFromCodiceDottore(codiceDottoreInstance);

	}
	

	@Override
	public Dottore verificaDisponibilita(String cd) {
		return repository.caricaDottoreFromCodiceDottore(cd);
	}

	@Override
	@Transactional
	public Dottore impostaDottore(Dottore dottore) {
		
		
		Dottore result = repository.caricaDottoreFromCodiceDottore(dottore.getCodiceDottore());
		
		result.setCodFiscalePazienteAttualmenteInVisita(dottore.getCodFiscalePazienteAttualmenteInVisita());
		
		result.setInVisita(true);
		
		return repository.save(result);
	}

	@Override
	@Transactional
	public Dottore ricovera(Dottore dottore) {
		
		Dottore result = repository.caricaDottoreFromCodiceDottore(dottore.getCodiceDottore());
		
		if(result.getCodFiscalePazienteAttualmenteInVisita().equals(dottore.getCodFiscalePazienteAttualmenteInVisita()))
			throw new RuntimeException("Questo dottore non ha un paziente con questo CF");
		
		result.setCodFiscalePazienteAttualmenteInVisita(null);
		result.setInVisita(false);
		
		return repository.save(result);
	}

	@Override
	@Transactional
	public Dottore cambiaServizio(Long id) {
		
		Dottore dottore = repository.findById(id).orElse(null);
		
		if(dottore.isInServizio() == true)
			throw new RuntimeException("impossibile cambiare lo stato in servizio se ha dei pazienti");
		
		if(dottore.isInServizio() == true)
			dottore.setInServizio(false);
		else
			dottore.setInServizio(true);
		
		return dottore;
	}

}
