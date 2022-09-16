package com.smartbear.ready.plugin.jira.factories;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;
import com.smartbear.ready.plugin.jira.clients.AsynchronousJiraRestClientEx;
import com.smartbear.ready.plugin.jira.clients.AsynchronousJiraRestClientServerEx;

import java.net.URI;

public class AsynchronousJiraRestClientFactoryEx extends AsynchronousJiraRestClientFactory {
    @Override
    public JiraRestClient create(final URI serverUri, final AuthenticationHandler authenticationHandler) {
        final DisposableHttpClient httpClient = new AsynchronousHttpClientFactory()
                .createClient(serverUri, authenticationHandler);
        if (serverUri.getHost().contains("atlassian.net")) {
            return new AsynchronousJiraRestClientEx(serverUri, httpClient);
        } else {
            return new AsynchronousJiraRestClientServerEx(serverUri, httpClient);
        }
    }

    @Override
    public JiraRestClient createWithBasicHttpAuthentication(final URI serverUri, final String username, final String password) {
        return create(serverUri, new BasicHttpAuthenticationHandler(username, password));
    }

    @Override
    public JiraRestClient create(final URI serverUri, final HttpClient httpClient) {
        final DisposableHttpClient disposableHttpClient = new AsynchronousHttpClientFactory().createClient(httpClient);
        if (serverUri.getHost().contains("atlassian.net")) {
            return new AsynchronousJiraRestClientEx(serverUri, disposableHttpClient);
        } else {
            return new AsynchronousJiraRestClientServerEx(serverUri, disposableHttpClient);
        }
    }
}
