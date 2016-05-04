#!/bin/bash
#usage	./exec_all.sh <executions by core> &

count=1
for fenix in $(cat fenix.txt); do
	cmd="ssh $fenix ./execThis.sh $count $1";	echo $cmd; $cmd;
	count=$(($count + $1 + $1 + $1 + $1));
done
