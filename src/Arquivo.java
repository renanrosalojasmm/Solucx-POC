
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author renan.rosa
 */
public class Arquivo {
    
    public String lerArquivoSQL() {
        
        String conteudo = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("7dias.sql"));
            conteudo = new String(encoded, "UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conteudo;
    }
}
