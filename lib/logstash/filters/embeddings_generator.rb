# encoding: utf-8

########################################################################
# Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V.
# under one or more contributor license agreements. Licensed under the
# Elastic License 2.0; you may not use this file except in compliance
# with the Elastic License 2.0.
########################################################################

require "logstash/filters/base"
require "logstash-filter-embeddings_generator_jars"

class LogStash::Filters::EmbeddingsGenerator < LogStash::Filters::Base

  config_name "embeddings_generator"

  config :source, :validate => :string, :default => "message"
  config :target, :validate => :string, :default => "embeddings"

  # `path` cane be local model, remote full path or base (example: huggingface pytorch) URL
  #  Examples:
  #   path => "djl://ai.djl.huggingface.pytorch"                # from huggingdface
  #   model_name => "BAAI/bge-small-en-v1.5"                    # dimension 384
  #   model_name => "sentence-transformers/all-MiniLM-L6-v2"    # dimension 384
  #   model_name => "eeeyounglee/EEVE-10.8B-mean-4096-4"        # DJL doesn't support by default, need conversion
  #   download EEVE-10.8B-mean-4096-4 from huggingface and convert into torchscript format
  #   then point to local model
  #   path => "/path-to-local/model/EEVE-10.8B-mean-4096-4"
  config :path, :validate => :string
  config :model_name, :validate => :string

  java_import 'org.logstash.plugins.filter.generator.EmbeddingsGenerator'

  # TODO:
  #   engine config support: (PyTorch | Rust | Apache MXNet | TensorFlow | ONNXRuntime)
  #     note that engine params can be configured through ENV var or jvm.options
  #     https://docs.djl.ai/master/docs/development/inference_performance_optimization.html
  #     https://github.com/deepjavalibrary/djl/blob/master/api/src/main/java/ai/djl/engine/Engine.java#L82-L83
  #   make model (ex: all-MiniLM-L6-v2) and LLM community (huggingface) configurable
  #   make target run H/W configurable: (CPU | GPU)

  attr_reader :generator

  public
  def register
    fail "`source` param cannot be null or empty" if @source.empty?
    fail "`target` param cannot be null or empty" if @target.empty?

    fail "`path` cannot be null or empty" if @path.nil? || @path.empty?

    @generator = EmbeddingsGenerator.new(@path, @model_name)
    fail "Failed to create a new EmbeddingsGenerator" if @generator.nil?
  end

  public
  def filter(event)
    # TODO: catch exceptions and add a failure tag
    #   for now to get to know all exceptions, not handling them
    #   event.get(@source) should be String class
    #   event.get(@source) should not be nil or empty
    embeddings = @generator.getEmbeddings(event.get(@source))
    event.set(@target, embeddings)
  end

  def close
    @generator.close
  end
end
