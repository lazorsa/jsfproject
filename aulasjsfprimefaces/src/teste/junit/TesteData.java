package teste.junit;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import br.com.project.report.util.DateUtils;

public class TesteData {

	@Test
	public void testData() {
		
		try {
			
			//System.out.println(DateUtils.getDateAtualReportName()); // 12052017
			
			assertEquals("12052017", DateUtils.getDateAtualReportName());
			
			assertEquals("'2017-05-12'", DateUtils.formatDateSql(Calendar.getInstance().getTime()));
			
			assertEquals("2017-05-12", DateUtils.formatDateSqlSimple(Calendar.getInstance().getTime()));

			
		} catch (Exception e) {
			e.printStackTrace(); 
			fail(e.getMessage());
		}
	}

}
