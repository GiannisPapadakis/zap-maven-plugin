package br.com.softplan.security.zap.api;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;

import org.testng.annotations.Test;

import br.com.softplan.security.zap.api.exception.ZapClientException;
import br.com.softplan.security.zap.api.model.AnalysisInfo;
import br.com.softplan.security.zap.api.report.ZapReport;
import br.com.softplan.security.zap.api.report.ZapReportUtil;
import br.com.softplan.security.zap.api.util.BaseIT;

public class ZapClientIT extends BaseIT {

	private static final String TARGET_404_URL = "http://server17:8180/404";
	private static final String WRONG_TARGET_URL = "http://blabla.blablabla";
	
	private static final int ANALYSIS_TIMEOUT = 240;

	@Test
	public void shouldGenerateAndSaveTheReports() {
		ZapReport zapReport = zapClient.analyze(new AnalysisInfo(TARGET_404_URL, ANALYSIS_TIMEOUT));
		File zapReportFile = ZapReportUtil.saveHtmlReport(zapReport);
		
		assertTrue(zapReportFile.exists());
		assertFalse(zapReport.getHtmlReportAsString().isEmpty());
		assertFalse(zapReport.getXmlReportAsString().isEmpty());
	}

	@Test(expectedExceptions = ZapClientException.class)
	public void shouldFailInCasoOfAnInvalidTargetUrl() {
		zapClient.analyze(new AnalysisInfo(WRONG_TARGET_URL, ANALYSIS_TIMEOUT));
	}

}
