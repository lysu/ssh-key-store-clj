task :default => :test

desc "run unit test"
task :test do
  sh 'rm target/classes -rf && lein javac && lein test'
end 

desc "generate standalone jar"
task :jar => :test do
  sh 'lein deps && rm *.jar pom.xml target/classes -rf && lein uberjar'
end

desc "Install in local repository"
task :install_local => :test do
  sh 'lein deps && rm *.jar pom.xml target/classes -rf && lein jar && lein install'
  sh 'cd ~/ && lein deps'
end

desc "Install in clojars repository"
task :clojars => :test do
  sh 'rm *.jar pom.xml target/classes -rf && lein pom && lein jar  '
  sh 'scp pom.xml *.jar clojars@clojars.org:'
end

desc "start swank server for emacs"
task :swank do
  sh 'rm target/classes -rf && lein javac && lein swank'
end
