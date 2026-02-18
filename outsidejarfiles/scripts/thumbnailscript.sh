cd /home/mateus/Documents/IntellijProjects/DLNA/ #Root path of non compiled project
echo Compilando...
mvn -q install
cd /home/mateus/Documents/IntellijProjects/Misc;#Videos top path
PROGRAM_FOLDER=/home/mateus/Documents/IntellijProjects/DLNA/outsidejarfiles/
CP=/home/mateus/Documents/IntellijProjects/DLNA/target/classes:/home/mateus/.m2/repository/org/json/json/20251224/json-20251224.jar;
if expr ${1-"noarg"} = "--scan" > /dev/null
then  java -cp $CP org.example.CommandGenerator "$(find -name '*.wmv') $(find -name '*.avi') $(find -name '*.mp4') $(find -name '*.mkv')" "scan" $PROGRAM_FOLDER
elif expr ${1-"noarg"}= "--command" > /dev/null
then java -cp $CP org.example.CommandGenerator "$(find -name '*.wmv') $(find -name '*.avi') $(find -name '*.mp4') $(find -name '*.mkv')" "command" $PROGRAM_FOLDER
else java -cp $CP org.example.CommandGenerator "$(find -name '*.wmv') $(find -name '*.avi') $(find -name '*.mp4') $(find -name '*.mkv')" $PROGRAM_FOLDER
fi
JAVAOUTPUT=$(java -cp $CP org.example.CommandPrinter $PROGRAM_FOLDER)
eval "$JAVAOUTPUT"
