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
package org.lareferencia.xoai.app;


import static java.lang.Integer.MAX_VALUE;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.lareferencia.xoai.ConfigurationManager;
import org.lareferencia.xoai.services.api.xoai.ItemRepositoryResolver;
import org.lareferencia.xoai.services.impl.xoai.LRItemRepositoryResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import com.lyncode.jtwig.mvc.JtwigViewResolver;

//import com.lyncode.jtwig.mvc.JtwigViewResolver;

@SpringBootApplication
@Import({BasicConfiguration.class})
@Configuration
@EnableWebMvc
@ComponentScan("org.lareferencia.xoai.controller")
public class MainApp implements WebMvcConfigurer {
	
	@Value( "${xoai.config.path}" )
	private String config_file_path;

	public static void main(String[] args) {
		
		SpringApplicationBuilder builder =  new SpringApplicationBuilder(MainApp.class);
		//builder.initializers(new MainAppContextInitializer());
		builder.run(args);
	 
	}



    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
            	
            	try 
                {
                    ConfigurationManager.loadConfig(config_file_path);
                }
                catch (RuntimeException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    throw new IllegalStateException("\nXOAI failed to initialize. Error while attempting to read the XOAI configuration file (Path: '" + config_file_path  + "\n" );
                }
            }
        };
    }
    

    @Bean
    public ItemRepositoryResolver xoaiItemRepositoryResolver() {
        return new LRItemRepositoryResolver();
    }
    
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/")
                .setCachePeriod(MAX_VALUE);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	

}
