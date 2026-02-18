cd /home/mateus/Documents/IntellijProjects/Misc;#Videos top path
PROGRAM_FOLDER=/home/mateus/Documents/IntellijProjects/DLNA/target/
JAR=$PROGRAM_FOLDER/DLNA-1.0-SNAPSHOT.jar
if expr $1 = "--scan"
then  java -jar $JAR  "$(find -name '*.wmv') $(find -name '*.avi') $(find -name '*.mp4') $(find -name '*.mkv')" "scan" $PROGRAM_FOLDER
else java -jar $JAR "$(find -name '*.wmv') $(find -name '*.avi') $(find -name '*.mp4') $(find -name '*.mkv')" $PROGRAM_FOLDER
fi
JAVAOUTPUT=$(java -cp $JAR org.example.CommandPrinter $PROGRAM_FOLDER)
eval "$JAVAOUTPUT"
