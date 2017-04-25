#usage	./cancel.sh
cd scripts
for id in $(cat Job_ID.txt); do
	cmd="qdel $id";	echo $cmd; $cmd;
done
