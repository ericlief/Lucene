src="/afs/ms/u/l/liefe/lucene-7.2.1/lucene-7.2.1/analysis/common/lucene-analyzers-common-7.2.1.jar:/afs/ms/u/l/liefe/lucene-7.2.1/lucene-7.2.1/core/lucene-core-7.2.1.jar:/afs/ms/u/l/liefe/lucene-7.2.1/lucene-7.2.1/queryparser/lucene-queryparser-7.2.1.jar"

javac -d ../../bin -cp $src *.java

#java -cp ~/lucene/* IndexFiles -index ../../index -docs ~/data/sample -update

java -cp ../../bin:$src lucene.IndexFiles -index ../../index -docs ~/data/sample -update
