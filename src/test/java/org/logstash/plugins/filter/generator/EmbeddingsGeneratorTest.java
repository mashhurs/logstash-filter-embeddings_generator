package org.logstash.plugins.filter.generator;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmbeddingsGeneratorTest {

    private EmbeddingsGenerator generator;

    @Before
    public void setUp() throws ModelNotFoundException, MalformedModelException, IOException {
        generator = new EmbeddingsGenerator();
    }

    @Test
    public void testGetEmbeddings() throws TranslateException {
        final String elasticBusinessDescription = "Elastic N.V., a search artificial intelligence (AI) company, delivers hosted and managed solutions designed to run in hybrid, public or private clouds, and multi-cloud environments in the United States and internationally. It primarily offers Elastic Stack, a set of software products that ingest and store data from various sources and formats, as well as performs search, analysis, and visualization on that data. The company's Elastic Stack product portfolio comprises Elasticsearch, a document store and search engine, and data store for various types of data, including textual, numerical, geospatial, structured, and unstructured; Kibana, a user interface, management, and configuration interface for the Elastic Stack; Elasticsearch Relevance Engine, which combines AI with its text search to give developers a suite of retrieval algorithms and the ability to integrate with large language models; Elastic Agent that offers integrated host protection and central management solutions; Logstash, a data processing pipeline for ingesting data into Elasticsearch or other storage systems from a multitude of sources simultaneously; and Beats, a single-purpose data shippers for sending data from edge machines to Elasticsearch or Logstash. It also provides software solutions on the Elastic Stack that address cases, including Elastic Search AI platform, for building search AI applications; Logs, to search and analyze petabytes of structured and unstructured logs; Metrics, to search and analyze numeric and time series data; Application Performance Monitoring, to deliver insight into application performance and health metrics; Synthetic Monitoring, to monitor the availability and functionality of user journeys; and security information and event management, and endpoint and cloud security solutions. The company was incorporated in 2012 and is based in Amsterdam, the Netherlands";
        List<Float> embeddings = generator.getEmbeddings(elasticBusinessDescription);
        assertEquals (384, embeddings.size());
        System.out.println(embeddings);
    }
}