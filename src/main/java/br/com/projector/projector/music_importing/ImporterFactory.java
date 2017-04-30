/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projector.projector.music_importing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme
 */
public class ImporterFactory {

    public static ImporterFactory getFactory() {
        return new ImporterFactory();
    }

    private ImporterFactory() {

    }

    public MusicUrlImporter getImporter(String urlSpec) {
        try {
            URL url = new URL(urlSpec);
            if (url.getHost().contains("letras.mus.br")) {
                return new LetrasMusImporter(urlSpec);
            }
            if (url.getHost().contains("vagalume.com.br")) {
                return new VagalumeImporter(urlSpec);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImporterFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
