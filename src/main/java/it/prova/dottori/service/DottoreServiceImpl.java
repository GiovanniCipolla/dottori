package it.prova.dottori.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.dottori.dto.DottoreDTO;
import it.prova.dottori.model.Dottore;
import it.prova.dottori.repository.DottoreRepository;

@Service
@Transactional
public class DottoreServiceImpl implements DottoreService {

	@Autowired
	private DottoreRepository repository;
	
	@Override
	@Transactional
	public DottoreDTO inserisciNuovo(DottoreDTO dottore) {
		repository.save(dottore.buildDottoreModel());
		return dottore;
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<DottoreDTO> listAll() {
		return DottoreDTO.createDottoreDTOListFromModelList((List<Dottore>) repository.findAll());
	}

	@Override
	@Transactional
	public DottoreDTO aggiorna(DottoreDTO dottore) {
		repository.save(dottore.buildDottoreModel());
		return dottore;
	}

	@Override
	@Transactional(readOnly = true)
	public DottoreDTO caricaSingoloElemento(Long id) {
		return DottoreDTO.buildDottoreDTOFromModel(repository.findById(id).orElse(null));
	}

	@Override
	@Transactional
	public void rimuovi(Long id) {
		repository.deleteById(id);
		
	}

}
