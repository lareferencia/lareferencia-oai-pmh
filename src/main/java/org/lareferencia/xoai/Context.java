/*
 * Copyright (c) 2013, 2026 LA Referencia / Red CLARA and others.
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package org.lareferencia.xoai;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;

/**
 * Request-scoped state used while producing an OAI-PMH response.
 *
 * <p>The provider no longer opens a DSpace database transaction. The retained
 * type keeps the service boundary stable while carrying the current servlet
 * request for repository URL resolution.</p>
 */
public class Context {

    private HttpServletRequest request;

    public Context() throws SQLException {
        // Kept for source compatibility with the existing context service.
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void abort() {
        request = null;
    }
}
