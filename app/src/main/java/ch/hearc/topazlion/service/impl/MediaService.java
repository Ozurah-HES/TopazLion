package ch.hearc.topazlion.service.impl;

import org.springframework.stereotype.Service;

import ch.hearc.topazlion.model.Media;
import ch.hearc.topazlion.service.MediaService_I;

@Service
public class MediaService implements MediaService_I {
    public void fetch_ANN(Media fetchOn, long annId)
    {
        // Get data from ANN
        // TODO

        // Set data to fetchOn
        fetchOn.setName("Name");
        fetchOn.setImgUrl("url");
    }
    
}
