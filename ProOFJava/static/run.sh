if [ $# -lt 1 ]
then
	echo "Usage: $0 {Server}"
else
	./exec4.sh $1 &
fi
