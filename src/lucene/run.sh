src="/home/liefe/lucene-6.5.1/analysis/common/lucene-analyzers-common-6.5.1.jar:/home/liefe/lucene-6.5.1/core/lucene-core-6.5.1.jar:/home/liefe/lucene-6.5.1/queryparser/lucene-queryparser-6.5.1.jar:/home/liefe/lucene-6.5.1/classification/lucene-classification-6.5.1.jar:/home/liefe/lucene-6.5.1/codecs/lucene-codecs-6.5.1.jar:/home/liefe/guava-19.0.jar"

javac -d ../../bin -cp $src *.java

#java -cp ~/lucene/* IndexFiles -index ../../index -docs ~/data/sample -update

# java -cp ../../bin:$src lucene.IndexFiles -index ../../index -docs ~/data/sample -update


java -cp ../../bin:$src lucene.IndexFiles -index ../../index -docs ~/data/sample -update -run 0
#java -cp ../../bin:$src lucene.Test


#java -cp ../../bin:$src lucene.SearchFiles -index ../../index




 

