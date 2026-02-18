cd /home/mateus/Documents/IntellijProjects/DLNA/;
echo Compilando...
mvn -q install;
cd /home/mateus/Documents/IntellijProjects/Misc;
java -classpath /home/mateus/Documents/IntellijProjects/DLNA/target/test-classes:/home/mateus/Documents/IntellijProjects/DLNA/target/classes:/home/mateus/.m2/repository/org/json/json/20251224/json-20251224.jar org.example.CommandGeneratorTest