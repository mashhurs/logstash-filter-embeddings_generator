package org.logstash.plugins.filter.generator;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EmbeddingsGeneratorTest {

    private EmbeddingsGenerator generator;

    @Before
    public void setUp() throws ModelNotFoundException, MalformedModelException, IOException {
        generator = new EmbeddingsGenerator();
    }

    @Test
    public void testGetEmbeddings() throws TranslateException {
        float[] embeddings = generator.getEmbeddings("test");
        assertEquals (384, embeddings.length);
        System.out.println(Arrays.toString(embeddings));
    }
}