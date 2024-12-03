# encoding: utf-8
require_relative '../spec_helper'
require "logstash/filters/embeddings_generator"

describe LogStash::Filters::EmbeddingsGenerator do
  describe "Set to Hello World" do
    let(:config) do <<-CONFIG
      filter {
        embeddings_generator { }
      }
    CONFIG
    end

  end
end
