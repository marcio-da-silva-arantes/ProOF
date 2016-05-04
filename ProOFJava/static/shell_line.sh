#!/bin/bash
cont=1
sum=0
for x in `/bin/ls ./job_local/waiting`; do
  if [ $cont -ge "$1" -a $cont -le "$2" ]; then
   cmd="java -Djava.library.path=/opt/ibm/ILOG/CPLEX_Studio126/cplex/bin/x86-64_linux -jar ./code/Java/Java.jar run ./job_local/waiting/$x ./input/"
   if [ -e "$x.out" ] ; then
    var=`sed -e :a -e '$q;N;2,$D;ba' "$x.out"`
    var2="#close$results"
    if [ $var != $var2 ]; then
      echo "$x -> ($var) -> ok"
    else
      echo "$x -> ($var) -> no -> $cmd"
      $cmd> "$x.out" 2>&1
      sum=$(($sum+1))
    fi
   else
    echo "$x -> ($x.out not exist) -> no -> $cmd"
    $cmd> "$x.out" 2>&1
    sum=$(($sum+1))
   fi
  fi
  cont=$(($cont+1))
done
echo "End [$1 .. $2] $sum";
