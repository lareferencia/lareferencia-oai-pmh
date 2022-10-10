package org.lareferencia.xoai.model;

import com.lyncode.xoai.dataprovider.core.XOAIContext;
import java.util.Comparator;

/**
 *
 * @author jonatan
 */
public class XOAIContextComparator implements Comparator<XOAIContext> {

    /**
     * Compara los objetoc de acuerdo a su nombre en orden alfabetico
     * @param o1 Primer objeto a comparar
     * @param o2 Segundo objeto a comparar
     * @return Devuelve el objeto que tiene prioridad por orden alfabetico
     */
    @Override
    public int compare(XOAIContext o1, XOAIContext o2) {
        return o1.getName().compareTo(o2.getName());
    }
    
}
