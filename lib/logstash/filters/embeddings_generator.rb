# encoding: utf-8
require "logstash/filters/base"
require "logstash-filter-embeddings_generator_jars"

class LogStash::Filters::EmbeddingsGenerator < LogStash::Filters::Base

  config_name "embeddings_generator"

  config :source, :validate => :string, :default => "message"
  config :target, :validate => :string, :default => "embeddings"

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
    @generator = EmbeddingsGenerator.new
    fail "Failed to create a new EmbeddingsGenerator" if @generator.nil?

    fail "source param cannot be null or empty" if @source.empty?
    fail "target param cannot be null or empty" if @target.empty?
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
