src="/home/liefe/lucene-7.2.1/analysis/common/lucene-analyzers-common-7.2.1.jar:/home/liefe/lucene-7.2.1/core/lucene-core-7.2.1.jar:/home/liefe/lucene-7.2.1/queryparser/lucene-queryparser-7.2.1.jar:/home/liefe/lucene-7.2.1/classification/lucene-classification-7.2.1.jar"

javac -d ../../bin -cp $src *.java

#java -cp ~/lucene/* IndexFiles -index ../../index -docs ~/data/sample -update

# java -cp ../../bin:$src lucene.IndexFiles -index ../../index -docs ~/data/sample -update
#java -cp ../../bin:$src lucene.IndexFiles -index ../../index -docs ~/data/sample -updatew
java -cp ../../bin:$src lucene.Test







 
