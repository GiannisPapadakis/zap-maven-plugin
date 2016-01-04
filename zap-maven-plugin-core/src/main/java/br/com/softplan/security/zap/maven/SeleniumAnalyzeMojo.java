package br.com.softplan.security.zap.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import br.com.softplan.security.zap.api.ZapClient;
import br.com.softplan.security.zap.api.model.AnalysisInfo;
import br.com.softplan.security.zap.api.model.AnalysisType;
import br.com.softplan.security.zap.api.model.AuthenticationInfo;
import br.com.softplan.security.zap.api.report.ZapReport;
import br.com.softplan.security.zap.commons.ZapInfo;
import br.com.softplan.security.zap.commons.boot.Zap;

/**
 * Run ZAP's Active Scan and generates the reports. <b>No Spider is executed.</b>
 * This scan assumes that integration tests ran using ZAP as a proxy, so the Active Scan
 * is able to use the navigation done during the tests for the scan. 
 * <p>
 * Normally this goal will be executed in the phase <i>post-integration-test</i>, while the
 * goal {@code startZap} will run in the phase <i>pre-integration-test</i>, to make sure
 * ZAP is running during the tests. 
 * 
 * @author pdsec
 */
@Mojo(name="seleniumAnalyze")
public class SeleniumAnalyzeMojo extends ZapMojo {
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting ZAP analysis at target: " + super.getTarget());
		
		ZapInfo zapInfo = super.buildZapInfo();
		AuthenticationInfo authenticationInfo = super.buildAuthenticationInfo();
		
		AnalysisInfo analysisInfo = super.buildAnalysisInfo(AnalysisType.ACTIVE_SCAN_ONLY);

		ZapClient zapClient = new ZapClient(zapInfo, authenticationInfo);
		try {
			ZapReport zapReport = zapClient.analyze(analysisInfo);
			super.saveReport(zapReport);
		} finally {
			Zap.stopZap();
		}

		getLog().info("ZAP analysis finished.");
	}
	
}
