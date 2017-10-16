package fr.ensimag.deca.codegen;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.ima.pseudocode.Label;

public class LabelEnd {
	
	Map<String, Label> listString;
	Map<Integer, Label> listInt;
	Map<Label, Integer> listVar;
	
	public LabelEnd(){
		listString = new HashMap<String, Label>();
		listInt = new HashMap<Integer, Label>();
		listVar = new HashMap<Label, Integer>();
	}

	public Map<String, Label> getListString() {
		return listString;
	}

	public Map<Integer, Label> getListInt() {
		return listInt;
	}
	
	public Map<Label, Integer> getListVar() {
		return listVar;
	}
	
	public void addListString(String value){
		Label str = new Label(value, true);
		listString.put(value, str);	
	}
	
	
	public void addListInt(Integer value){
		Label v = new Label("I_" +value, true);
		listInt.put(value, v);
	}
	
	public void addListVar(String value){
		Label v = new Label(value, true);
		listVar.put(v, 0);
	}
	
	

}
