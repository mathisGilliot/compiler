package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.*;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

public class EnvironmentType {
	
	
    protected HashMap<Symbol,TypeDefinition> environnement;
    
    public EnvironmentType() {
        this.environnement = new HashMap<Symbol,TypeDefinition>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
        //throw new UnsupportedOperationException("not yet implemented");
        if (this.environnement.containsKey(key)) {        	
        	return this.environnement.get(key);
        }
        else {
        	return null;
        }
    }
    
    public HashMap<Symbol,TypeDefinition> getEnvironnement() {
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
    public void declare(Symbol name, TypeDefinition def) throws DoubleDefException {
        //throw new UnsupportedOperationException("not yet implemented");
    	if (this.environnement.containsKey(name)) {
    		throw new DoubleDefException(); 
    	}
    	else {
    		this.environnement.put(name, def);
    	}
    }
    
    /**
     * Vérifie si la classe class1 étend la classe class2 dans l'environnement
     * 
     * @param class1
     * @param class2
     * @return
     * 			Renvoie true si class1 étend class2
     */
    private boolean etend(ClassType class1, ClassType class2) {
    	ClassDefinition classDef = class1.getDefinition();
    	ClassDefinition superClassDef;
    	if (classDef != null) {
    		superClassDef = classDef.getSuperClass();
    	} else {
    		superClassDef = classDef;
    	}
    	while (superClassDef != null) {
    		if (superClassDef.getType().sameType(class2)) {
    			return true;
    		}
    		classDef = superClassDef;
    		superClassDef = classDef.getSuperClass();
    	}
    	return false;
    }
    
    /**
     * Vérifie si type1 est un sous-type de type2 relativement à l'environnement type
     * 
     * @param type1
     * @param type2
     * @return
     *            Renvoie true si type1 est un sous-type de type2
     */
    public boolean subtype(Type type1, Type type2) {
    	if (type1.isClass() && type2.isClass()) {
    		boolean obj = type2.getName().getName().equals("Object");
    		boolean etend = this.etend((ClassType) type1, (ClassType) type2);
    		return type1.sameType(type2) || obj || etend;
    	} else {
    		return type1.sameType(type2) || (type1.isNull() && type2.isClass());
    	}
    }
    
    
    /**
     * Vérifie si on peut affecter un objet de type1 une valeur de type2
     * 
     * @param type1
     *            Type de l'objet à affecter
     * @param type2
     *            Type de la valeur 
     * @return
     *            Renvoie true si l'affectation est possible
     */
    public boolean assignCompatible(Type type1, Type type2) {
    	return  ((type1.isFloat() && type2.isInt()) || this.subtype(type2, type1));
    }
    
    /**
     * Ajoute l'environnement prédéfini à l'environnement type
     * 
     * @param t
     */
    public void predef(SymbolTable t) {
    	TypeDefinition defInt = new TypeDefinition(new IntType(t.create("int")), Location.BUILTIN);
    	this.environnement.put(t.create("int"), defInt);
    	TypeDefinition defFloat = new TypeDefinition(new FloatType(t.create("float")), Location.BUILTIN);
    	this.environnement.put(t.create("float"), defFloat);
    	TypeDefinition defBoolean = new TypeDefinition(new BooleanType(t.create("boolean")), Location.BUILTIN);
    	this.environnement.put(t.create("boolean"), defBoolean);
    	TypeDefinition defVoid = new TypeDefinition(new VoidType(t.create("void")), Location.BUILTIN);
    	this.environnement.put(t.create("void"), defVoid);
    	
    	ClassType objectType = new ClassType(t.create("Object"), null, null);
    	ClassDefinition defObject = new ClassDefinition(objectType, Location.BUILTIN, null);
    	this.environnement.put(t.create("Object"), defObject);
    	
    	Signature equalsSign = new Signature();
    	equalsSign.add(objectType);
    	MethodDefinition equalsDef = new MethodDefinition(defBoolean.getType(), Location.BUILTIN, equalsSign, 1);
    	defObject.setNumberOfFields(0);
    	defObject.setNumberOfMethods(1);
    	try {
    		defObject.getMembers().declare(defObject.getMembers().getTab().create("equals"), equalsDef); 
    	} catch (EnvironmentExp.DoubleDefException e) {
    	}
    	defObject.setOperand(new RegisterOffset(1, Register.GB));
    	Label [] tabEtiquettesObject = defObject.getTabEtiquettes();
    	tabEtiquettesObject[1] = new Label("code.Object.equals");
    }
    
//    @Override
//    public String toString() {
//    	String s = new String();
//    	Set<Entry<Symbol, TypeDefinition>> couples = this.environnement.entrySet();
//    	Iterator<HashMap.Entry<Symbol,TypeDefinition>> itCouples = couples.iterator();
//    	  while (itCouples.hasNext()) {
//    	      Entry<Symbol, TypeDefinition> couple = itCouples.next();
//    	      s += couple.toString() + " et ";
//    	  }
//    	  return s;
//    }
    
    @Override
    public String toString() {
    	String s = new String();
    	Iterator<Symbol> i = this.environnement.keySet().iterator();
    	Symbol sym;
    	while (i.hasNext())
    	{
    		sym = i.next();
    		s = s + "name : " + sym.getName() + " --> type : " + this.get(sym).getType().getName().getName() + " ; location : " + this.get(sym).getLocation() + " \n";
    	}
    	return s;
    }
    
    
}
