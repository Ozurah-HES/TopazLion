package ch.hearc.topazlion.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ch.hearc.topazlion.model.AnnData;

public interface MediaService_I {

    public AnnData getAnnData(long annId) throws JsonMappingException, JsonProcessingException;
    
}
