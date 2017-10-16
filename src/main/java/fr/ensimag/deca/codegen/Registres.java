package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

// Classe de gestion des registres

public class Registres {
	private int RMax = 15;
	private int regMax = 1;
	private int nbTemp = 0;
	private int maxTemp = 0;
	private int maxParamCalled = 0;
	private boolean busyRegs[] = new boolean[RMax+1];
	
	public Registres(int nb){
		this.RMax = nb - 1;
	}
	
	public int getRegMax(){
		return regMax;
	}
	
	public int getRMax(){
		return RMax;
	}

	public GPRegister reg_dispo(){
		// On cherche un registre disponible
		for (int i = 2; i <= RMax; i++ ){
			if (!busyRegs[i]){
				busyRegs[i] = true;
				if (i > regMax){
					regMax = i;
				}
				return Register.getR(i);
			}
		}
		throw new UnsupportedOperationException("Plus de registres disponibles : échec PUSH/POP");
	}

	public void liberer(GPRegister r){
		busyRegs[r.getNumber()] = false;
	}
	
	public void paramsCalledMethod(int nbParam){
		if (nbParam > maxParamCalled){
			maxParamCalled = nbParam;
		}
	}
	
	public int getMaxParamCalled(){
		return this.maxParamCalled;
	}
	
	public void incrNbTemp(){
		nbTemp++;
		if (nbTemp > maxTemp){
			maxTemp = nbTemp;
		}
	}
	
	public void decrNbTemp(){
		nbTemp--;
	}
	
	public int getMaxTemp(){
		return maxTemp;
	}

}
