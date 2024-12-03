PLUGIN_VERSION = File.read(File.expand_path(File.join(File.dirname(__FILE__), "VERSION"))).strip unless defined?(PLUGIN_VERSION)

Gem::Specification.new do |s|
  s.name          = 'logstash-filter-embeddings_generator'
  s.version       = PLUGIN_VERSION
  s.licenses      = ["Apache License (2.0)"]
  s.summary       = 'Logstash Filter Plugin to generate embeddings'
  s.description   = 'This plugin can be used to generate embeddings for GenAI RAG applications.'
  s.homepage      = 'https://github.com/mashhurs/logstash-filter-embeddings_generator'
  s.authors       = ['Elastic']
  s.email         = 'info@elastic.co'
  s.require_paths = %w[lib vendor/jar-dependencies]
  s.platform       = 'java'

  # Files
  s.files = Dir['lib/**/*','spec/**/*','vendor/**/*','*.gemspec','*.md','CONTRIBUTORS','Gemfile','LICENSE','NOTICE.TXT']
   # Tests
  s.test_files = s.files.grep(%r{^(test|spec|features)/})

  # Special flag to let us know this is actually a logstash plugin
  s.metadata = { "logstash_plugin" => "true", "logstash_group" => "filter" }

  # Gem dependencies
  s.add_runtime_dependency "logstash-core-plugin-api", "~> 2.0"

  s.add_development_dependency 'logstash-devutils'
end
