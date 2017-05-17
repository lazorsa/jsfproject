package br.com.project.util.all;

public enum EstatusPersistencia {

	ERROR("Error"), SUCESSO ("Sucesso"), OBJETO_REFERENCIADO("Este objeto no puede ser cerrado por poseer referencias al mismo");

	private String name;

	private EstatusPersistencia(String s) {
		this.name = s;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
}
