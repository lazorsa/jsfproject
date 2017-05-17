package br.com.project.exception;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import br.com.framework.hibernate.session.HibernateUtil;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapped;

	// Obtiene una instancia de FacesContext
	final FacesContext facesContext = FacesContext.getCurrentInstance();

	// Obtiene un mapa de FacesContext
	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

	// Obtiene el estado actual de la navegaci�n entre las p�ginas del JSF
	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

	// Declara al constructor que recibe una excepci�n del tipo ExceptionHandler
	// como par�metro
	CustomExceptionHandler(ExceptionHandler exception) {
		this.wrapped = exception;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	// Sobrescribir el m�todo handle que es responsable de manipular las
	// excepciones del JSF
	@Override
	public void handle() throws FacesException {

		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

		while (iterator.hasNext()) {
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			// Recupera la excepci�n del contexto
			Throwable exception = context.getException();

			// Aqu� tratamos de tratar la excepci�n
			try {

				requestMap.put("exceptionMessage", exception.getMessage());

				if (exception != null && exception.getMessage() != null
						&& exception.getMessage().indexOf("ConstraintViolationException") != -1) {

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN,
							"El registro no se puede quitar por estar asociado.", ""));

				} else if (exception != null && exception.getMessage() != null
						&& exception.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1) {

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El registro ha sido actualizado o eliminado por otro usuario. Consulte de nuevo.", ""));

				} else {
					// Avisa al usuario del error
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"El sistema se recuper� de un error inesperado.", ""));

					// Tranquiliza al usuario para que contin�e usando el sistema
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Usted puede continuar usando el sistema normalmente!", ""));

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"El error fue causado por:\n" + exception.getMessage(), ""));

					// SETA LA NAVEGACI�N PARA UNA P�GINA EST�NDAR - REDIRECIONA PARA UNA PAGINA DE ERROR.
					// Este alerta s�lo se muestra si la p�gina no redirige
					RequestContext.getCurrentInstance()
							.execute("alert('El sistema se recuper� de un error inesperado.')");

					RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Error", "El sistema se recuper� de un error inesperado.."));

					navigationHandler.handleNavigation(facesContext, null,
							"/error/error.jsf?faces-redirect=true&expired=true");
				}

				// Renderiza la p�gina de error y muestra los mensajes
				facesContext.renderResponse();
			} finally {

				SessionFactory sf = HibernateUtil.getSessionfactory();

				if (sf.getCurrentSession().getTransaction().isActive()) {
					sf.getCurrentSession().getTransaction().rollback();
				}
				// Imprimir la excepci�n en la consola
				exception.printStackTrace();

				// Quita la excepci�n de la cola
				iterator.remove();
			}
		}
		// Manipula el error
		getWrapped().handle();

	}

}
