package com.examen.soria.repository;

import com.examen.soria.model.TransaccionTurno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionTurnoRepository extends MongoRepository<TransaccionTurno, String> {
    
    List<TransaccionTurno> findByCodigoTurno(String codigoTurno);
    
    List<TransaccionTurno> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    List<TransaccionTurno> findByCodigoTurnoAndTipoTransaccion(String codigoTurno, String tipoTransaccion);
    
    List<TransaccionTurno> findByTipoTransaccion(String tipoTransaccion);
} 