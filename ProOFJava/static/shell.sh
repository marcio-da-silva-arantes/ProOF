#!/bin/bash
cont=1
sum=0
for x in `/bin/ls ./job_local/waiting`; do
  if [ $cont -ge "$1" -a $cont -le "$2" ]; then
    cmd="java -Djava.library.path=/opt/ibm/ILOG/CPLEX_Studio126/cplex/bin/x86-64_linux -jar ./code/Java/Java.jar run ./job_local/waiting/$x ./input/"
    echo $cmd;
    $cmd> "$x.out" 2>&1
    sum=$(($sum+1))
  fi
  cont=$(($cont+1))
done
echo "End [$1 .. $2] $sum";
