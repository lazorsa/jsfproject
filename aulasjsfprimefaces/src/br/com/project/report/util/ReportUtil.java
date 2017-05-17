package br.com.project.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@SuppressWarnings("deprecation")
@Component
public class ReportUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String UNDERLINE = "_";
	
	private static final String FOLDER_RELATORIOS = "/report"; 
	
	private static final String SUBREPORT_DIR ="SUBREPORT_DIR";
	
	private static final String EXTENSION_ODS = "ods";
	
	private static final String EXTENSION_XLS = "xls";
	
	private static final String EXTENSION_HTML = "html";
	
	private static final String EXTENSION_PDF = "pdf";
	
	private String SEPARATOR = File.separator;
	
	private static final int RELATORIO_PDF = 1;
	
	private static final int RELATORIO_EXCEL = 2;
	
	private static final int RELATORIO_HTML = 3;
	
	private static final int RELATORIO_PLANILHA_OPEN_OFFICE = 4;

	private static final String PONTO = ".";
	
	private StreamedContent arquivoRetorno = null;
	
	private String caminhoArquivoRelatorio = null;
	@SuppressWarnings("rawtypes")
	private JRExporter tipoArquivoExportado = null;
	
	private String  extansaoArquivoExportado = "";
	
	private String caminhoSubreport_dir = "";
	
	private File arquivoGerado = null;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public StreamedContent geraRelatorio(List<?> listDataBeanCollectionReport, HashMap parametrosRelatorio, String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio) throws Exception {
		
		/**
		 * Crea una lista de collectionData de beans
		 */
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(listDataBeanCollectionReport);
		
		
		/**
		 * Forma un camino físico para contener una relación de compilados Jasper
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		
		context.responseComplete();
		
		ServletContext sContext = (ServletContext) context.getExternalContext().getContext();
		
		String caminhoRelatorio = sContext.getRealPath(FOLDER_RELATORIOS);
		
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");
		
		if (caminhoRelatorio == null || (caminhoRelatorio != null && caminhoRelatorio.isEmpty()) || !file.exists()) {
			
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();
			
			SEPARATOR = "";
			
		}
		
		/**
		 * Ruta para imágenes
		 */
		
		parametrosRelatorio.put("REPORT_PARAMETER_IMG", caminhoRelatorio);
		
		/**
		 * ruta completa o relativa indicada
		 */
		
		String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper";
		
		/**
		 * 
		 */
		
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);
		
		/**
		 * 
		 */
		
		caminhoSubreport_dir = caminhoRelatorio + SEPARATOR;
		
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_dir);
		
		/**
		 * 
		 */
		
		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, jrBeanCollectionDataSource);
		
		switch (tipoRelatorio) {
		case RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();
			extansaoArquivoExportado = EXTENSION_PDF;
			break;
			
		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();
			extansaoArquivoExportado = EXTENSION_HTML;
			break;
			
		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();
			extansaoArquivoExportado = EXTENSION_XLS;
			break;
			
		case RELATORIO_PLANILHA_OPEN_OFFICE:
			tipoArquivoExportado = new JROdtExporter();
			extansaoArquivoExportado = EXTENSION_ODS;
			break;

		default:
			tipoArquivoExportado = new JRPdfExporter();
			extansaoArquivoExportado = EXTENSION_PDF;
			break;
		}
		
		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAtualReportName();
		
		/**
		 * Ruta relativa de salida
		 */
		
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioSaida + PONTO + extansaoArquivoExportado;
		
		/**
		 * Crea nuevo archivo exportado
		 */
		
		arquivoGerado = new File(caminhoArquivoRelatorio);
		
		/**
		 * Prepara impresión
		 */
		
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
		
		/**
		 * Nombre del archivo exportado
		 */
		
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
		
		/**
		 * Ejecuta exportación
		 */
		
		tipoArquivoExportado.exportReport();
		
		/**
		 * remueve el archivo del servidor
		 */
		arquivoGerado.deleteOnExit();
		
		/**
		 * Crea inputstream para ser usado por primefaces
		 */
		
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
		
		/**
		 * Return de la aplicación 
		 */
		
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio,"application/"+extansaoArquivoExportado,nomeRelatorioSaida + PONTO + extansaoArquivoExportado);
		
		return arquivoRetorno;
		
		
	}
	
	

}
