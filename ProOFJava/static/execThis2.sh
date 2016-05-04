#!/bin/bash
cmd="cd UAV3D/client_space2/";			echo $cmd; $cmd;
cmd="./exec_line.sh $1 $2";			echo $cmd; $cmd> "this.log" 2>&1;
