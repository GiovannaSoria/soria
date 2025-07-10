package com.examen.soria.repository;

import com.examen.soria.model.TurnoCaja;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoCajaRepository extends MongoRepository<TurnoCaja, String> {
    
    Optional<TurnoCaja> findByCodigoTurno(String codigoTurno);
    
    List<TurnoCaja> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    Optional<TurnoCaja> findByCodigoCajaAndCodigoCajeroAndEstado(String codigoCaja, String codigoCajero, String estado);
    
    List<TurnoCaja> findByEstado(String estado);
    
    boolean existsByCodigoCajaAndCodigoCajeroAndEstado(String codigoCaja, String codigoCajero, String estado);
} 