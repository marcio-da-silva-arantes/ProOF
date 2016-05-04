#!/bin/bash
for fenix in $(cat fenix.txt); do
	cmd="ssh $fenix ./cancelThis.sh";	echo $cmd; $cmd;
done
