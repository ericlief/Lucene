javac -d ../bin -cp ~/lucene src/*.java

#java -cp ~/lucene/* IndexFiles -index ../../index -docs ~/data/sample -update

java -cp ../../bin;~/lucene;  IndexFiles -index ../../index -docs ~/data/sample -update
