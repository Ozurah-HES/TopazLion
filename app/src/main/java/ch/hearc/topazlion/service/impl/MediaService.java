package ch.hearc.topazlion.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import ch.hearc.topazlion.model.AnnData;
import ch.hearc.topazlion.service.MediaService_I;

@Service
public class MediaService implements MediaService_I {

    static final String ANN_URL = "https://cdn.animenewsnetwork.com/encyclopedia/api.xml";
    static final String ANN_PARAM = "title=";
    
    private String getANNUrl(long annId) {
        return ANN_URL + "?" + ANN_PARAM + annId;
    }

    public AnnData getAnnData(long annId) throws JsonMappingException, JsonProcessingException
    {
        // Get data from ANN
        String annUrl = getANNUrl(annId);
        RestTemplate restTemplate = new RestTemplate();
        String annData = restTemplate.getForObject(annUrl, String.class);
        
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(annData, AnnData.class);
    }
    
}
