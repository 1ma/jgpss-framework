/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author eZe
 * @param <T>
 */
public class CustomArrayList<T> extends ArrayList<T>{
    
    
    /**
     * Returns the total average of the Array List
     * @return 
     */
    public Float avg() {
        
        if (isEmpty()) {
            return new Float(0);
        }            
        
        float sum = 0;
        while (iterator().hasNext()) {
            
            sum += (Float) iterator().next();
        }        
        return sum/size();        
    }  
    
    /**
     * Returns the last item of the list. Null if the list is empty
     * @return 
     */
    public T lastItem() {
        
        if (isEmpty()) {
            return null;
        }
        
        return get(size() - 1);
        
    }
}
