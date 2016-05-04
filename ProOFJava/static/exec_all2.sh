#!/bin/bash
count=1
for fenix in $(cat fenix2.txt); do
	cmd="ssh $fenix ./execThis2.sh $count $1";	echo $cmd; $cmd;
	count=$(($count + $1 + $1 + $1 + $1));
done
