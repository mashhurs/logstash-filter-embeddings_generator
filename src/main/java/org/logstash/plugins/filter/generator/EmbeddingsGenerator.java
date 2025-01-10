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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmbeddingsGenerator {

    private final static Logger logger = LogManager.getLogger(EmbeddingsGenerator.class);

    private static Predictor<String, float[]> predictor;

    public EmbeddingsGenerator(final String path, final String modelName) throws ModelNotFoundException, MalformedModelException, IOException {
        logger.info("Using PyTorch engine.");
        logger.info("Using " + path + " path.");

        Criteria.Builder<String, float[]> criteriaBuilder = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optEngine("PyTorch")
                .optTranslatorFactory(new TextEmbeddingTranslatorFactory());

        if (Objects.isNull(modelName)) {
            criteriaBuilder.optModelPath(Paths.get(path));
        } else {
            logger.info("Using " + modelName + " model.");
            final String modelUrl = path.concat("/").concat(modelName);
            criteriaBuilder.optModelUrls(modelUrl);
        }

        Criteria<String, float[]> criteria = criteriaBuilder.build();

        logger.info("Loading a model.");
        ZooModel<String, float[]> model = criteria.loadModel();
        logger.info("Successfully loaded model.");
        predictor = model.newPredictor();
        logger.info("Successfully created a predictor, translates text into vector.");
    }

    private static final List<Float> result = new ArrayList<>();
    synchronized public List<Float> getEmbeddings(String text) throws TranslateException {
        logger.info("Started to generate embeddings for the input text");
        float[] data = predictor.predict(text);
        logger.info("Embeddings size: " + data.length);
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
