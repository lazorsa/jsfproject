package br.com.project.util.all;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class Messagens extends FacesContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Messagens() {

	}
	
	public static void msg(String msg){
		if (facesContextValido()) {
			getFaceContext().addMessage("msg", new FacesMessage(msg));
			
		}
	}
	
	public static void responseOperation(EstatusPersistencia estatusPersistencia){
		if (estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.SUCESSO)) {
			succeso();
			
		}else if (estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.OBJETO_REFERENCIADO)) {
			msgSeverityFatal(EstatusPersistencia.OBJETO_REFERENCIADO.toString());
			
		}else {
			erroNaOperacao();
		}
	}
	
	public static void succeso(){
		msgSeverityInfor(Constante.OPERACAO_REALIZADA_COM_SUCESSO);
	}
	
	public static void erroNaOperacao(){
		if (facesContextValido()) {
			msgSeverityFatal(Constante.ERROR_NA_OPERACAO);
		}
	}

	public static FacesContext getFaceContext() {
		return FacesContext.getCurrentInstance();

	}

	private static boolean facesContextValido() {
		return getFaceContext() != null;
	}

	public static void msgSeverityWarn(String msg) {
		if (facesContextValido()) {
			getFaceContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
		}
	}
	
	public static void msgSeverityFatal(String msg) {
		if (facesContextValido()) {
			getFaceContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg));
		}
	}
	
	public static void msgSeverityError(String msg) {
		if (facesContextValido()) {
			getFaceContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public static void msgSeverityInfor(String msg) {
		if (facesContextValido()) {
			getFaceContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
		}
	}	
	

}
