#!/bin/bash
#usage ./shell_frist.sh <jump> <count>
cont=1
sum=0
for x in `/bin/ls ./job_local/waiting`; do
  if [ $(($sum+1)) -le "$2" ]; then
   cmd="java -Djava.library.path=/opt/ibm/ILOG/CPLEX_Studio126/cplex/bin/x86-64_linux -jar ./code/Java/Java.jar run ./job_local/waiting/$x ./input/"
   if [ -e "$x.out" ] ; then
    var=`sed -e :a -e '$q;N;2,$D;ba' "$x.out"`
    var2="#close\$results"
    if [ $var == $var2 ]; then
      echo -ne "$x -> ($var) -> ok                                              \r"
    else
      if [ $cont -le "$1" ]; then
        echo "$x -> ($var) -> no -> iginore"
        cont=$(($cont+1))
      else
        echo "$x -> ($var) -> no -> $cmd"
        $cmd> "$x.out" 2>&1
        sum=$(($sum+1))
      fi
    fi
   else
    if [ $cont -le "$1" ]; then
      echo "$x -> ($x.out not exist) -> no -> iginore"
      cont=$(($cont+1))
    else
      echo "$x -> ($x.out not exist) -> no -> $cmd"
      $cmd> "$x.out" 2>&1
      sum=$(($sum+1))
    fi
   fi
  fi
done
echo "End [$1 .. $2] $cont $sum";
