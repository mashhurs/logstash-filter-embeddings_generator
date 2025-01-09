/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V.
 * under one or more contributor license agreements. Licensed under the
 * Elastic License 2.0; you may not use this file except in compliance
 * with the Elastic License 2.0.
 */

package org.logstash.plugins.filter.generator;

import ai.djl.MalformedModelException;
import ai.djl.huggingface.translator.TextEmbeddingTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmbeddingsGenerator {

    private static final String DJL_MODEL = "sentence-transformers/all-MiniLM-L6-v2";
    private static final String DJL_PATH = "djl://ai.djl.huggingface.pytorch/" + DJL_MODEL;

    private final static Logger logger = LogManager.getLogger(EmbeddingsGenerator.class);

    private static Predictor<String, float[]> predictor;

    public EmbeddingsGenerator() throws ModelNotFoundException, MalformedModelException, IOException {
        logger.info("Using PyTorch engine.");
        logger.info("Using " + DJL_MODEL + " model.");

        Criteria<String, float[]> criteria =
                Criteria.builder()
                        .setTypes(String.class, float[].class)
                        .optModelUrls(DJL_PATH)
                        .optEngine("PyTorch")
                        .optTranslatorFactory(new TextEmbeddingTranslatorFactory())
                        .build();

        logger.info("Loading a model.");
        ZooModel<String, float[]> model = criteria.loadModel();
        logger.info("Successfully loaded model.");
        predictor = model.newPredictor();
        logger.info("Successfully created a predictor, translates text into vector.");
    }

    private static final List<Float> result = new ArrayList<>();
    synchronized public List<Float> getEmbeddings(String text) throws TranslateException {
        float[] data = predictor.predict(text);
        // TODO: temporary cast to align with Logstash Valuefier.Converter
        result.clear();
        for (float datum : data) {
            result.add(datum);
        }
        return result;
    }

    public void close() {
        logger.info("Closing the predictor.");
        predictor.close();
    }
}
