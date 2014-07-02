package org.stagemonitor.requestmonitor;

import com.codahale.metrics.MetricRegistry;
import org.stagemonitor.core.Configuration;
import org.stagemonitor.core.StageMonitorPlugin;
import org.stagemonitor.core.rest.RestClient;

import java.io.InputStream;

public class RequestMonitorPlugin implements StageMonitorPlugin {

	@Override
	public void initializePlugin(MetricRegistry metricRegistry, Configuration config) {
		addElasticsearchMapping(config.getElasticsearchUrl());
		RestClient.sendGrafanaDashboardAsync(config.getElasticsearchUrl(), "Request.json");
		RestClient.sendKibanaDashboardAsync(config.getElasticsearchUrl(), "Recent Requests.json");
	}

	private void addElasticsearchMapping(String serverUrl) {
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("stagemonitor-elasticsearch-index-template.json");
		// Sending non-asynchronously to avoid race conditions
		RestClient.sendAsJson(serverUrl, "/_template/stagemonitor", "PUT", resourceAsStream);
	}

}