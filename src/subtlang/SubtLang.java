/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtlang;

/**
 *
 * @author Martijn
 */

import org.knallgrau.utils.textcat.TextCategorizer;

public class SubtLang {
    
    private String content;
    
    public SubtLang(String aryLines) {
        content = aryLines;
    }
    
    public String Language() {
        
        String category;
        
        TextCategorizer guesser = new TextCategorizer();
        category = guesser.categorize(content);
        
        return category;
        
    }
    
}
