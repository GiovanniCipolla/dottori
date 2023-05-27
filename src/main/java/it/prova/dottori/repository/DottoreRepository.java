package it.prova.dottori.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.prova.dottori.model.Dottore;

public interface DottoreRepository extends PagingAndSortingRepository<Dottore, Long>, JpaSpecificationExecutor<Dottore>  {

}
