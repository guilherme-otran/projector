/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projector.projector.music_importing;

import br.com.projector.projector.models.Music;

/**
 *
 * @author guilherme
 */
public interface ImportCallback {

    void onImportSuccess(Music music);

    void onImportError();
}