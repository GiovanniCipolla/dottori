package it.prova.dottori.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.dottori.model.Dottore;
import it.prova.dottori.repository.DottoreRepository;

@Service
@Transactional
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
	public Dottore aggiorna(Dottore dottoreInstance) {
		return repository.save(dottoreInstance);
	}

	@Override
	public void inserisciNuovo(Dottore dottoreInstance) {
		
		dottoreInstance.setInServizio(true);
		dottoreInstance.setInVisita(false);
		repository.save(dottoreInstance);
	}

	@Override
	public void rimuovi(Long idToRemove) {
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
	public Dottore impostaDottore(Dottore dottore) {
		Dottore result = repository.caricaDottoreFromCodiceDottore(dottore.getCodiceDottore());
		result.setCodFiscalePazienteAttualmenteInVisita(dottore.getCodFiscalePazienteAttualmenteInVisita());
		return repository.save(result);
	}

	@Override
	public Dottore ricovera(Dottore dottore) {
		Dottore result = repository.caricaDottoreFromCodiceDottore(dottore.getCodiceDottore());
		result.setCodFiscalePazienteAttualmenteInVisita(null);
		result.setInVisita(false);
		return repository.save(result);
	}

}
