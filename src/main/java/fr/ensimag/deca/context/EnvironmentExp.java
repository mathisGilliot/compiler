package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Iterator;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl27
 * @date 01/01/2017
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).
	
    EnvironmentExp parentEnvironment;
    protected HashMap<Symbol,ExpDefinition> environnement;
    protected SymbolTable tab;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.environnement = new HashMap<Symbol,ExpDefinition>();
        this.tab = new SymbolTable();
    }

    public SymbolTable getTab() {
		return tab;
	}

	public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        //throw new UnsupportedOperationException("not yet implemented");
        if (this.environnement.containsKey(key)) {        	
        	return this.environnement.get(key);
        }
        else {
        	return null;
        }
    }
    
    //empilement de envExp1/envExp2
    public void EmpileExp(EnvironmentExp envExp2) {
    	HashMap<Symbol,ExpDefinition> envExpAux = new HashMap<Symbol,ExpDefinition>();
    	Iterator<Symbol> it = envExp2.getEnvironnement().keySet().iterator();
    	while (it.hasNext()) {
    		Symbol symb = it.next();
    		envExpAux.put(symb, envExp2.getEnvironnement().get(symb));
    	}
    	Iterator<Symbol> it2 = this.environnement.keySet().iterator();
    	while (it2.hasNext()) {
    		Symbol symb2 = it2.next();
    		envExpAux.put(symb2, this.environnement.get(symb2));
    	}
    	this.environnement = envExpAux;
    	
    }
    
    
    
    
    public HashMap<Symbol,ExpDefinition> getEnvironnement() {
    	return environnement;
    }
    

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        //throw new UnsupportedOperationException("not yet implemented");
    	if (this.environnement.containsKey(name)) {
    		throw new DoubleDefException(); 
    	}
    	else {
    		this.environnement.put(name, def);
    	}
    }
    
    
    @Override 
    public String toString() {
    	String s = "";
    	Iterator<Symbol> it = this.environnement.keySet().iterator();
    	while (it.hasNext()) {
    		Symbol symb = it.next();
    		s += symb.getName() + " --> " + this.get(symb).toString() + "\n";
    	}
    	return s;
    }

}
