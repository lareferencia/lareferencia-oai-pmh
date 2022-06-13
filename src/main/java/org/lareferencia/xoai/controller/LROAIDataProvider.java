/*******************************************************************************
 * Copyright (c) 2013, 2019 LA Referencia / Red CLARA and others
 *
 * This file is part of LRHarvester v4.x software
 *
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *     For any further information please contact
 *     Lautaro Matas <lmatas@gmail.com>
 *******************************************************************************/
package org.lareferencia.xoai.controller;

import ch.qos.logback.classic.util.ContextInitializer;
import com.hp.hpl.jena.reasoner.ValidityReport.Report;
import com.lyncode.xoai.dataprovider.OAIDataProvider;
import com.lyncode.xoai.dataprovider.OAIRequestParameters;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.services.api.RepositoryConfiguration;

import org.apache.log4j.Logger;
import org.lareferencia.xoai.services.api.cache.XOAICacheService;
import org.lareferencia.xoai.services.api.config.XOAIManagerResolver;
import org.lareferencia.xoai.services.api.config.XOAIManagerResolverException;
import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;
import org.lareferencia.xoai.Context;
import org.lareferencia.xoai.services.api.xoai.IdentifyResolver;
import org.lareferencia.xoai.services.api.xoai.ItemRepositoryResolver;
import org.lareferencia.xoai.services.api.xoai.SetRepositoryResolver;
import org.lareferencia.xoai.services.impl.xoai.LRResumptionTokenFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.apache.log4j.Logger.getLogger;
import org.lareferencia.xoai.model.XOAIContextComparator;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
@Controller
public class LROAIDataProvider
{
    private static final Logger log = getLogger(LROAIDataProvider.class);

    @Autowired XOAICacheService cacheService;
    @Autowired ContextService contextService;
    @Autowired XOAIManagerResolver xoaiManagerResolver;
    @Autowired ItemRepositoryResolver itemRepositoryResolver;
    @Autowired IdentifyResolver identifyResolver;
    @Autowired SetRepositoryResolver setRepositoryResolver;

    private LRResumptionTokenFormatter resumptionTokenFormat = new LRResumptionTokenFormatter();

    @RequestMapping("/")
    public String indexAction (HttpServletResponse response, Model model) throws ServletException {
        try {
            XOAIManager manager = xoaiManagerResolver.getManager();            
            Collection<XOAIContext> contextos = manager.getContextManager().getContexts();
            //Collection<XOAIContext> contextosRedalyc = new ArrayList<XOAIContext>();
            
            //Recupera el contexto default
            Stream<XOAIContext> contexto = contextos.stream()
                    .filter(r -> r.getName().contains("Default"));
            
            //Recupera los contextos de paises en orden alfabetico por nombre
            Stream<XOAIContext> paises = contextos.stream()
                    .filter(r -> (r.getDescription().contains("documents from countries")))
                    .sorted(new XOAIContextComparator());
            
            //Recupera los contextos de instituciones en orden alfabetico por nombre
            Stream<XOAIContext> instituciones = contextos.stream()
                    .filter(r -> (r.getDescription().contains("documents from institutions")))
                    .sorted(new XOAIContextComparator());
            
            //Une los contextos, primero el default con paises, y de esta union con instituciones
            //para que queden ordenados mediante estos grupos
            Collection<XOAIContext> contextosRedalyc = Stream.concat(
                    Stream.concat(contexto, paises), instituciones)
                    .collect(Collectors.toList());
            
//            for(XOAIContext c : contextos) {
//                System.out.println("NAME: " + c.getName());
//                if(!c.getName().contains("LAReferencia") 
//                        && !c.getName().contains("Driver") 
//                        && !c.getName().contains("Doctoral and Master Thesis") 
//                        && !c.getName().contains("OpenAIRE")
//                        && !c.getName().contains("Default")) {
//                     contextosRedalyc.add(c);
//                     //break;
//                }  
//            }
            //Agrega los contextos para iterarlos en la pagina principal
            model.addAttribute("contexts", contextosRedalyc);
            //model.addAttribute("contexts", manager.getContextManager().getContexts());
            response.setStatus(SC_BAD_REQUEST);
        } catch (XOAIManagerResolverException e) {
            throw new ServletException("Unable to load XOAI manager, please, try again.", e);
        }
        return "index";
    }

    @RequestMapping("/{context}")
    public String contextAction (Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("context") String xoaiContext) throws IOException, ServletException {
        Context context = null;
        try {
            request.setCharacterEncoding("UTF-8");
            context = contextService.getContext();
            context.setRequest(request);

            XOAIManager manager = xoaiManagerResolver.getManager();
            
            RepositoryConfiguration identify = identifyResolver.getIdentify();

            OAIDataProvider dataProvider = new OAIDataProvider(manager, xoaiContext,
                    identify,
                    setRepositoryResolver.getSetRepository(),
                    itemRepositoryResolver.getItemRepository(),
                    resumptionTokenFormat);

            OutputStream out = response.getOutputStream();
            OAIRequestParameters parameters = new OAIRequestParameters(buildParametersMap(request));

            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");

            String identification = xoaiContext + parameters.requestID();

            if (cacheService.isActive()) {
                if (!cacheService.hasCache(identification))
                    cacheService.store(identification, dataProvider.handle(parameters));

                cacheService.handle(identification, out);
            } else dataProvider.handle(parameters, out);


            out.flush();
            out.close();

            closeContext(context);

        } catch (InvalidContextException e) {
            log.debug(e.getMessage(), e);
            return indexAction(response, model);
        } catch (ContextServiceException e) {
            log.error(e.getMessage(), e);
            closeContext(context);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unexpected error while writing the output. For more information visit the log files.");
        } catch (XOAIManagerResolverException e) {
            throw new ServletException("OAI 2.0 wasn't correctly initialized, please check the log for previous errors", e);
        } catch (OAIException e) {
            log.error(e.getMessage(), e);
            closeContext(context);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unexpected error. For more information visit the log files.");
        } catch (WritingXmlException e) {
            log.error(e.getMessage(), e);
            closeContext(context);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unexpected error while writing the output. For more information visit the log files.");
        } catch (XMLStreamException e) {
            log.error(e.getMessage(), e);
            closeContext(context);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unexpected error while writing the output. For more information visit the log files.");
        } finally {
            closeContext(context);
        }

        return null; // response without content
    }

    private void closeContext(Context context) {
        if (context != null)
            context.abort();
    }

	private Map<String, List<String>> buildParametersMap(
			HttpServletRequest request) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] values = request.getParameterValues(name);
			map.put(name, asList(values));
		}
		return map;
	}

}
