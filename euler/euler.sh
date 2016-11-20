cp /modules/apps/cplex/12.6/cplex/lib/cplex.jar ./code/Java/lib/.
chmod +x *.sh
cd scripts
for sc in $(ls *.sh); do
    chmod +x $sc
    cmd="qsub $sc"; echo $cmd; $cmd;
done
