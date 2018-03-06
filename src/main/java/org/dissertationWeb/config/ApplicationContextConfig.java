package org.dissertationWeb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

@Configuration//Spring special wording that is telling that this class is a configuration class
@ComponentScan(basePackages={"org.dissertationWeb.*"})//I am telling here to scan for the main files in the org.dissertationWeb folder
public class ApplicationContextConfig {

	/**
	 * Main tile bean that is creating the possibility of use tiles in Spring
	 * @return
	 */
	@Bean(name = "viewResolver")
	public ViewResolver getViewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();

		// TilesView 3
		viewResolver.setViewClass(TilesView.class);

		return viewResolver;
	}

	/**
	 * This Bean control the tile configuration, so is telling that the definition tile is tiles.xml in WEB-INF
	 * Tile.xml is telling how to redirect a string to a JSP file 
	 * @return
	 */
	@Bean(name = "tilesConfigurer")
	public TilesConfigurer getTilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();

		// TilesView 3
		tilesConfigurer.setDefinitions("/WEB-INF/tiles.xml");

		return tilesConfigurer;
	}
	/**
	 * This bean is the one taking care that I can use multipartResolver all around the application
	 * @return
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver resolver=new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}

}