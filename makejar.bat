echo CLEANING
del .\bin\*.class
echo BUILDING
javac -d .\bin .\src\*.java
echo MAKING JAR
jar cfm .\bin\MotorDiagram.jar .\bin\Manifest.txt .\bin\*.class