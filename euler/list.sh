total=0 
running=0 
waiting=0 
finished=0 
for x in `/bin/ls ./job_local/waiting`; do
   if [ -e "$x.out" ] ; then
    var=`sed -e :a -e '$q;N;2,$D;ba' "$x.out"`
    var2="#close\$results"
    if [ $var == $var2 ]; then
      echo -ne "$x -> ($finished | $running | $waiting) -> ok -> ($var) \r"
      finished=$(($finished+1))
    else
      echo -ne "$x -> ($finished | $running | $waiting) -> no -> ($var) \r"
      running=$(($running+1))
    fi
   else
    echo -ne "$x -> ($finished | $running | $waiting) -> no -> ($x.out not exist) \r"
    waiting=$(($waiting+1))
   fi
   total=$(($total+1)) 
done
echo "total jobs: $total | finished: $finished | running: $running | waiting: $waiting";
