#!/bin/bash
for fenix in $(cat fenix2.txt); do
	cmd="ssh $fenix ./cancelThis.sh";	echo $cmd; $cmd;
done
