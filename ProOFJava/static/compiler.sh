export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8
rm Java.jar
cp /opt/ibm/ILOG/CPLEX_Studio126/cplex/lib/cplex.jar ./lib/.
chmod +x ./lib/cplex.jar
cd bin/
rm -r *
cd ..
javac -verbose -sourcepath src/ -classpath lib/cplex.jar:lib/jsc.jar:lib/jdom.jar:lib/gif4j_pro_trial_2.3.jar:lib/Jama-1.0.3.jar:lib/jgrapht-core-0.9.1.jar src/ProOF/ProOF.java -d bin/
cd bin/
jar -cvfm ../Java.jar ../manifest.mf .
cd ..
chmod +x Java.jar

