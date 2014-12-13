package com.com.android.eboerse.database;

/**
 * Pojo zum interagieren zwischen DB und Anwendung
 * @author Tok
 *
 */
public class Favorit {

	String Kurs_Name;
	String Kurs_Symbol;
	String Flag_Favorit;
	String untereGrenze;
	String obereGrenze;
	
	public Favorit(){
		
	}
	public Favorit(String Name , String Symbol , String Flag, String obereGrenze, String untereGrenze){
		this.Kurs_Name = Name;
		this.Kurs_Symbol = Symbol;
		this.Flag_Favorit = Flag;
		this.untereGrenze = untereGrenze;
		this.obereGrenze = obereGrenze;
	}
	public String getKurs_Name() {
		return Kurs_Name;
	}


	public void setKurs_Name(String kurs_Name) {
		Kurs_Name = kurs_Name;
	}


	public String getKurs_Symbol() {
		return Kurs_Symbol;
	}


	public void setKurs_Symbol(String kurs_Symbol) {
		Kurs_Symbol = kurs_Symbol;
	}


	public String getFlag_Favorit() {
		return Flag_Favorit;
	}


	public void setFlag_Favorit(String flag_Favorit) {
		Flag_Favorit = flag_Favorit;
	}
	
	public String getUntereGrenze() {
		return untereGrenze;
	}
	
	public void setUntereGrenze(String untereGrenze) {
		this.untereGrenze = untereGrenze;
	}
	
	public String getObereGrenze() {
		return obereGrenze;
	}
	
	public void setObereGrenze(String obereGrenze) {
		this.obereGrenze = obereGrenze;
	}
}
